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

}