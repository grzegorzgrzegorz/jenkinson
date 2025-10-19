package acceptance.declarative

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.step

class generalAssertionsTest extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initializeFromFile("declarative/simplePipeline.groovy")
            jenkinson.run()
        }
    }

    void "test: existing step is called"() {
        assert step("sh").isCalled()
    }

    void "test: existing step with specific parameter is called"() {
        assert step("sh", "mvn").isCalled()
    }

    void "test: existing step outside stages section is called"() {
        assert step("label").isCalled()
    }

    void "test: existing step with specific parameter outside stages section is called"() {
        assert step("label", "test").isCalled()
    }

    void "test: unexisting step is not called"() {
        assert step("unexisting").isCalled() == false
    }

    void "test: existing step with unexisting parameter is not called"() {
        assert step("sh", "unexisting").isCalled() == false
    }

    void "test: existing step with unexisting parameter outside stages section is not called"() {
        assert step("label", "unexisting").isCalled() == false
    }
}
