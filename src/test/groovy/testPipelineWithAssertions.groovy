import com.passfailerror.assertion.Assertion
import com.passfailerror.resultStack.ResultStack
import com.passfailerror.resultStack.ResultStackProcessor
import groovy.test.GroovyTestCase

import java.nio.file.Files
import java.nio.file.Paths

class testPipelineWithAssertions extends GroovyTestCase {

    Script pipelineScript
    ResultStackProcessor stackProcessor

    def steps = ["label", "echo", "sh"]
    def sections = ["pipeline", "agent", "stages", "stage", "steps"]


    def mockJenkins(pipelineScript, mockedSteps, mockedSections) {
        mockSteps(pipelineScript, mockedSteps)
        mockSections(pipelineScript, mockedSections)
    }

    def mockSections(pipelineScript, mockedSections) {
        mockedSections.each {
            section ->
                def currentSection = section
                pipelineScript.metaClass."$currentSection" = { Object... params ->
                    log.info(currentSection)
                    if (params.length > 1) {
                        params[1].call() // stage("name"){closure}
                    }
                    else{
                        params[0].call() // steps{closure}
                    }
                    ResultStackProcessor.getInstance().storeInvocation(currentSection, params, pipelineScript.getBinding().getVariables())
                }

        }
    }


    def mockSteps(pipelineScript, mockedSteps) {
        mockedSteps.each {
            step ->
                def currentStep = step
                pipelineScript.metaClass."$currentStep" = { Object... params ->
                    log.info(currentStep + " " + params[0].toString())
                    ResultStackProcessor.getInstance().storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                }
        }
    }


    void setUp() {
        def pipelinePath = Paths.get(this.class.getResource("simplePipeline.groovy").toURI())

        def pipelineFile = pipelinePath.toFile()

        ResultStackProcessor.setPipelineFile(pipelineFile)
        ResultStackProcessor.setPipelineFileContent(Files.readAllLines(pipelinePath))
        ResultStackProcessor.setResultStack(new ResultStack())

        def binding = new Binding()
        binding.setProperty("env", [:])
        GroovyShell shell = new GroovyShell(binding)
        pipelineScript = shell.parse(pipelineFile)
        mockJenkins(pipelineScript, steps, sections)

    }

    void test_stepWasCalled() {
        pipelineScript.run()
        assert Assertion.stage("First stage").calls("sh")
    }

    void test_unexistingStepWasNotCalled() {
        pipelineScript.run()
        assert Assertion.stage("First stage").calls("unexisting") == false
    }

    void test_stageHasEnvVariable_setInside(){
        pipelineScript.run()
        assert Assertion.stage("First stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }

    void test_stageDoesntHave_unexistingEnvVariable(){
        pipelineScript.run()
        assert Assertion.stage("First stage").hasEnvVariable("unexisting") == false
    }

    void test_stageDoesntHaveEnvVariable_setInConsecutiveStep(){
        pipelineScript.run()
        assert Assertion.stage("First stage").hasEnvVariable("SECOND_STAGE_VAR") == false
    }

    void test_stageHasEnvVariable_setInPreviousStep(){
        pipelineScript.run()
        assert Assertion.stage("Second stage").hasEnvVariable("TEST_GLOBAL_VAR")
    }
}
