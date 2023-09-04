package acceptance.libraryTypes

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase
import objectiveLibraryExamples.ObjectiveLibrary

import static com.passfailerror.assertion.Assertion.step

class testObjectiveLibrary extends GroovyTestCase {

    Jenkinson jenkinson
    def paramsMap
    ObjectiveLibrary objectiveLibrary

    void setUp() {
        jenkinson = Jenkinson.initialize()
        paramsMap = ["p1": "p1_value", "p2": "p2_value"]
        objectiveLibrary = new ObjectiveLibrary(jenkinson.getPipelineScript(), paramsMap)
        objectiveLibrary.initialize()
    }

    void test_objectiveLibraryAssertion_returnsTrue_forExistingInvocation() {
        //WHEN
        objectiveLibrary.run()
        //THEN
        assert step("echo", "I am working in first stage").isCalled()
    }

    void test_objectiveLibraryAssertion_returnsFalse_forUnexistingValueInvocation() {
        //WHEN
        objectiveLibrary.run()
        //THEN
        assert step("echo", "Unexisting text").isCalled() == false
    }

    void test_objectiveLibraryAssertion_returnsFalse_forUnexistingStepInvocation() {
        //WHEN
        objectiveLibrary.run()
        //THEN
        assert step("unexisting step", "I am working in first stage").isCalled() == false
    }

    void test_objectiveLibraryAssertion_returnsFalse_forStepWhichExistsInNotExecutedPartOfLibrary() {
        //WHEN
        objectiveLibrary.secondStage()
        //THEN
        assert step("echo", "I am working in first stage").isCalled() == false
    }

}
