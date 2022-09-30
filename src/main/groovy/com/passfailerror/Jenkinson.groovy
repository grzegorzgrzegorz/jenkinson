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


    def put(String pipelineFileName) {
        def pipelinePath = Paths.get(this.class.getClassLoader().getResource(pipelineFileName).toURI())
        ResultStackProcessor.initializeFromPath(pipelinePath)
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

    def mockJenkins(pipelineScript) {
        Steps.instance.mock(pipelineScript)
        Sections.instance.mock(pipelineScript)
    }


}
