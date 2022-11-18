package acceptance

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.GeneralAssertion.step

class testSingletonLibrary extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initializeFromFile("singletonLibrary.groovy")
            jenkinson.runMethod("call")
        }
    }

    void test_stepIsCalled_insideLibrary() {
        assert step("echo","param:").isCalled()
    }

    void test_stepIsCalled_insideNestedLibrary() {
        assert step("echo","innerParam:").isCalled()
    }

    void test_assertingExistingStep_withUnexistingParameter_isFalse() {
        assert step("echo","unexisting").isCalled() == false
    }

    void test_assertingUnexistingStep_withExistingParameter_isFalse() {
        assert step("unexistingStep","param:").isCalled() == false
    }

}
