package resultStack

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

class ResultStackValidatorTest extends GroovyTestCase {


    void "test: unexisting stage assertion returns False even when step exists"() {
        given:
        String scriptContent = 'echo "testing"'
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("unexisting stage", "echo", null) == false
    }

    void "test: existing stage assertion returns True on existing step"() {
        given:
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "echo", null)
    }

    void "test: existing stage assertion returns True on existing step with parameter"() {
        given:
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "echo", "testing")
    }

    void "test: existing stage assertion returns False on existing step with unexisting parameter"() {
        given:
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "echo", "testing2") == false
    }

    void "test: existing stage assertion returns False on unexisting step with existing param"() {
        given:
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "echo2", "testing") == false
    }

    void "test: existing stage assertion returns false on unexisting step"() {
        given:
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "unexisitingStep", null) == false
    }

    void "test: unexisting stage assertion returns false on existing env variable"() {
        given:
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemHasEnvVariable("unexisting", "TEST1") == false
    }

    void "test: existing stage assertion returns false on unexisting env variable"() {
        given:
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemHasEnvVariable("test", "unexistingVariable") == false
    }

    void "test: existing stage assertion returns True on existing env variable"() {
        given:
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        when:
        jenkinson.run()
        then:
        assert jenkinson.getResultStackValidator().declarativeItemHasEnvVariable("test", "TEST1")
    }
}
