package com.passfailerror

import com.passfailerror.assertion.Assertion
import com.passfailerror.dsl.EmulateDsl
import com.passfailerror.resultStack.ResultStackProcessor
import com.passfailerror.resultStack.ResultStackValidator
import com.passfailerror.syntax.EmulableSteps
import com.passfailerror.syntax.Sections
import com.passfailerror.syntax.Steps
import com.passfailerror.syntax.Syntax
import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
class Jenkinson {

    static Jenkinson initializeFromFile(String pipelineFileName) {
        Path pipelinePath = Paths.get(Jenkinson.class.getClassLoader().getResource(pipelineFileName).toURI())
        return new Jenkinson(pipelinePath)
    }

    static Jenkinson initializeFromText(String pipelineText) {
        return new Jenkinson(pipelineText)
    }

    static Jenkinson initialize(){
        return initializeFromText('')
    }

    Script pipelineScript
    ResultStackProcessor resultStackProcessor
    ResultStackValidator resultStackValidator = new ResultStackValidator()
    Sections sections = new Sections()
    Steps steps = new Steps()
    EmulableSteps emulableSteps = new EmulableSteps()

    Jenkinson(String pipelineText) {
        resultStackProcessor = ResultStackProcessor.getInstanceFromText(pipelineText)
        initialize(resultStackProcessor)
        pipelineScript = getPipelineScriptFromText(pipelineText)
        mockJenkins(pipelineScript)
    }

    Jenkinson(Path pipelinePath) {
        resultStackProcessor = ResultStackProcessor.getInstanceFromPath(pipelinePath)
        initialize(resultStackProcessor)
        pipelineScript = getPipelineScriptFromPath(pipelinePath)
        mockJenkins(pipelineScript)
    }

    def initialize(ResultStackProcessor resultStackProcessor) {
        resultStackValidator.setResultStackProcessor(resultStackProcessor)
        Assertion.setResultStackValidator(resultStackValidator)
        Syntax.setResultStackProcessor(resultStackProcessor)
    }

    def getPipelineScriptFromText(String text) {
        def binding = new Binding()
        binding.setProperty("env", [:])
        GroovyShell shell = new GroovyShell(binding)
        return shell.parse(text)
    }

    Script getPipelineScriptFromPath(Path pipelinePath) {
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

    EmulateDsl emulateStep(item){
        return new EmulateDsl(this, EmulableSteps.class, item)
    }

}
