package com.passfailerror.syntax


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
                    resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                }
        }
    }
}
