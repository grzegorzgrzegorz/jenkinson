package com.passfailerror.syntax

import com.passfailerror.resultStack.ResultStackProcessor
import groovy.util.logging.Slf4j

@Singleton
@Slf4j
class Steps implements Syntax{


    def steps = ["label", "echo", "sh"]

    def mock(pipelineScript) {
        steps.each {
            step ->
                def currentStep = step
                pipelineScript.metaClass."$currentStep" = { Object... params ->
                    log.info(currentStep + " " + params[0].toString())
                    ResultStackProcessor.getInstance().storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                }
        }
    }
}
