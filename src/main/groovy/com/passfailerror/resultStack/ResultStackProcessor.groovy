package com.passfailerror.resultStack

import com.passfailerror.Utils

import java.nio.file.Files
import java.nio.file.Path

@Singleton
class ResultStackProcessor {

    static defaultName = "Script1.groovy"
    File pipelineFile
    List<String> content

    // when running Jenkinson multiple times, ResultStack.instance is the same one and without resetting it accumulates content

    void initializeFromPath(Path pipelinePath) {
        setPipelineFile(pipelinePath.toFile())
        setContent(Files.readAllLines(pipelinePath))
        ResultStack.instance.reset()
    }

    void initializeFromContent(List<String> contentList) {
        setPipelineFile(new File(defaultName))
        setContent(contentList)
        ResultStack.instance.reset()
    }

    void storeInvocation(String syntaxItem, Object[] parameters, LinkedHashMap<String, LinkedHashMap<String, String>> runtimeVariables) {
        String fileContentBasedCallStack = createStackLine()
        LinkedHashMap<String, List<Object>> syntaxItemInvocation = [:]
        syntaxItemInvocation.put(syntaxItem, parameters.toList())
        ResultStack.instance.getInvocationStack().add(new ResultStackEntry(fileContentBasedCallStack, syntaxItemInvocation, getDeepCopy(runtimeVariables)))
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
        return ResultStack.instance.getInvocationStack().findAll(item -> item.fileContentBasedCallStack.contains(declarativeItem))
    }

    List<ResultStackEntry> getInvocationStackHavingEnvVariable(List<ResultStackEntry> invocationStack, String variableName) {
        return invocationStack
                .findAll(resultStackEntry -> resultStackEntry.getRuntimeVariables().get("env").containsKey(variableName))
    }

}
