package com.passfailerror.syntax

import com.passfailerror.resultStack.ResultStackProcessor

class ReturningValueToken implements EmulableToken{


    static ResultStackProcessor resultStackProcessor

    Map<String,List<String>> tokenParamValueMap = [:]
    Map<String,Object> actionMap = [:]

    def tokenMapContains(map, token, command){
        if (map.containsKey(token) && command.contains(map[token])) {
            return true
        }
        return false
    }

    def modifyCommandOutput(currentStep, actualCommand, params) {
        if (tokenMapContains(tokenParamValueMap, currentStep, actualCommand) && actionMap.containsKey(currentStep)) {
            return actionMap[currentStep]
        }
    }
}
