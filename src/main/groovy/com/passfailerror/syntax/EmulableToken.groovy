package com.passfailerror.syntax

import com.passfailerror.resultStack.ResultStackProcessor

class EmulableToken {

    static ResultStackProcessor resultStackProcessor

    Map<String,List<String>> tokenParamValueMap = [:]

    List<String> realExecutionList = []
    Map<String,Class> emulatorMap = [:]
    Map<String,String> returnValueMap = [:]

    def mock(pipelineScript) {}

    def shouldBeExecuted(token, command) {
        if (tokenMapContains(tokenParamValueMap, token, command) && token in realExecutionList) {
            return true
        }
        return false
    }

    def shouldBeEmulated(token, command) {
        if (tokenMapContains(tokenParamValueMap, token, command) && emulatorMap.containsKey(token)) {
            return true
        }
        return false
    }

    def shouldReturnValue(token, command){
        if (tokenMapContains(tokenParamValueMap, token, command) && returnValueMap.containsKey(token)) {
            return true
        }
        return false
    }

    def tokenMapContains(map, token, command){
        if (map.containsKey(token) && command.contains(map[token])) {
            return true
        }
        return false
    }

    def execute(actualCommand) {
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
}