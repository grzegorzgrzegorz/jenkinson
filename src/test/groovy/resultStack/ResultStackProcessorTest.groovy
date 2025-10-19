package resultStack

import com.passfailerror.Jenkinson
import com.passfailerror.resultStack.ResultStackEntry
import groovy.test.GroovyTestCase

class ResultStackProcessorTest extends GroovyTestCase {

    ResultStackEntry getLastItemOfInvocationStack() {
        return jenkinson.getResultStackProcessor().getResultStack().getInvocationStack().last()
    }

    def jenkinson

    void setUp() {
        String scriptContent = 'echo "testing"'
        jenkinson = Jenkinson.initializeFromText(scriptContent)
        jenkinson.run()
    }

    void "test: FileContentBasedCallStack is String and contains executed line"() {
        when:
        jenkinson.run()
        then:
        assert getLastItemOfInvocationStack().getFileContentBasedCallStack() instanceof String
        assert getLastItemOfInvocationStack().getFileContentBasedCallStack().contentEquals("echo \"testing\"")
    }

    void "test: InvocationsPart is Map and contains invoked command"() {
        when:
        jenkinson.run()
        then:
        assert getLastItemOfInvocationStack().getInvocations() instanceof Map
        assert getLastItemOfInvocationStack().getInvocations().containsKey("echo")
        assert getLastItemOfInvocationStack().getInvocations().get("echo") instanceof List
        assert getLastItemOfInvocationStack().getInvocations().get("echo")[0] == "testing"
    }

    void "test: RuntimeVariablesPart is LinkedHashMap and uses Map for env value"() {
        when:
        jenkinson.run()
        then:
        assert getLastItemOfInvocationStack().getRuntimeVariables() instanceof LinkedHashMap
        assert getLastItemOfInvocationStack().getRuntimeVariables().containsKey("env")
        assert getLastItemOfInvocationStack().getRuntimeVariables().get("env") instanceof Map
        assert (getLastItemOfInvocationStack().getRuntimeVariables().get("env") as Map).keySet().size() == 0
    }

}
