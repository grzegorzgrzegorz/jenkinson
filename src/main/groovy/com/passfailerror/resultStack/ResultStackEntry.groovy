package com.passfailerror.resultStack

import groovy.transform.NullCheck

class ResultStackEntry {
    final String fileContentBasedCallStack
    final LinkedHashMap<String, List<Object>> invocations
    final LinkedHashMap<String, Map<String, String>> runtimeVariables

    @NullCheck
    ResultStackEntry(String fileContentBasedCallStack, LinkedHashMap<String, List<Object>> invocations, LinkedHashMap<String, LinkedHashMap<String, String>> runtimeVariables) {
        this.fileContentBasedCallStack = fileContentBasedCallStack
        this.invocations = invocations
        this.runtimeVariables = runtimeVariables
    }
}