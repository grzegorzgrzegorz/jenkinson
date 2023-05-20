package com.passfailerror.resultStack

class WithParameterEntityValidator {

    final ResultStackProcessor resultStackProcessor
    final List<ResultStackEntry> invocationStack

    WithParameterEntityValidator(ResultStackProcessor resultStackProcessor){
        this.resultStackProcessor = resultStackProcessor
        this.invocationStack = resultStackProcessor.getInvocationStack()
    }

    boolean resultStackHasStepWithParam(String stepName, String param) {
        return resultStackProcessor.getInvocationStackHavingStepWithParam(invocationStack, stepName, param).size() > 0
    }

    boolean resultStackHasStepWithParam(List<ResultStackEntry> invocationStack, String stepName, String param) {
        return resultStackProcessor.getInvocationStackHavingStepWithParam(invocationStack, stepName, param).size() > 0
    }

    boolean resultStackHasDeclarativeItemWithStep(String declarativeItem, String stepName, String param) {
        List<ResultStackEntry> invocationStackRelatedToDeclarativeItem = resultStackProcessor.getInvocationStackHavingDeclarativeItem(declarativeItem)
        return invocationStackRelatedToDeclarativeItem.size() > 0 && resultStackHasStepWithParam(invocationStackRelatedToDeclarativeItem, stepName, param)
    }
}
