package com.passfailerror.syntax

import com.passfailerror.resultStack.processor.ResultStackProcessor
import groovy.transform.NullCheck

class EmulatingToken implements EmulableToken{

    final ResultStackProcessor resultStackProcessor

    @NullCheck
    EmulatingToken(resultStackProcessor){
        this.resultStackProcessor = resultStackProcessor
    }

    Map<String,List<String>> tokenParamValueMap = [:]
    Map<String,Object> actionMap = [:]

    def modifyCommandOutput(currentStep, actualCommand, params) {
        if (tokenMapContains(tokenParamValueMap, currentStep, actualCommand) && actionMap.containsKey(currentStep)) {
            def emulator = actionMap[currentStep]
            return emulator.run(params)
        }
    }

    def tokenMapContains(map, token, command){
        if (map.containsKey(token) && command.contains(map[token])) {
            return true
        }
        return false
    }
}
