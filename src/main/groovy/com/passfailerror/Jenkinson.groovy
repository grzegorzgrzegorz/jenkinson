package com.passfailerror

import com.passfailerror.assertion.Assertion
import com.passfailerror.resultStack.ResultStackProcessor
import com.passfailerror.resultStack.ResultStackValidator
import com.passfailerror.syntax.Sections
import com.passfailerror.syntax.Steps
import com.passfailerror.syntax.Syntax
import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
class Jenkinson {

    Script pipelineScript
    ResultStackProcessor resultStackProcessor
    ResultStackValidator resultStackValidator = new ResultStackValidator()
    Sections sections = new Sections()
    Steps steps = new Steps()

    def put(String pipelineFileName) {
        def pipelinePath = Paths.get(this.class.getClassLoader().getResource(pipelineFileName).toURI())
        resultStackProcessor = ResultStackProcessor.getInstanceFromPath(pipelinePath)
        resultStackValidator.setResultStackProcessor(resultStackProcessor)
        Assertion.setResultStackValidator(resultStackValidator)
        Syntax.setResultStackProcessor(resultStackProcessor)
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

    def runMethod(String methodName) {
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
