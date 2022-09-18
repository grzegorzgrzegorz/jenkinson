package com.passfailerror

class ResultStack {

    List<ResultStackEntry> invocationStack = []
    String delimiter = "==="

    def getInvocationStack(){
        return invocationStack
    }

    def print(){
        invocationStack.each {item ->
            println(item.getStackLine()+delimiter+item.getInvocations().toString()+delimiter+item.getRuntimeVariables().toString())}
    }
}
