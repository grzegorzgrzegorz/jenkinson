package com.passfailerror.resultStack.validator

import com.passfailerror.resultStack.ResultStackEntry
import com.passfailerror.resultStack.processor.ResultStackProcessor


class ResultStackValidator {

    final ResultStackProcessor resultStackProcessor
    final WithParameterEntityValidator withParameterEntityValidator
    final ParameterlessEntityValidator parameterlessEntityValidator
    final validatorMap = [:]

    ResultStackValidator(resultStackProcessor){
        this.resultStackProcessor = resultStackProcessor
        this.withParameterEntityValidator = new WithParameterEntityValidator(resultStackProcessor)
        this.parameterlessEntityValidator = new ParameterlessEntityValidator(resultStackProcessor)
        this.validatorMap = ["true": withParameterEntityValidator, "false": parameterlessEntityValidator]
    }

    boolean itemIsCalled(String item, String param) {
        return validatorMap[(param != null).toString()].resultStackHasStepWithParam(item, param)
    }

    boolean declarativeItemCallsStepWithParam(String declarativeItem, String stepName, String param) {
        return validatorMap[(param != null).toString()].resultStackHasDeclarativeItemWithStep(declarativeItem, stepName, param)
    }

    boolean declarativeItemHasEnvVariable(String declarativeItem, String variableName) {
        return resultStackHasDeclarativeItemWithEnvVariable(declarativeItem, variableName)
    }

    private boolean resultStackHasDeclarativeItemWithEnvVariable(String declarativeItem, String variableName) {
        List<ResultStackEntry> invocationStackRelatedToDeclarativeItem = resultStackProcessor.getInvocationStackHavingDeclarativeItem(declarativeItem)
        return invocationStackRelatedToDeclarativeItem.size() > 0 && resultStackHasEnvVariable(invocationStackRelatedToDeclarativeItem, variableName)
    }

    private boolean resultStackHasEnvVariable(List<ResultStackEntry> invocationStack, variableName) {
        return resultStackProcessor.getInvocationStackHavingEnvVariable(invocationStack, variableName).size() > 0
    }
}
