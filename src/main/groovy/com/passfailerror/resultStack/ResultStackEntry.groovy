package com.passfailerror.resultStack

class ResultStackEntry {
    def fileContentBasedCallStack
    def invocations
    def runtimeVariables

    public ResultStackEntry(String fileContentBasedCallStack, Map invocations, LinkedHashMap runtimeVariables){
        this.fileContentBasedCallStack = fileContentBasedCallStack
        this.invocations = invocations
        this.runtimeVariables = runtimeVariables
    }
}
