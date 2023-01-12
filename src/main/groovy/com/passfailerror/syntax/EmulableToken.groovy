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
}