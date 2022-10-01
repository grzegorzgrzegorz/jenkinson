package com.passfailerror.assertion

import com.passfailerror.resultStack.ResultStackValidator

class Assertion {

    static Assertion stage(String stageName){
        return new Assertion(stageName)
    }

    String stageName;

    public Assertion(String stageName){
        this.stageName = stageName
    }

    boolean calls(String stepName){
        return ResultStackValidator.instance.stageCallsStep(stageName, stepName)
    }

    boolean hasEnvVariable(String variableName){
        return ResultStackValidator.instance.stageHasEnvVariable(stageName, variableName)
    }
}
