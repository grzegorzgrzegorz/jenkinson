package resultStack

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase

class testResultStackValidator extends GroovyTestCase {


    void testUnexistingStageAssertion_returnsFalse_evenWhenStepExists() {
        //GIVEN
        String scriptContent = 'echo "testing"'
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("unexisting stage", "echo", null) == false
    }

    void testExistingStageAssertion_returnsTrue_onExistingStep() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "echo", null)
    }

    void testExistingStageAssertion_returnsTrue_onExistingStepWithParam() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "echo", "testing")
    }

    void testExistingStageAssertion_returnsFalse_onExistingStepWithUnexistingParam() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "echo", "testing2") == false
    }

    void testExistingStageAssertion_returnsFalse_onUnexistingStepWithExistingParam() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "echo2", "testing") == false
    }

    void testExistingStageAssertion_returnsFalse_onUnexistingStep() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemCallsStepWithParam("test", "unexisitingStep", null) == false
    }

    void testUnexistingStageAssertion_returnsFalse_onExistingEnvVariable() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemHasEnvVariable("unexisting", "TEST1") == false
    }

    void testExistingStageAssertion_returnsFalse_onUnexistingEnvVariable() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemHasEnvVariable("test", "unexistingVariable") == false
    }

    void testExistingStageAssertion_returnsTrue_onExistingEnvVariable() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def jenkinson = Jenkinson.initializeFromText(scriptContent)
        //WHEN
        jenkinson.run()
        //THEN
        assert jenkinson.getResultStackValidator().declarativeItemHasEnvVariable("test", "TEST1")
    }
}
