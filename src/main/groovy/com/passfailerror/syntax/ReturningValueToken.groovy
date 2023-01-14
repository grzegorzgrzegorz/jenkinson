package com.passfailerror.syntax

class ReturningValueToken extends EmulableToken{

    def modifyCommandOutput(currentStep, actualCommand, params) {
        if (tokenMapContains(tokenParamValueMap, currentStep, actualCommand) && actionMap.containsKey(currentStep)) {
            return actionMap[currentStep]
        }
    }
}
