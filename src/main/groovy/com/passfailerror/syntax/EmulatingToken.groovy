package com.passfailerror.syntax

class EmulatingToken extends EmulableToken{

    def modifyCommandOutput(currentStep, actualCommand) {
        if (tokenMapContains(tokenParamValueMap, currentStep, actualCommand) && actionMap.containsKey(currentStep)) {
            def emulator = emulatorMap[currentStep]
            return emulator.run(params)
        }
    }
}
