package com.passfailerror

import groovy.transform.NullCheck

import java.nio.file.Path

class FilePipelineScript implements PipelineScript {

    final pipelinePath

    @NullCheck
    FilePipelineScript(Path pipelinePath) {
        this.pipelinePath = pipelinePath
    }

    Script get() {
        def pipelineFile = pipelinePath.toFile()
        def binding = new Binding()
        binding.setProperty("env", [:])
        GroovyShell shell = new GroovyShell(binding)
        return shell.parse(pipelineFile)
    }

}
