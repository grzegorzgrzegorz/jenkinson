package com.passfailerror.syntax


interface EmulableToken {


    def tokenMapContains(map, token, command)

    def modifyCommandOutput(currentStep, actualCommand, params)

}