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
        assert stageName != null && stageName =~ /^(\w+|\d+)/, "contract violation"
        declarativeItem = stageName
        return this
    }

    boolean calls(String stepName) {
        return calls(stepName, null)
    }

    boolean calls(String stepName, String param) {
        assert stepName != null && stepName =~ /^(\w+|\d+)/, "contract violation"
        return resultStackValidator.declarativeItemCallsStepWithParam(declarativeItem, stepName, param)
    }

    boolean hasEnvVariable(String variableName) {
        assert variableName != null && variableName =~ /^(\w+|\d+)/, "contract violation"
        return resultStackValidator.declarativeItemHasEnvVariable(declarativeItem, variableName)
    }
}
