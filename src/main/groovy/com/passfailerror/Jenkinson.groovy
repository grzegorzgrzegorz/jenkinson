package com.passfailerror

import com.passfailerror.resultStack.ResultStack
import com.passfailerror.resultStack.ResultStackProcessor
import groovy.util.logging.Slf4j

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
public class Jenkinson {

    Script pipelineScript
    def steps = ["label", "echo", "sh"]
    def sections = ["pipeline", "agent", "stages", "stage", "steps"]


    def put(String pipelineFileName) {
        def pipelinePath = Paths.get(this.class.getClassLoader().getResource(pipelineFileName).toURI())
        setResultStackProcessor(pipelinePath)
        pipelineScript = getPipelineScript(pipelinePath)
        mockJenkins(pipelineScript, steps, sections)
    }

    Script getPipelineScript(Path pipelinePath) {
        def pipelineFile = pipelinePath.toFile()
        def binding = new Binding()
        binding.setProperty("env", [:])
        GroovyShell shell = new GroovyShell(binding)
        return shell.parse(pipelineFile)
    }

    def setResultStackProcessor(pipelinePath) {
        def pipelineFile = pipelinePath.toFile()
        ResultStackProcessor.setPipelineFile(pipelineFile)
        ResultStackProcessor.setPipelineFileContent(Files.readAllLines(pipelinePath))
        ResultStackProcessor.setResultStack(new ResultStack())
    }

    def run() {
        pipelineScript.run()
    }

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
                    } else {
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

}
