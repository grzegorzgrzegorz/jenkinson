package com.passfailerror.assertion

import com.passfailerror.resultStack.ResultStackValidator

class DeclarativeAssertion extends Assertion{

    static DeclarativeAssertion stage(String stageName){
        return new DeclarativeAssertion(stageName)
    }

    String declarativeItem

    DeclarativeAssertion(String declarativeItem){
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
