package com.passfailerror

import com.passfailerror.resultStack.ResultStackProcessor
import com.passfailerror.syntax.Sections
import com.passfailerror.syntax.Steps
import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
public class Jenkinson {

    Script pipelineScript
    Sections sections = new Sections()
    Steps steps = new Steps()


    def put(String pipelineFileName) {
        def pipelinePath = Paths.get(this.class.getClassLoader().getResource(pipelineFileName).toURI())
        ResultStackProcessor.instance.initializeFromPath(pipelinePath)
        pipelineScript = getPipelineScript(pipelinePath)
        mockJenkins(pipelineScript)
    }

    Script getPipelineScript(Path pipelinePath) {
        def pipelineFile = pipelinePath.toFile()
        def binding = new Binding()
        binding.setProperty("env", [:])
        GroovyShell shell = new GroovyShell(binding)
        return shell.parse(pipelineFile)
    }

    def run() {
        pipelineScript.run()
    }

    def runMethod(String methodName){
        runMethod(methodName, null)
    }

    def runMethod(String methodName, Object args) {
        pipelineScript.invokeMethod(methodName, args)
    }

    def mockJenkins(pipelineScript) {
        steps.mock(pipelineScript)
        sections.mock(pipelineScript)
    }


}
