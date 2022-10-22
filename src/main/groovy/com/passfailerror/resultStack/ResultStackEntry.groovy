package com.passfailerror.resultStack

class ResultStackEntry {
    String fileContentBasedCallStack
    LinkedHashMap<String, List<Object>> invocations
    LinkedHashMap<String, Map<String, String>> runtimeVariables

    ResultStackEntry(String fileContentBasedCallStack, LinkedHashMap<String, List<Object>> invocations, LinkedHashMap<String, LinkedHashMap<String, String>> runtimeVariables) {
        this.fileContentBasedCallStack = fileContentBasedCallStack
        this.invocations = invocations
        this.runtimeVariables = runtimeVariables
    }
}