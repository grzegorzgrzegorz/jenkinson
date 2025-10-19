package acceptance.libraryTypes

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase
import objectiveLibraryExamples.ObjectiveLibrary

import static com.passfailerror.assertion.Assertion.step

class objectiveLibraryTest extends GroovyTestCase {

    Jenkinson jenkinson
    def paramsMap
    ObjectiveLibrary objectiveLibrary

    void setUp() {
        jenkinson = Jenkinson.initialize()
        paramsMap = ["p1": "p1_value", "p2": "p2_value"]
        objectiveLibrary = new ObjectiveLibrary(jenkinson.getPipelineScript(), paramsMap)
        objectiveLibrary.initialize()
    }

    void "test: existing step with existing parameter is called"() {
        when:
        objectiveLibrary.run()
        then:
        assert step("echo", "I am working in first stage").isCalled()
    }

    void "test: existing step with unexisting parameter is not called"() {
        when:
        objectiveLibrary.run()
        then:
        assert step("echo", "Unexisting text").isCalled() == false
    }

    void "test: unexisting step is not called"() {
        when:
        objectiveLibrary.run()
        then:
        assert step("unexisting step", "I am working in first stage").isCalled() == false
    }

    void "test: step which exists but which was not executed is not called"() {
        when:
        objectiveLibrary.secondStage()
        then:
        assert step("echo", "I am working in first stage").isCalled() == false
    }

}
