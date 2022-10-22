package com.passfailerror.resultStack

class ResultStackEntry {
    String fileContentBasedCallStack
    Map<String, List<String>> invocations
    LinkedHashMap<String, Map<String, String>> runtimeVariables

    public ResultStackEntry(String fileContentBasedCallStack, Map<String, List<String>> invocations, LinkedHashMap<String, LinkedHashMap<String, String>> runtimeVariables) {
        this.fileContentBasedCallStack = fileContentBasedCallStack
        this.invocations = invocations
        this.runtimeVariables = runtimeVariables
    }
}