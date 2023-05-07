package com.passfailerror.assertion

import com.passfailerror.resultStack.ResultStackValidator
import groovy.transform.NullCheck

class DeclarativeAssertion {

    static ResultStackValidator resultStackValidator

    static DeclarativeAssertion stage(String stageName) {
        return new DeclarativeAssertion(stageName)
    }

    final String declarativeItem

    @NullCheck
    DeclarativeAssertion(String declarativeItem) {
        this.declarativeItem = declarativeItem
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
