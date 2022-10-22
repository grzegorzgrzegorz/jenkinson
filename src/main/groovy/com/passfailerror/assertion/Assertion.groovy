package com.passfailerror.assertion

import com.passfailerror.resultStack.ResultStackValidator

class Assertion {

    static Assertion stage(String stageName){
        return new Assertion(stageName)
    }

    String declarativeItem

    Assertion(String declarativeItem){
        this.declarativeItem = declarativeItem
    }

    boolean calls(String stepName){
        return calls(stepName, null)
    }

    boolean calls(String stepName, String param){
        return ResultStackValidator.instance.declarativeItemCallsStepWithParam(declarativeItem, stepName, param)
    }

    boolean hasEnvVariable(String variableName){
        return ResultStackValidator.instance.declarativeItemHasEnvVariable(declarativeItem, variableName)
    }
}
