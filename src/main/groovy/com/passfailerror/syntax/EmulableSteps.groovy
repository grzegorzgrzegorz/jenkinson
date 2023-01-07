package com.passfailerror.syntax


import groovy.util.logging.Slf4j

@Slf4j
class EmulableSteps extends Steps {

    def addRealExecutions(step, commandList) {
        commandList.each { command ->
            realExecutionMap.put(step, command)
        }
    }

    def mock(pipelineScript) {
        realExecutionMap.each { entry ->
            def currentStep = entry.key
            pipelineScript.metaClass."$currentStep" = { Object... params ->
                log.info(currentStep + " " + params[0].toString())
                def actualCommand
                if (params[0] instanceof Map) {
                    actualCommand = params[0]["script"]
                } else {
                    actualCommand = params[0]
                }
                if (shouldBeExecuted(currentStep, actualCommand)) {
                    throw new UnsupportedOperationException("step calling not implemented yet")
                }
                resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
            }
        }
    }
}
