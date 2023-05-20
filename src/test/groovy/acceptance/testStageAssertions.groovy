package acceptance

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.the

class testStageAssertions extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
            jenkinson.run()
        }
    }


    void test_stepWasCalled() {
        assert the(jenkinson).stage("First stage").calls("sh")
    }

    void test_unexistingStepWasNotCalled() {
        assert the(jenkinson).stage("First stage").calls("unexisting") == false
    }

    void test_stageHasEnvVariable_setInside() {
        assert the(jenkinson).stage("First stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }

    void test_stageDoesntHave_unexistingEnvVariable() {
        assert the(jenkinson).stage("First stage").hasEnvVariable("unexisting") == false
    }

    void test_stageDoesntHaveEnvVariable_setInConsecutiveStep() {
        assert the(jenkinson).stage("First stage").hasEnvVariable("SECOND_STAGE_VAR") == false
    }

    void test_stageHasEnvVariable_setInPreviousStep() {
        assert the(jenkinson).stage("Second stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }
}
