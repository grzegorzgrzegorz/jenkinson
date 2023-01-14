package com.passfailerror.syntax

import com.passfailerror.resultStack.ResultStackProcessor
import groovy.util.logging.Slf4j

@Slf4j
class EmulableToken {

    static ResultStackProcessor resultStackProcessor

    Map<String,List<String>> tokenParamValueMap = [:]
    Map<String,Object> actionMap = [:]

    def tokenMapContains(map, token, command){
        if (map.containsKey(token) && command.contains(map[token])) {
            return true
        }
        return false
    }

    def modifyCommandOutput(){}

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


    def executeCommand(commandParams) {
        def execPrefix = getOsBasedExecutionPrefix()
        def params = []
        params.addAll(execPrefix)
        params.addAll(commandParams)
        return new ProcessBuilder(params).start()
    }

    def getOsBasedExecutionPrefix() {
        if (System.getProperty("os.name").contains("Windows")) {
            return ["cmd.exe", "/c"]
        } else {
            return ["bash", "-c"]
        }
    }
}