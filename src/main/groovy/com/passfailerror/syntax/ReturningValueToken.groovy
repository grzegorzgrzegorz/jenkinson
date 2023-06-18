package com.passfailerror.syntax

import com.passfailerror.resultStack.processor.ResultStackProcessor
import groovy.transform.NullCheck

class ReturningValueToken implements ActionableToken{


    final ResultStackProcessor resultStackProcessor

    @NullCheck
    ReturningValueToken(resultStackProcessor){
        this.resultStackProcessor = resultStackProcessor
    }

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
