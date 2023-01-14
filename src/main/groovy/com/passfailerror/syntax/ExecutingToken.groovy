package com.passfailerror.syntax

class ExecutingToken extends EmulableToken {

    def modifyCommandOutput(currentStep, actualCommand, params) {
        if (tokenMapContains(tokenParamValueMap, currentStep, actualCommand) && actionMap.containsKey(currentStep)) {
            return execute(actualCommand)
        }
    }

}
