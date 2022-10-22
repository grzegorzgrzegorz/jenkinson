package com.passfailerror.resultStack

@Singleton
class ResultStackValidator {

    boolean itemIsCalled(String item, String param){
        List<ResultStackEntry> invocationStack = ResultStack.instance.getInvocationStack()
        if (param) {
            return resultStackHasStepWithParam(invocationStack, item, param)
        } else {
            return resultStackHasStep(invocationStack, item)
        }
    }

    boolean declarativeItemCallsStepWithParam(String declarativeItem, String stepName, String param) {
        return resultStackHasStageWithStep(declarativeItem, stepName, param)
    }

    private boolean resultStackHasStageWithStep(String declarativeItem, String stepName, String param) {
        List<ResultStackEntry> resultStackListRelatedToStage = ResultStackProcessor.instance.getResultStackListHavingDeclarativeItem(declarativeItem)
        if (resultStackListRelatedToStage.size() > 0) {
            if (param) {
                return resultStackHasStepWithParam(resultStackListRelatedToStage, stepName, param)
            } else {
                return resultStackHasStep(resultStackListRelatedToStage, stepName)
            }
        }
        return false
    }

    private boolean resultStackHasStepWithParam(List<ResultStackEntry> resultStackList, String stepName, String param) {
        if (ResultStackProcessor.instance.getResultStackListHavingStepWithParam(resultStackList, stepName, param).size() > 0) {
            return true
        }
        return false
    }

    private boolean resultStackHasStep(List<ResultStackEntry> resultStackList, String stepName) {
        if (ResultStackProcessor.instance.getResultStackListHavingStep(resultStackList, stepName).size() > 0) {
            return true
        }
        return false
    }

    boolean declarativeItemHasEnvVariable(String declarativeItem, String variableName) {
        return resultStackHasDeclarativeItemWithEnvVariable(declarativeItem, variableName)
    }

    private boolean resultStackHasDeclarativeItemWithEnvVariable(String declarativeItem, String variableName) {
         List<ResultStackEntry> resultStackListRelatedToStage = ResultStackProcessor.instance.getResultStackListHavingDeclarativeItem(declarativeItem)
         if (resultStackListRelatedToStage.size() > 0) {
            return resultStackHasEnvVariable(resultStackListRelatedToStage, variableName)
        }
        return false
    }

    private boolean resultStackHasEnvVariable(List<ResultStackEntry> resultStackList, variableName) {
        if (ResultStackProcessor.instance.getResultStackListHavingEnvVariable(resultStackList, variableName).size() > 0) {
            return true
        }
        return false
    }
}
