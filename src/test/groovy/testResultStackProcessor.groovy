import com.passfailerror.resultStack.ResultStackEntry
import com.passfailerror.resultStack.ResultStackProcessor
import groovy.test.GroovyTestCase

class testResultStackProcessor extends GroovyTestCase{

    ResultStackEntry getLastItemOfInvocationStack() {
        return ResultStackProcessor.getResultStack().getInvocationStack().last()
    }

    static Script scriptObject

    void setUp() {
        String scriptContent = 'echo "testing"'
        ResultStackProcessor.initializeFromContent(scriptContent.tokenize(System.lineSeparator()))
        if (scriptObject == null) {
            def binding = new Binding()
            binding.setProperty("env", [:])
            GroovyShell shell = new GroovyShell(binding)
            scriptObject = shell.parse(scriptContent)
            scriptObject.metaClass.echo = { Object... params ->
                ResultStackProcessor.getInstance().storeInvocation("echo", params, scriptObject.getBinding().getVariables())
            }
        }
    }

    void testContentBasedCallStackPart_isString_andContainsExecutedLine() {
        //WHEN
        scriptObject.run()
        //THEN
        assert getLastItemOfInvocationStack().getFileContentBasedCallStack() instanceof String
        assert getLastItemOfInvocationStack().getFileContentBasedCallStack().contentEquals("echo \"testing\"")
    }

    void testInvocationsPart_isMap_andContainsInvokedCommand(){
        //WHEN
        scriptObject.run()
        //THEN
        assert getLastItemOfInvocationStack().getInvocations() instanceof Map
        assert getLastItemOfInvocationStack().getInvocations().containsKey("echo");
        assert getLastItemOfInvocationStack().getInvocations().get("echo") instanceof Object[]
        assert getLastItemOfInvocationStack().getInvocations().get("echo")[0] == "testing"
    }

    void testRuntimeVariablesPart_isLinkedHashMap_andContainsMapForEnvValue(){
        //WHEN
        scriptObject.run()
        //THEN
        assert getLastItemOfInvocationStack().getRuntimeVariables() instanceof LinkedHashMap
        assert getLastItemOfInvocationStack().getRuntimeVariables().containsKey("env")
        assert getLastItemOfInvocationStack().getRuntimeVariables().get("env") instanceof Map
        assert (getLastItemOfInvocationStack().getRuntimeVariables().get("env") as Map).keySet().size() == 0
    }

}
