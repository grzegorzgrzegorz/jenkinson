package com.passfailerror.syntax.actionable


interface ActionableToken {


    def tokenMapContains(map, token, command)

    def modifyCommandOutput(currentStep, actualCommand, params)

}