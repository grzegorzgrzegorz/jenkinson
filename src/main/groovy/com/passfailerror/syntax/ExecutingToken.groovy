package com.passfailerror.syntax

class ExecutingToken extends EmulableToken {

    Map<String, String> realExecutionMap = [:]

    def modifyCommandOutput(currentStep, actualCommand) {
        if (tokenMapContains(tokenParamValueMap, currentStep, actualCommand) && actionMap.containsKey(currentStep)) {
            return execute(actualCommand)
        }
    }

}
