package com.passfailerror.resultStack


class ResultStackValidator {

    final ResultStackProcessor resultStackProcessor

    ResultStackValidator(resultStackProcessor){
        this.resultStackProcessor = resultStackProcessor
    }

    boolean itemIsCalled(String item, String param) {
        List<ResultStackEntry> invocationStack = resultStackProcessor.getInvocationStack()
        if (param) {
            return resultStackHasStepWithParam(invocationStack, item, param)
        } else {
            return resultStackHasStep(invocationStack, item)
        }
    }

    boolean declarativeItemCallsStepWithParam(String declarativeItem, String stepName, String param) {
        return resultStackHasDeclarativeItemWithStep(declarativeItem, stepName, param)
    }

    private boolean resultStackHasDeclarativeItemWithStep(String declarativeItem, String stepName, String param) {
        List<ResultStackEntry> invocationStackRelatedToDeclarativeItem = resultStackProcessor.getInvocationStackHavingDeclarativeItem(declarativeItem)
        if (invocationStackRelatedToDeclarativeItem.size() > 0) {
            if (param) {
                return resultStackHasStepWithParam(invocationStackRelatedToDeclarativeItem, stepName, param)
            } else {
                return resultStackHasStep(invocationStackRelatedToDeclarativeItem, stepName)
            }
        }
        return false
    }

    private boolean resultStackHasStepWithParam(List<ResultStackEntry> invocationStack, String stepName, String param) {
        if (resultStackProcessor.getInvocationStackHavingStepWithParam(invocationStack, stepName, param).size() > 0) {
            return true
        }
        return false
    }

    private boolean resultStackHasStep(List<ResultStackEntry> invocationStack, String stepName) {
        if (resultStackProcessor.getInvocationStackHavingStep(invocationStack, stepName).size() > 0) {
            return true
        }
        return false
    }

    boolean declarativeItemHasEnvVariable(String declarativeItem, String variableName) {
        return resultStackHasDeclarativeItemWithEnvVariable(declarativeItem, variableName)
    }

    private boolean resultStackHasDeclarativeItemWithEnvVariable(String declarativeItem, String variableName) {
        List<ResultStackEntry> invocationStackRelatedToDeclarativeItem = resultStackProcessor.getInvocationStackHavingDeclarativeItem(declarativeItem)
        if (invocationStackRelatedToDeclarativeItem.size() > 0) {
            return resultStackHasEnvVariable(invocationStackRelatedToDeclarativeItem, variableName)
        }
        return false
    }

    private boolean resultStackHasEnvVariable(List<ResultStackEntry> invocationStack, variableName) {
        if (resultStackProcessor.getInvocationStackHavingEnvVariable(invocationStack, variableName).size() > 0) {
            return true
        }
        return false
    }
}
