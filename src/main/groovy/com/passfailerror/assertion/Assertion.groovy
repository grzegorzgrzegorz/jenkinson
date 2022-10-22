package com.passfailerror.assertion

import com.passfailerror.resultStack.ResultStackValidator

class Assertion {

    static Assertion stage(String stageName){
        return new Assertion(stageName)
    }

    String stageName

    Assertion(String stageName){
        this.stageName = stageName
    }

    boolean calls(String stepName){
        return calls(stepName, null)
    }

    boolean calls(String stepName, String param){
        return ResultStackValidator.instance.stageCallsStepWithParam(stageName, stepName, param)
    }

    boolean hasEnvVariable(String variableName){
        return ResultStackValidator.instance.stageHasEnvVariable(stageName, variableName)
    }
}
