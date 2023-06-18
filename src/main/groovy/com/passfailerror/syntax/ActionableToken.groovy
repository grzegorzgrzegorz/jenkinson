package com.passfailerror.syntax


interface ActionableToken {


    def tokenMapContains(map, token, command)

    def modifyCommandOutput(currentStep, actualCommand, params)

}