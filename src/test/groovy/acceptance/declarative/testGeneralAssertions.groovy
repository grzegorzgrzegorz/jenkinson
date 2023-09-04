package acceptance.declarative

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.step

class testGeneralAssertions extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initializeFromFile("declarative/simplePipeline.groovy")
            jenkinson.run()
        }
    }

    void test_stepIsCalled_returnsTrue_forExistingStep_insideStage() {
        assert step("sh").isCalled()
    }

    void test_stepIsCalled_returnsTrue_forExistingStepWithParam_insideStage() {
        assert step("sh", "mvn").isCalled()
    }

    void test_stepIsCalled_returnsTrue_forExistingStep_outsideStages() {
        assert step("label").isCalled()
    }

    void test_stepIsCalled_returnsTrue_forExistingStepWithParam_outsideStages() {
        assert step("label", "test").isCalled()
    }

    void test_stepIsCalled_returnsFalse_forUnexistingStep() {
        assert step("unexisting").isCalled() == false
    }

    void test_stepIsCalled_returnsFalse_forExistingStepWithUnexistingParam_insideStage() {
        assert step("sh", "unexisting").isCalled() == false
    }

    void test_stepIsCalled_returnsFalse_forExistingStepWithUnexistingParam_outsideStages() {
        assert step("label", "unexisting").isCalled() == false
    }
}
