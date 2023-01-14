package com.passfailerror.syntax

class ReturningValueToken extends EmulableToken{

    def modifyCommandOutput(currentStep, actualCommand) {
        if (tokenMapContains(tokenParamValueMap, currentStep, actualCommand) && actionMap.containsKey(currentStep)) {
            return returnValueMap[currentStep]
        }
    }
}
