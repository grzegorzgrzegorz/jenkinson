package acceptance

import com.passfailerror.Jenkinson
import com.passfailerror.assertion.DeclarativeAssertion
import groovy.test.GroovyTestCase

class testStageAssertions extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = new Jenkinson()
            jenkinson.put("simplePipeline.groovy")
            jenkinson.run()
        }
    }


    void test_stepWasCalled() {
        assert DeclarativeAssertion.stage("First stage").calls("sh")
    }

    void test_unexistingStepWasNotCalled() {
        assert DeclarativeAssertion.stage("First stage").calls("unexisting") == false
    }

    void test_stageHasEnvVariable_setInside() {
        assert DeclarativeAssertion.stage("First stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }

    void test_stageDoesntHave_unexistingEnvVariable() {
        assert DeclarativeAssertion.stage("First stage").hasEnvVariable("unexisting") == false
    }

    void test_stageDoesntHaveEnvVariable_setInConsecutiveStep() {
        assert DeclarativeAssertion.stage("First stage").hasEnvVariable("SECOND_STAGE_VAR") == false
    }

    void test_stageHasEnvVariable_setInPreviousStep() {
        assert DeclarativeAssertion.stage("Second stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }
}
