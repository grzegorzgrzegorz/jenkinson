package com.passfailerror.resultStack

class ResultStackEntry {
    def stackLine
    def invocations
    def runtimeVariables

    public ResultStackEntry(String stackLine, Map invocations, LinkedHashMap runtimeVariables){
        this.stackLine = stackLine
        this.invocations = invocations
        this.runtimeVariables = runtimeVariables
    }
}
