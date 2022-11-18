package resultStack

import com.passfailerror.Jenkinson
import com.passfailerror.resultStack.ResultStackEntry
import groovy.test.GroovyTestCase

class testResultStackProcessor extends GroovyTestCase {

    ResultStackEntry getLastItemOfInvocationStack() {
        return jenkinson.getResultStackProcessor().getResultStack().getInvocationStack().last()
    }

    def jenkinson

    void setUp() {
        String scriptContent = 'echo "testing"'
        jenkinson = Jenkinson.initializeFromText(scriptContent)
        jenkinson.run()
    }

    void testContentBasedCallStackPart_isString_andContainsExecutedLine() {
        //WHEN
        jenkinson.run()
        //THEN
        assert getLastItemOfInvocationStack().getFileContentBasedCallStack() instanceof String
        assert getLastItemOfInvocationStack().getFileContentBasedCallStack().contentEquals("echo \"testing\"")
    }

    void testInvocationsPart_isMap_andContainsInvokedCommand() {
        //WHEN
        jenkinson.run()
        //THEN
        assert getLastItemOfInvocationStack().getInvocations() instanceof Map
        assert getLastItemOfInvocationStack().getInvocations().containsKey("echo")
        assert getLastItemOfInvocationStack().getInvocations().get("echo") instanceof List
        assert getLastItemOfInvocationStack().getInvocations().get("echo")[0] == "testing"
    }

    void testRuntimeVariablesPart_isLinkedHashMap_andUsesMapForEnvValue() {
        //WHEN
        jenkinson.run()
        //THEN
        assert getLastItemOfInvocationStack().getRuntimeVariables() instanceof LinkedHashMap
        assert getLastItemOfInvocationStack().getRuntimeVariables().containsKey("env")
        assert getLastItemOfInvocationStack().getRuntimeVariables().get("env") instanceof Map
        assert (getLastItemOfInvocationStack().getRuntimeVariables().get("env") as Map).keySet().size() == 0
    }

}
