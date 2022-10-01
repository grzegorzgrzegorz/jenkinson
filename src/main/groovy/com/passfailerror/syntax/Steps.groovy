package com.passfailerror.syntax

import com.passfailerror.resultStack.ResultStackProcessor
import groovy.util.logging.Slf4j


@Slf4j
class Steps extends Syntax {


    def steps = ["label", "echo", "sh"]

    def mock(pipelineScript) {
        steps.each {
            step ->
                def currentStep = step
                pipelineScript.metaClass."$currentStep" = { Object... params ->
                    log.info(currentStep + " " + params[0].toString())
                    ResultStackProcessor.instance.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                }
        }
    }
}
