package acceptance.libraryTypes

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.step

class singletonLibraryTest extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initializeFromFile("singletonLibrary/singletonLibrary.groovy")
            jenkinson.runMethod("call")
        }
    }

    void "test: step inside library is called"() {
        assert step("echo", "param:").isCalled()
    }

    void "test: step inside nested library is called"() {
        assert step("echo", "innerParam:").isCalled()
    }

    void "test: existing step with specific unexisting parameter is not called"() {
        assert step("echo", "unexisting").isCalled() == false
    }

    void "test: unexisting step with specific existing parameter is not called"() {
        assert step("unexistingStep", "param:").isCalled() == false
    }

}
