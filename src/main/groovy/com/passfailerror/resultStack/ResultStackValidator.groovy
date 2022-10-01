package com.passfailerror.resultStack

@Singleton
class ResultStackValidator {

    boolean stageCallsStep(String stageName, String stepName){
        return resultStackHasStageWithStep(stageName, stepName)
    }

    private boolean resultStackHasStageWithStep(String stageName, String stepName){
        def result = ResultStack.instance.getInvocationStack().findAll(item->item.fileContentBasedCallStack.contains(stageName))
        if(result.size()>0){
            return resultStackHasStep(result, stepName)
        }
        return false
    }

    private boolean resultStackHasStep(List<ResultStack> resultStackList, stepName){
        return resultStackList
                .findAll(resultStackEntry -> resultStackEntry.invocations.containsKey(stepName))
    }

    boolean stageHasEnvVariable(String stageName, String variableName){
        return resultStackHasStageWithEnvVariable(stageName, variableName)
    }

    private boolean resultStackHasStageWithEnvVariable(String stageName, String variableName){
        def result = ResultStack.instance.getInvocationStack().findAll(item->item.fileContentBasedCallStack.contains(stageName))
        if(result.size()>0) {
            return resultStackHasEnvVariable(result, variableName)
        }
        return false
    }

    private boolean resultStackHasEnvVariable(List<ResultStack> resultStackList, variableName){
        def resultList = resultStackList
                .findAll(resultStackEntry -> resultStackEntry.getRuntimeVariables().get("env").containsKey(variableName))
        if (resultList.size() > 0){
            return true
        }
        return false
    }
}
