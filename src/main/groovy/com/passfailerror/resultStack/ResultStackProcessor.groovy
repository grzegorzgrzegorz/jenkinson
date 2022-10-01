package com.passfailerror.resultStack

import java.nio.file.Files
import java.nio.file.Path

@Singleton
class ResultStackProcessor {

    static defaultName = "Script1.groovy"
    File pipelineFile
    List<String> content

    // when running Jenkinson multiple times, ResultStack.instance is the same one and without resetting it accumulates content

    def initializeFromPath(Path pipelinePath) {
        setPipelineFile(pipelinePath.toFile())
        setContent(Files.readAllLines(pipelinePath))
        ResultStack.instance.reset()
    }

    def initializeFromContent(List<String> contentList) {
        setPipelineFile(new File(defaultName))
        setContent(contentList)
        ResultStack.instance.reset()
    }

    void storeInvocation(syntaxItem, parameters, runtimeVariables) {
        def fileContentBasedCallStack = createStackLine()
        def syntaxItemInvocation = [:]
        syntaxItemInvocation.put(syntaxItem, parameters);
        ResultStack.instance.getInvocationStack().add(new ResultStackEntry(fileContentBasedCallStack, syntaxItemInvocation, getDeepCopy(runtimeVariables)));
    }

    def getDeepCopy(runtimeVariables) {
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
            if (lineNumber < 0) {
                continue
            }
            def index = lineNumber - 1
            def line = content.get(index).replace("{", "").replace("\t", "").replace("}", "").trim()
            def prependix = ""
            result.add(prependix + line)
        }
        return result
    }

    def getResultStackEntriesWithStackLineContainingString(String name) {
        return ResultStack.instance.getInvocationStack().findAll(item -> item.fileContentBasedCallStack.contains(name))
    }

}
