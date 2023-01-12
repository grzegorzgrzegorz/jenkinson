package acceptance

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase
import static com.passfailerror.assertion.DeclarativeAssertion.stage

class testStepVariants extends GroovyTestCase{

    Jenkinson jenkinson

    void setUp(){
        jenkinson = Jenkinson.initializeFromFile("pipeline_with_emulator.groovy")
    }

    void test_step_returns_null(){
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('First stage').calls("echo", "defaultResult:null")
    }

    void test_shStep_isExecuted(){
        //GIVEN
        jenkinson.emulateStep("sh").parameters(["git --version"]).setRealExecution()
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Second stage').calls("echo", "resultBasingOnRealExecution:git version")
    }

    class CustomShEmulator{
        def run(params){
            def script = getScript(params)
            def result = getFirstParameter(script)
            return result+" computed result"
        }

        def getFirstParameter(script){
            return script.split(" ")[1]
        }

        def getScript(params){
            return params[0]["script"]
        }
    }

    void test_shStep_isExecuted_usingCustomCode(){
        //GIVEN
        jenkinson.emulateStep("sh").parameters(["complicatedAppWhichComputesResultInProduction"]).setEmulator(new CustomShEmulator())
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Third stage').calls("echo", "resultBasingOnCustomCode:inputData computed result")
    }

    void test_step_returnsMockedValue(){
        //GIVEN
        jenkinson.emulateStep("sh").parameters(["otherApp"]).returnValue("mocked result")
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Fourth stage').calls("echo", "mockedResult:mocked result")
    }
}
