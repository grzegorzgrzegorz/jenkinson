package com.passfailerror


import com.passfailerror.assertion.DeclarativeAssertion
import com.passfailerror.assertion.GeneralAssertion
import com.passfailerror.dsl.EmulateDsl
import com.passfailerror.resultStack.ResultStackProcessor
import com.passfailerror.resultStack.ResultStackValidator
import com.passfailerror.script.FilePipelineScript
import com.passfailerror.script.TextPipelineScript
import com.passfailerror.syntax.*
import groovy.transform.NullCheck
import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.nio.file.Paths

@Slf4j
class Jenkinson {

    static Jenkinson initializeFromFile(String pipelineFileName) {
        Path pipelinePath = Paths.get(Jenkinson.class.getClassLoader().getResource(pipelineFileName).toURI())
        def resultStackProcessor = ResultStackProcessor.getInstanceFromPath(pipelinePath)
        def pipelineScript = new FilePipelineScript(pipelinePath).get()
        return new Jenkinson(pipelineScript, resultStackProcessor)
    }

    static Jenkinson initializeFromText(String pipelineText) {
        def resultStackProcessor = ResultStackProcessor.getInstanceFromText(pipelineText)
        def pipelineScript = new TextPipelineScript(pipelineText).get()
        return new Jenkinson(pipelineScript, resultStackProcessor)
    }

    static Jenkinson initialize() {
        return initializeFromText('')
    }

    final Script pipelineScript
    final ResultStackProcessor resultStackProcessor
    final ResultStackValidator resultStackValidator
    final Sections sections
    final Steps steps
    final DeclarativeAssertion declarativeAssertion
    final GeneralAssertion generalAssertion

    @NullCheck
    Jenkinson(Script pipelineScript, ResultStackProcessor resultStackProcessor) {
        this.pipelineScript = pipelineScript
        this.resultStackProcessor = resultStackProcessor
        this.resultStackValidator = new ResultStackValidator(resultStackProcessor)
        this.steps = new Steps(resultStackProcessor)
        this.sections = new Sections(resultStackProcessor)
        mockJenkinsDefaults(pipelineScript)
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

    EmulateDsl emulateStep(item) {
        return new EmulateDsl(this, Steps.class, item, new EmulatingToken(resultStackProcessor))
    }

    EmulateDsl executeStep(item) {
        return new EmulateDsl(this, Steps.class, item, new ExecutingToken(resultStackProcessor))
    }

    EmulateDsl mockStep(item) {
        return new EmulateDsl(this, Steps.class, item, new ReturningValueToken(resultStackProcessor))
    }

}
