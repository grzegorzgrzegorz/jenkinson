package com.passfailerror.resultStack

import com.passfailerror.Utils
import groovy.transform.NullCheck

import java.nio.file.Files
import java.nio.file.Path


class ResultStackProcessor {

    static defaultName = "Script1.groovy"
    final File pipelineFile
    final List<String> content
    final ResultStack resultStack = new ResultStack()

    static ResultStackProcessor getInstanceFromPath(Path pipelinePath) {
        return new ResultStackProcessor(pipelinePath.toFile(), Files.readAllLines(pipelinePath))
    }

    static ResultStackProcessor getInstanceFromLines(List<String> contentList) {
        return new ResultStackProcessor(new File(defaultName), contentList)
    }

    static ResultStackProcessor getInstanceFromText(String text) {
        return ResultStackProcessor.getInstanceFromLines(text.tokenize(System.lineSeparator()))
    }

    @NullCheck
    private ResultStackProcessor(File file, List<String> content) {
        this.pipelineFile = file
        this.content = content
    }

    void storeInvocation(String syntaxItem, Object[] parameters, LinkedHashMap<String, LinkedHashMap<String, String>> runtimeVariables) {
        String fileContentBasedCallStack = createStackLine()
        LinkedHashMap<String, List<Object>> syntaxItemInvocation = [:]
        syntaxItemInvocation.put(syntaxItem, parameters.toList())
        resultStack.getInvocationStack().add(new ResultStackEntry(fileContentBasedCallStack, syntaxItemInvocation, getDeepCopy(runtimeVariables)))
    }

    Map<Object, Object> getDeepCopy(runtimeVariables) {
        return runtimeVariables.collectEntries { k, v ->
            [k.getClass().newInstance(k), v.getClass().newInstance(v)]
        }
    }

    String createStackLine() {
        List<Integer> lineNumbers = getLineNumbersFromSTElements(getPipelineFileSTELements())
        List<String> stackLineAsList = getLinesFromPipelineFile(lineNumbers)
        return stackLineAsList.join(",")
    }

    List<Integer> getLineNumbersFromSTElements(pipelineStackTraceElements) {
        return pipelineStackTraceElements.collect(item -> item.getLineNumber()).sort()
    }

    List<StackTraceElement> getPipelineFileSTELements() {
        return Thread.currentThread().getStackTrace().findAll { item -> item.getFileName() == pipelineFile.getName() }
    }

    List<String> getLinesFromPipelineFile(List<Integer> lineNumbers) {
        List<String> result = []
        for (lineNumber in lineNumbers) {
            if (lineNumber < 0) {
                continue
            }
            int index = lineNumber - 1
            String line = content.get(index).replace("{", "").replace("\t", "").replace("}", "").trim()
            String prependix = ""
            result.add(prependix + line)
        }
        return result
    }

    List<ResultStackEntry> getInvocationStackHavingStepWithParam(List<ResultStackEntry> invocationStack, String stepName, String param) {
        return getInvocationStackHavingStep(invocationStack, stepName).
                findAll(resultStackEntry -> Utils.instance.mapContainsValue(resultStackEntry.invocations, param))
    }

    List<ResultStackEntry> getInvocationStackHavingStep(List<ResultStackEntry> invocationStack, String stepName) {
        return invocationStack
                .findAll(resultStackEntry -> resultStackEntry.invocations.containsKey(stepName))
    }

    List<ResultStackEntry> getInvocationStackHavingDeclarativeItem(String declarativeItem) {
        return resultStack.getInvocationStack().findAll(item -> item.fileContentBasedCallStack.contains(declarativeItem))
    }

    List<ResultStackEntry> getInvocationStackHavingEnvVariable(List<ResultStackEntry> invocationStack, String variableName) {
        return invocationStack
                .findAll(resultStackEntry -> resultStackEntry.getRuntimeVariables().get("env").containsKey(variableName))
    }

    List<ResultStackEntry> getInvocationStack() {
        return resultStack.getInvocationStack()
    }
}
