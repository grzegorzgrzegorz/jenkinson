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

    void test_step_returns_null() {
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('First stage').calls("echo", "defaultResult:null")
    }

    void test_step_isExecuted() {
        //GIVEN
        jenkinson.executeStep("sh").parameters(["git --version"])
        //WHEN
        jenkinson.run()
        //THEN
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

    void test_step_isExecuted_usingCustomCode() {
        //GIVEN
        jenkinson.emulateStep("sh").parameters(["complicatedAppWhichComputesResultInProduction"]).setEmulator(new CustomShEmulator())
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Third stage').calls("echo", "resultBasingOnCustomCode:inputData computed result")
    }

    void test_step_isExecuted_usingClosureWithParameters() {
        //GIVEN
        def closure = { parameters -> return parameters[0].script}
        jenkinson.mockStep("sh").parameters(["complicatedAppWhichComputesResultInProduction"]).returnValue(closure)
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Third stage').calls("echo", "resultBasingOnCustomCode:complicatedAppWhichComputesResultInProduction inputData")
    }


    void test_step_isExecuted_usingParameterlessClosure() {
        //GIVEN
        def closure = {-> return "value from closure"}
        jenkinson.mockStep("parameterlessCustomStep").returnValue(closure)
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Third stage').calls("echo", "resultBasingOnCustomCode2:value from closure")
    }

    void test_step_returnsMockedValue() {
        //GIVEN
        jenkinson.mockStep("sh").parameters(["otherApp"]).returnValue("mocked result")
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Fourth stage').calls("echo", "mockedResult:mocked result")
    }

}
