package com.passfailerror.resultStack

@Singleton
class ResultStackValidator {

    boolean stageCallsStepWithParam(String stageName, String stepName, String param) {
        return resultStackHasStageWithStep(stageName, stepName, param)
    }

    private boolean resultStackHasStageWithStep(String stageName, String stepName, String param) {
        List<ResultStackEntry> resultStackListRelatedToStage = ResultStackProcessor.instance.getResultStackListHavingStage(stageName)
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

    boolean stageHasEnvVariable(String stageName, String variableName) {
        return resultStackHasStageWithEnvVariable(stageName, variableName)
    }

    private boolean resultStackHasStageWithEnvVariable(String stageName, String variableName) {
         List<ResultStackEntry> resultStackListRelatedToStage = ResultStackProcessor.instance.getResultStackListHavingStage(stageName)
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
