package acceptance

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase
import static com.passfailerror.assertion.DeclarativeAssertion.stage

class testStepVariants extends GroovyTestCase{

    def jenkinson

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
        jenkinson.emulateStep("sh").setRealExecutions(["git --version"])
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Second stage').calls("echo", "resultBasingOnRealExecution:git version")
    }

    void test_shStep_isExecuted_usingCustomCode(){
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Third stage').calls("echo", "resultBasingOnCustomCode:computed result")
    }

    void test_step_returnsMockedValue(){
        //WHEN
        jenkinson.run()
        //THEN
        assert stage('Fourth stage').calls("echo", "mockedResult:mocked result")
    }
}
