package acceptance.declarative

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

import static com.passfailerror.assertion.Assertion.stage

class stageAssertionsTest extends GroovyTestCase {

    def jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initializeFromFile("declarative/simplePipeline.groovy")
            jenkinson.run()
        }
    }


    void "test: specific step is called by specific stage"() {
        assert stage("First stage").calls("sh")
    }

    void "test: unexisting step is not called"() {
        assert stage("First stage").calls("unexisting") == false
    }

    void "test: stage has specific env variable set inside its scope"() {
        assert stage("First stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }

    void "test: stage doesnt have specific env variable set inside its scope"() {
        assert stage("First stage").hasEnvVariable("unexisting") == false
    }

    void "test: stage doesnt have env variable inside its scope which is set in the next step"() {
        assert stage("First stage").hasEnvVariable("SECOND_STAGE_VAR") == false
    }

    void "test: stage has env variable inside its scope which is set in previous step"() {
        assert stage("Second stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }
}
