package com.passfailerror.assertion


import com.passfailerror.resultStack.ResultStackValidator
import groovy.transform.NullCheck

class DeclarativeAssertion {

    final ResultStackValidator resultStackValidator
    def String declarativeItem

    @NullCheck
    DeclarativeAssertion(ResultStackValidator resultStackValidator) {
        this.resultStackValidator = resultStackValidator
    }

    DeclarativeAssertion stage(String stageName) {
        declarativeItem = stageName
        return this
    }

    boolean calls(String stepName) {
        return calls(stepName, null)
    }

    boolean calls(String stepName, String param) {
        return resultStackValidator.declarativeItemCallsStepWithParam(declarativeItem, stepName, param)
    }

    boolean hasEnvVariable(String variableName) {
        return resultStackValidator.declarativeItemHasEnvVariable(declarativeItem, variableName)
    }
}
