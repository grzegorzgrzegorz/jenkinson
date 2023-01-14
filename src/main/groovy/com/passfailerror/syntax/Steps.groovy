package com.passfailerror.syntax


import groovy.util.logging.Slf4j

@Slf4j
class Steps extends Token {

    def steps = ["label", "echo", "sh"]
    Map<String,List<String>> tokenParamValueMap = [:]

    ReturningValueToken returningValueToken = new ReturningValueToken()
    ExecutingToken executingToken = new ExecutingToken()
    EmulatingToken emulatingToken = new EmulatingToken()

    def mock(pipelineScript){
        mockFromList(pipelineScript)
        mockFromMap(pipelineScript, returningValueToken)
        mockFromMap(pipelineScript, executingToken)
        mockFromMap(pipelineScript, emulatingToken)
    }

    def mockFromList(pipelineScript) {
        steps.each {
            step ->
                def currentStep = step
                pipelineScript.metaClass."$currentStep" = { Object... params ->
                    log.info(currentStep + " " + params[0].toString())
                    resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                }
        }
    }

    def mockFromMap(pipelineScript, tokenObject) {
        tokenObject.getTokenParamValueMap().each { entry ->
            def currentStep = entry.key
            pipelineScript.metaClass."$currentStep" = { Object... params ->
                log.info(currentStep + " " + params[0].toString())
                def actualCommand
                def commandOutput
                if (params[0] instanceof Map) {
                    actualCommand = params[0]["script"]
                } else {
                    actualCommand = params[0]
                }
                commandOutput = tokenObject.modifyCommandOutput(currentStep, actualCommand, params)
                resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                return commandOutput
            }
        }
    }
}
