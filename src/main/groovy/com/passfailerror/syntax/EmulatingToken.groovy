package com.passfailerror.syntax

class EmulatingToken extends EmulableToken{

    def modifyCommandOutput(currentStep, actualCommand, params) {
        if (tokenMapContains(tokenParamValueMap, currentStep, actualCommand) && actionMap.containsKey(currentStep)) {
            def emulator = actionMap[currentStep]
            return emulator.run(params)
        }
    }
}
