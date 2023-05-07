package com.passfailerror.syntax

import com.passfailerror.resultStack.ResultStackProcessor
import groovy.util.logging.Slf4j

@Slf4j
class Steps implements Token {

    static ResultStackProcessor resultStackProcessor

    def defaultSteps = ["label", "echo", "sh"]
    def emulableTokenList = []

    def mockDefaults(pipelineScript) {
        defaultSteps.each {
            step ->
                def currentStep = step
                pipelineScript.metaClass."$currentStep" = { Object... params ->
                    log.info(currentStep + " " + params[0].toString())
                    resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                }
        }
    }

    def mock(pipelineScript) {
        emulableTokenList.each { emulatingToken -> mockFromMap(pipelineScript, emulatingToken) }
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
