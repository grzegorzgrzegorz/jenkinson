package acceptance

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.the

class testGeneralAssertions extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
            jenkinson.run()
        }
    }

    void test_stepIsCalled_returnsTrue_forExistingStep_insideStage() {
        assert the(jenkinson).step("sh").isCalled()
    }

    void test_stepIsCalled_returnsTrue_forExistingStepWithParam_insideStage() {
        assert the(jenkinson).step("sh", "mvn").isCalled()
    }

    void test_stepIsCalled_returnsTrue_forExistingStep_outsideStages() {
        assert the(jenkinson).step("label").isCalled()
    }

    void test_stepIsCalled_returnsTrue_forExistingStepWithParam_outsideStages() {
        assert the(jenkinson).step("label", "test").isCalled()
    }

    void test_stepIsCalled_returnsFalse_forUnexistingStep() {
        assert the(jenkinson).step("unexisting").isCalled() == false
    }

    void test_stepIsCalled_returnsFalse_forExistingStepWithUnexistingParam_insideStage() {
        assert the(jenkinson).step("sh", "unexisting").isCalled() == false
    }

    void test_stepIsCalled_returnsFalse_forExistingStepWithUnexistingParam_outsideStages() {
        assert the(jenkinson).step("label", "unexisting").isCalled() == false
    }
}
