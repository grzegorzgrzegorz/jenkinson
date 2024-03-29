package com.passfailerror.syntax

import com.passfailerror.resultStack.processor.ResultStackProcessor
import groovy.transform.NullCheck
import groovy.util.logging.Slf4j

@Slf4j
class Steps implements Token {

    final ResultStackProcessor resultStackProcessor

    @NullCheck
    Steps(resultStackProcessor) {
        this.resultStackProcessor = resultStackProcessor
    }

    def defaultSteps = ["label",
                        "echo",
                        "sh",
                        "logRotator",
                        "buildDiscarder",
                        "skipDefaultChackout",
                        "timestamps",
                        "timeout",
                        "disableConcurrentBuilds",
                        "office365ConnectorWebhooks",
                        "cleanWs",
                        "library",
                        "tool",
                        "modernSCM",
                        "checkout",
                        "build",
                        "usernamePassword",
                        "office365ConnectorSend",
                        "archiveArtifacts",
                        "beforeAgent",
                        "environment",
                        "cron"]

    def actionableList = []

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
        actionableList.each { emulatingToken -> mockFromMap(pipelineScript, emulatingToken) }
    }

    def mockFromMap(pipelineScript, tokenObject) {
        tokenObject.getTokenParamValueMap().each { entry ->
            def currentStep = entry.key
            pipelineScript.metaClass."$currentStep" = { Object... params ->
                if (params) {
                    log.info(currentStep + " " + params[0].toString())
                } else {
                    log.info(currentStep)
                }
                def actualCommand = ""
                def commandOutput
                if (params) {
                    if (params[0] instanceof Map) {
                        actualCommand = params[0]["script"]
                    } else {
                        actualCommand = params[0]
                    }
                }
                commandOutput = tokenObject.modifyCommandOutput(currentStep, actualCommand, params)
                resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                return commandOutput
            }
        }
    }
}
