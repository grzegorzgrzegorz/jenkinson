package com.passfailerror.resultStack

import java.nio.file.Files
import java.nio.file.Path

class ResultStackProcessor {

    static File pipelineFile = new File("Script1.groovy")
    static List<String> content
    static ResultStack resultStack

    static initializeFromPath(Path pipelinePath){
        setPipelineFile(pipelinePath.toFile())
        initializeFromContent(Files.readAllLines(pipelinePath))
    }

    static initializeFromContent(List<String> contentList){
        setContent(contentList)
        ResultStackProcessor.setResultStack(new ResultStack())
    }

    static getInstance(){return new ResultStackProcessor()}


    void storeInvocation(syntaxItem, parameters, runtimeVariables) {
        def fileContentBasedCallStack = createStackLine()
        def syntaxItemInvocation = [:]
        syntaxItemInvocation.put(syntaxItem, parameters);
        resultStack.getInvocationStack().add(new ResultStackEntry(fileContentBasedCallStack, syntaxItemInvocation, getDeepCopy(runtimeVariables)));
    }

    def getDeepCopy(runtimeVariables){
        return runtimeVariables.collectEntries { k, v ->
            [k.getClass().newInstance(k), v.getClass().newInstance(v)]
        }
    }

    def createStackLine() {
        def lineNumbers = getLineNumbersFromSTElements(getPipelineFileSTELements())
        def stackLineAsList = getLinesFromPipelineFile(lineNumbers)
        return stackLineAsList.join(",")
    }

    def getLineNumbersFromSTElements(pipelineStackTraceElements) {
        return pipelineStackTraceElements.collect(item -> item.getLineNumber()).sort()
    }

    def getPipelineFileSTELements() {
        return Thread.currentThread().getStackTrace().findAll { item -> item.getFileName() == pipelineFile.getName(); }
    }

    def getLinesFromPipelineFile(lineNumbers) {
        def result = []
        for (lineNumber in lineNumbers) {
            if (lineNumber < 0){
                continue
            }
            def index = lineNumber - 1
            def line = content.get(index).replace("{", "").replace("\t", "").replace("}","").trim()
            def prependix = ""
            result.add(prependix + line)
        }
        return result
    }

    def getResultStackEntriesWithStackLineContainingString(String name){
        return resultStack.getInvocationStack().findAll(item -> item.fileContentBasedCallStack.contains(name))
    }

}
