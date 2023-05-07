package com.passfailerror

import com.passfailerror.assertion.Assertion
import com.passfailerror.dsl.EmulateDsl
import com.passfailerror.resultStack.ResultStackProcessor
import com.passfailerror.resultStack.ResultStackValidator
import com.passfailerror.syntax.EmulatingToken
import com.passfailerror.syntax.ExecutingToken
import com.passfailerror.syntax.ReturningValueToken
import com.passfailerror.syntax.Sections
import com.passfailerror.syntax.Steps
import com.passfailerror.syntax.Token
import com.passfailerror.syntax.EmulableToken
import groovy.transform.NullCheck
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

    final Script pipelineScript
    final ResultStackProcessor resultStackProcessor
    final ResultStackValidator resultStackValidator = new ResultStackValidator()
    final Sections sections = new Sections()
    final Steps steps = new Steps()

    @NullCheck
    Jenkinson(String pipelineText) {
        resultStackProcessor = ResultStackProcessor.getInstanceFromText(pipelineText)
        initialize(resultStackProcessor)
        pipelineScript = getPipelineScriptFromText(pipelineText)
        mockJenkinsDefaults(pipelineScript)
    }

    @NullCheck
    Jenkinson(Path pipelinePath) {
        resultStackProcessor = ResultStackProcessor.getInstanceFromPath(pipelinePath)
        initialize(resultStackProcessor)
        pipelineScript = getPipelineScriptFromPath(pipelinePath)
        mockJenkinsDefaults(pipelineScript)
    }

    def initialize(ResultStackProcessor resultStackProcessor) {
        resultStackValidator.setResultStackProcessor(resultStackProcessor)
        Assertion.setResultStackValidator(resultStackValidator)
        Steps.setResultStackProcessor(resultStackProcessor)
        Sections.setResultStackProcessor(resultStackProcessor)
        EmulatingToken.setResultStackProcessor(resultStackProcessor)
        ExecutingToken.setResultStackProcessor(resultStackProcessor)
        ReturningValueToken.setResultStackProcessor(resultStackProcessor)
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
        mockJenkins(pipelineScript)
        pipelineScript.run()
    }

    def runMethod(String methodName) {
        runMethod(methodName, null)
    }

    def runMethod(String methodName, Object args) {
        mockJenkins(pipelineScript)
        pipelineScript.invokeMethod(methodName, args)
    }

    def mockJenkins(pipelineScript) {
        steps.mock(pipelineScript)
    }

    def mockJenkinsDefaults(pipelineScript) {
        steps.mockDefaults(pipelineScript)
        sections.mockDefaults(pipelineScript)
    }

    EmulateDsl emulateStep(item){
        return new EmulateDsl(this, Steps.class, item, new EmulatingToken())
    }

    EmulateDsl executeStep(item){
        return new EmulateDsl(this, Steps.class, item, new ExecutingToken())
    }

    EmulateDsl mockStep(item){
        return new EmulateDsl(this, Steps.class, item,new ReturningValueToken())
    }

}
