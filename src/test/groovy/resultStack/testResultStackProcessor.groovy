package resultStack


import com.passfailerror.resultStack.ResultStackEntry
import com.passfailerror.resultStack.ResultStackProcessor
import groovy.test.GroovyTestCase

class testResultStackProcessor extends GroovyTestCase {

    ResultStackEntry getLastItemOfInvocationStack() {
        return resultStackProcessor.getResultStack().getInvocationStack().last()
    }

    ResultStackProcessor resultStackProcessor
    Script scriptObject

    void setUp() {
        String scriptContent = 'echo "testing"'
        resultStackProcessor = ResultStackProcessor.getInstanceFromContent(scriptContent.tokenize(System.lineSeparator()))
        def binding = new Binding()
        binding.setProperty("env", [:])
        GroovyShell shell = new GroovyShell(binding)
        scriptObject = shell.parse(scriptContent)
        scriptObject.metaClass.echo = { Object... params ->
            resultStackProcessor.storeInvocation("echo", params, scriptObject.getBinding().getVariables())
        }
    }

    void testContentBasedCallStackPart_isString_andContainsExecutedLine() {
        //WHEN
        scriptObject.run()
        //THEN
        assert getLastItemOfInvocationStack().getFileContentBasedCallStack() instanceof String
        assert getLastItemOfInvocationStack().getFileContentBasedCallStack().contentEquals("echo \"testing\"")
    }

    void testInvocationsPart_isMap_andContainsInvokedCommand() {
        //WHEN
        scriptObject.run()
        //THEN
        assert getLastItemOfInvocationStack().getInvocations() instanceof Map
        assert getLastItemOfInvocationStack().getInvocations().containsKey("echo")
        assert getLastItemOfInvocationStack().getInvocations().get("echo") instanceof List
        assert getLastItemOfInvocationStack().getInvocations().get("echo")[0] == "testing"
    }

    void testRuntimeVariablesPart_isLinkedHashMap_andUsesMapForEnvValue() {
        //WHEN
        scriptObject.run()
        //THEN
        assert getLastItemOfInvocationStack().getRuntimeVariables() instanceof LinkedHashMap
        assert getLastItemOfInvocationStack().getRuntimeVariables().containsKey("env")
        assert getLastItemOfInvocationStack().getRuntimeVariables().get("env") instanceof Map
        assert (getLastItemOfInvocationStack().getRuntimeVariables().get("env") as Map).keySet().size() == 0
    }

}
