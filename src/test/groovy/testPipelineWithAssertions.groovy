import com.passfailerror.Jenkinson
import com.passfailerror.assertion.Assertion
import groovy.test.GroovyTestCase

class testPipelineWithAssertions extends GroovyTestCase {


    void setUp() {
        def jenkinson = new Jenkinson()
        jenkinson.put("simplePipeline.groovy")
        jenkinson.run()
    }


    void test_stepWasCalled() {
        assert Assertion.stage("First stage").calls("sh")
    }

    void test_unexistingStepWasNotCalled() {
        assert Assertion.stage("First stage").calls("unexisting") == false
    }

    void test_stageHasEnvVariable_setInside() {
        assert Assertion.stage("First stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }

    void test_stageDoesntHave_unexistingEnvVariable() {
        assert Assertion.stage("First stage").hasEnvVariable("unexisting") == false
    }

    void test_stageDoesntHaveEnvVariable_setInConsecutiveStep() {
        assert Assertion.stage("First stage").hasEnvVariable("SECOND_STAGE_VAR") == false
    }

    void test_stageHasEnvVariable_setInPreviousStep() {
        assert Assertion.stage("Second stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }
}
