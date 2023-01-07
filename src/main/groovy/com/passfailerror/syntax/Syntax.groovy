package com.passfailerror.syntax

import com.passfailerror.resultStack.ResultStackProcessor

class Syntax {

    static ResultStackProcessor resultStackProcessor

    def realExecutionMap = [:]

    def mock(pipelineScript) {}

    def shouldBeExecuted(item, command) {
        if (realExecutionMap.containsKey(item) && realExecutionMap[item].contains(command)) {
            return true
        }
        return false
    }
}