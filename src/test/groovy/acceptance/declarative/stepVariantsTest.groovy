package acceptance.declarative

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.stage

class stepVariantsTest extends GroovyTestCase {

    Jenkinson jenkinson

    void setUp() {
        jenkinson = Jenkinson.initializeFromFile("declarative/pipeline_with_emulator.groovy")
        jenkinson.mockStep("parameterlessCustomStep")
    }

    void "test: step returns null"() {
        when:
        jenkinson.run()
        then:
        assert stage('First stage').calls("echo", "defaultResult:null")
    }

    void "test: step is executed"() {
        given:
        jenkinson.executeStep("sh").parameters(["git --version"])
        when:
        jenkinson.run()
        then:
        assert stage('Second stage').calls("echo", "resultBasingOnRealExecution:git version")
    }

    class CustomShEmulator {
        def run(params) {
            def script = getScript(params)
            def result = getFirstParameter(script)
            return result + " computed result"
        }

        def getFirstParameter(script) {
            return script.split(" ")[1]
        }

        def getScript(params) {
            return params[0]["script"]
        }
    }

    void "test: step is executed using custom code"() {
        given:
        jenkinson.emulateStep("sh").parameters(["complicatedAppWhichComputesResultInProduction"]).setEmulator(new CustomShEmulator())
        when:
        jenkinson.run()
        then:
        assert stage('Third stage').calls("echo", "resultBasingOnCustomCode:inputData computed result")
    }

    void "test: step is executed using closure with parameters"() {
        given:
        def closure = { parameters -> return parameters[0].script}
        jenkinson.mockStep("sh").parameters(["complicatedAppWhichComputesResultInProduction"]).returnValue(closure)
        when:
        jenkinson.run()
        then:
        assert stage('Third stage').calls("echo", "resultBasingOnCustomCode:complicatedAppWhichComputesResultInProduction inputData")
    }


    void "test: step is executed using parameterless closure"() {
        given:
        def closure = {-> return "value from closure"}
        jenkinson.mockStep("parameterlessCustomStep").returnValue(closure)
        when:
        jenkinson.run()
        then:
        assert stage('Third stage').calls("echo", "resultBasingOnCustomCode2:value from closure")
    }

    void "test: step returns mocked value"() {
        given:
        jenkinson.mockStep("sh").parameters(["otherApp"]).returnValue("mocked result")
        when:
        jenkinson.run()
        then:
        assert stage('Fourth stage').calls("echo", "mockedResult:mocked result")
    }

}
