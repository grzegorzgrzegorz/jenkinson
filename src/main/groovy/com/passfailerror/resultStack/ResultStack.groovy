package com.passfailerror.resultStack

@Singleton
class ResultStack {

    List<ResultStackEntry> invocationStack = []
    String delimiter = "==="

    def getInvocationStack(){
        return invocationStack
    }

    def print(){
        invocationStack.each {item ->
            println(item.getFileContentBasedCallStack()+delimiter+item.getInvocations().toString()+delimiter+item.getRuntimeVariables().toString())}
    }

    def reset(){
        invocationStack = [];
    }
}
