package acceptance

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.the

class testStepVariants extends GroovyTestCase {

    Jenkinson jenkinson

    void setUp() {
        jenkinson = Jenkinson.initializeFromFile("pipeline_with_emulator.groovy")
    }

    void test_step_returns_null() {
        //WHEN
        jenkinson.run()
        //THEN
        assert the(jenkinson).stage('First stage').calls("echo", "defaultResult:null")
    }

    void test_shStep_isExecuted() {
        //GIVEN
        jenkinson.executeStep("sh").parameters(["git --version"])
        //WHEN
        jenkinson.run()
        //THEN
        assert the(jenkinson).stage('Second stage').calls("echo", "resultBasingOnRealExecution:git version")
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

    void test_shStep_isExecuted_usingCustomCode() {
        //GIVEN
        jenkinson.emulateStep("sh").parameters(["complicatedAppWhichComputesResultInProduction"]).setEmulator(new CustomShEmulator())
        //WHEN
        jenkinson.run()
        //THEN
        assert the(jenkinson).stage('Third stage').calls("echo", "resultBasingOnCustomCode:inputData computed result")
    }

    void test_step_returnsMockedValue() {
        //GIVEN
        jenkinson.mockStep("sh").parameters(["otherApp"]).returnValue("mocked result")
        //WHEN
        jenkinson.run()
        //THEN
        assert the(jenkinson).stage('Fourth stage').calls("echo", "mockedResult:mocked result")
    } // ToDo: add ability to return value using closure
}
