package acceptance

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.stage

class testStageAssertions extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
            jenkinson.run()
        }
    }


    void test_stepWasCalled() {
        assert stage("First stage").calls("sh")
    }

    void test_unexistingStepWasNotCalled() {
        assert stage("First stage").calls("unexisting") == false
    }

    void test_stageHasEnvVariable_setInside() {
        assert stage("First stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }

    void test_stageDoesntHave_unexistingEnvVariable() {
        assert stage("First stage").hasEnvVariable("unexisting") == false
    }

    void test_stageDoesntHaveEnvVariable_setInConsecutiveStep() {
        assert stage("First stage").hasEnvVariable("SECOND_STAGE_VAR") == false
    }

    void test_stageHasEnvVariable_setInPreviousStep() {
        assert stage("Second stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }
}
