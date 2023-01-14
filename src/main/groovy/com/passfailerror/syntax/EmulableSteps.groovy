package com.passfailerror.syntax


import groovy.util.logging.Slf4j

@Slf4j
class EmulableSteps extends EmulableToken {

    def mock(pipelineScript) {
        mockReturnValues(pipelineScript)
        mockRealExecutions(pipelineScript)
        mockEmulators(pipelineScript)
    }

    def mockReturnValues(pipelineScript) {
        returnValueMap.each { entry ->
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
                if (shouldReturnValue(currentStep, actualCommand)) {
                    commandOutput = returnValueMap[currentStep]
                }
                resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                return commandOutput
            }
        }
    }

    def mockRealExecutions(pipelineScript) {
        realExecutionList.each { token ->
            def currentStep = token
            pipelineScript.metaClass."$currentStep" = { Object... params ->
                log.info(currentStep + " " + params[0].toString())
                def actualCommand
                def commandOutput
                if (params[0] instanceof Map) {
                    actualCommand = params[0]["script"]
                } else {
                    actualCommand = params[0]
                }
                if (shouldBeExecuted(currentStep, actualCommand)) {
                    commandOutput = execute(actualCommand)
                }
                resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                return commandOutput
            }
        }
    }

    def mockEmulators(pipelineScript) {
        emulatorMap.each { entry ->
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
                if (shouldBeEmulated(currentStep, actualCommand)) {
                    def emulator = emulatorMap[currentStep]
                    commandOutput = emulator.run(params)
                }
                resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                return commandOutput
            }
        }
    }
}
