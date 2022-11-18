package com.passfailerror.assertion

class DeclarativeAssertion extends Assertion {


    static DeclarativeAssertion stage(String stageName) {
        return new DeclarativeAssertion(stageName)
    }

    String declarativeItem

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
