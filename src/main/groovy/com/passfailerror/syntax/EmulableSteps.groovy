package com.passfailerror.syntax


import groovy.util.logging.Slf4j

@Slf4j
class EmulableSteps extends EmulableToken {

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
                def commandOutput
                if (params[0] instanceof Map) {
                    actualCommand = params[0]["script"]
                } else {
                    actualCommand = params[0]
                }
                if (shouldBeExecuted(currentStep, actualCommand)) {
                    commandOutput = shExecute(actualCommand)
                }
                resultStackProcessor.storeInvocation(currentStep, params, pipelineScript.getBinding().getVariables())
                return commandOutput
            }
        }
    }

    def shExecute(actualCommand) {
        def commandParams = actualCommand.split(" ")
        try {
            def stdOut = new StringBuilder()
            def stdErr = new StringBuilder()
            def proc = executeCommand(commandParams)
            proc.waitForProcessOutput(stdOut, stdErr)
            proc.waitForOrKill(5000)
            def combinedOutput = stdOut.toString() + stdErr.toString()
            if (proc.exitValue() != 0) { // command was run and decision is made basing on its exit value
                throw new RuntimeException(combinedOutput)
            }
            log.debug(combinedOutput)
            return combinedOutput
        }
        catch (Exception e) {// command was not run at all
            throw new RuntimeException(e)
        }
    }

    def executeCommand(commandParams) {
        def execPrefix = getOsBasedExecutionPrefix()
        def params = []
        params.addAll(execPrefix)
        params.addAll(commandParams)
        return new ProcessBuilder(params).start()
    }

    def getOsBasedExecutionPrefix(){
        if (System.getProperty("os.name").contains("Windows")){
            return ["cmd.exe", "/c"]
        }
        else{
            return ["bash", "-c"]
        }
    }
}
