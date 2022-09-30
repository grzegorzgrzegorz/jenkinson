import com.passfailerror.resultStack.ResultStackProcessor
import com.passfailerror.resultStack.ResultStackValidator
import com.passfailerror.syntax.Sections
import com.passfailerror.syntax.Steps
import groovy.test.GroovyTestCase

class testResultStackValidator extends GroovyTestCase {


    Script getScriptObject(String scriptContent) {
        ResultStackProcessor.initializeFromContent(scriptContent.tokenize(System.lineSeparator()))
        def binding = new Binding()
        binding.setProperty("env", [:])
        GroovyShell shell = new GroovyShell(binding)
        def scriptObject = shell.parse(scriptContent)
        Steps.instance.mock(scriptObject)
        Sections.instance.mock(scriptObject)
        return scriptObject
    }

    void testUnexistingStageAssertion_returnsFalse_evenWhenStepExists() {
        //GIVEN
        String scriptContent = 'echo "testing"'
        def scriptObject = getScriptObject(scriptContent)
        //WHEN
        scriptObject.run()
        //THEN
        assert ResultStackValidator.getInstance().stageCallsStep("unexisting stage", "echo") == false
    }

    void testExistingStageAssertion_returnsTrue_onExistingStep() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def scriptObject = getScriptObject(scriptContent)
        //WHEN
        scriptObject.run()
        //THEN
        assert ResultStackValidator.getInstance().stageCallsStep("test", "echo")
    }

    void testExistingStageAssertion_returnsFalse_onUnexistingStep() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
            }
            '''
        def scriptObject = getScriptObject(scriptContent)
        //WHEN
        scriptObject.run()
        //THEN
        assert ResultStackValidator.getInstance().stageCallsStep("test", "unexisitingStep") == false
    }

    void testUnexistingStageAssertion_returnsFalse_onExistingEnvVariable() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def scriptObject = getScriptObject(scriptContent)
        //WHEN
        scriptObject.run()
        //THEN
        assert ResultStackValidator.getInstance().stageHasEnvVariable("unexisting", "TEST1") == false
    }

    void testExistingStageAssertion_returnsFalse_onUnexistingEnvVariable() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def scriptObject = getScriptObject(scriptContent)
        //WHEN
        scriptObject.run()
        //THEN
        assert ResultStackValidator.getInstance().stageHasEnvVariable("test", "unexistingVariable") == false
    }

    void testExistingStageAssertion_returnsTrue_onExistingEnvVariable() {
        //GIVEN
        String scriptContent = '''
            stage("test"){
                echo "testing"
                env.TEST1="value"
            }
            '''
        def scriptObject = getScriptObject(scriptContent)
        //WHEN
        scriptObject.run()
        //THEN
        assert ResultStackValidator.getInstance().stageHasEnvVariable("test", "TEST1")
    }
}
