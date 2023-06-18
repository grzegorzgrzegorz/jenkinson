package com.passfailerror.resultStack.validator

import com.passfailerror.resultStack.ResultStackEntry
import com.passfailerror.resultStack.processor.ResultStackProcessor

class ParameterlessEntityValidator {

    final ResultStackProcessor resultStackProcessor
    final List<ResultStackEntry> invocationStack

    ParameterlessEntityValidator(ResultStackProcessor resultStackProcessor){
        this.resultStackProcessor = resultStackProcessor
        this.invocationStack = resultStackProcessor.getInvocationStack()
    }

    boolean resultStackHasStepWithParam(String stepName, String param) {
        return resultStackProcessor.getInvocationStackHavingStep(invocationStack, stepName).size() > 0
    }

    boolean resultStackHasStepWithParam(List<ResultStackEntry> invocationStack, String stepName, String param) {
        return resultStackProcessor.getInvocationStackHavingStep(invocationStack, stepName).size() > 0
    }

    boolean resultStackHasDeclarativeItemWithStep(String declarativeItem, String stepName, String param) {
        List<ResultStackEntry> invocationStackRelatedToDeclarativeItem = resultStackProcessor.getInvocationStackHavingDeclarativeItem(declarativeItem)
        return invocationStackRelatedToDeclarativeItem.size() > 0 && resultStackHasStepWithParam(invocationStackRelatedToDeclarativeItem, stepName, null)
    }
}
