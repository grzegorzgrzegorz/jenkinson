package acceptance

import com.passfailerror.Jenkinson
import groovy.test.GroovyTestCase
import objectiveLibrary.ObjectiveLibrary

import static com.passfailerror.assertion.GeneralAssertion.step

class testObjectiveLibrary extends GroovyTestCase {

    Jenkinson jenkinson

    void setUp() {
        if (jenkinson == null) {
            jenkinson = Jenkinson.initialize()
        }
    }

    void test_objectiveLibraryAssertion_returnsTrue_forExistingInvocation() {
        //GIVEN
        def paramsMap = ["p1": "p1_value", "p2": "p2_value"]
        ObjectiveLibrary objectiveLibrary = new ObjectiveLibrary(jenkinson.getPipelineScript(), paramsMap)
        //WHEN
        objectiveLibrary.initialize()
        objectiveLibrary.run()
        //THEN
        assert step("echo","I am working in first stage").isCalled()
    }

    void test_objectiveLibraryAssertion_returnsFalse_forUnexistingValueInvocation() {
        //GIVEN
        def paramsMap = ["p1": "p1_value", "p2": "p2_value"]
        ObjectiveLibrary objectiveLibrary = new ObjectiveLibrary(jenkinson.getPipelineScript(), paramsMap)
        //WHEN
        objectiveLibrary.initialize()
        objectiveLibrary.run()
        //THEN
        assert step("echo","Unexisting text").isCalled() == false
    }

    void test_objectiveLibraryAssertion_returnsFalse_forUnexistingStepInvocation() {
        //GIVEN
        def paramsMap = ["p1": "p1_value", "p2": "p2_value"]
        ObjectiveLibrary objectiveLibrary = new ObjectiveLibrary(jenkinson.getPipelineScript(), paramsMap)
        //WHEN
        objectiveLibrary.initialize()
        objectiveLibrary.run()
        //THEN
        assert step("unexisting step","I am working in first stage").isCalled() == false
    }

    void test_objectiveLibraryAssertion_returnsFalse_forStepWhichExistsInNotExecutedPartOfLibrary() {
        //GIVEN
        def paramsMap = ["p1": "p1_value", "p2": "p2_value"]
        ObjectiveLibrary objectiveLibrary = new ObjectiveLibrary(jenkinson.getPipelineScript(), paramsMap)
        //WHEN
        objectiveLibrary.secondStage()
        //THEN
        assert step("echo","I am working in first stage").isCalled() == false
    }
}
