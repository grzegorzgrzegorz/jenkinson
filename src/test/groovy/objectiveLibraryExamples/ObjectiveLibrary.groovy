package objectiveLibraryExamples

class ObjectiveLibrary implements Serializable {

    private static final long serialVersionUID

    def scriptObject // org.jenkinsci.plugins.workflow.cps.CpsScript
    def paramsMap
    def utils

    ObjectiveLibrary(scriptObject, paramsMap) {
        this.scriptObject = scriptObject
        this.paramsMap = paramsMap
        this.utils = new ObjectiveUtils(scriptObject)
    }

    def initialize() {
        checkParamsMap()
    }

    def checkParamsMap() {
        paramsMap.each { entry ->
            utils.checkNotNull(entry.key, entry.value)
        }
    }

    def run() {
        firstStage()
        secondStage()
    }

    def firstStage() {
        scriptObject.stage('First stage') {
            scriptObject.echo "I am working in first stage"
            scriptObject.echo "paramsMap p1: "+paramsMap.p1
        }
    }

    def secondStage() {
        scriptObject.stage('Second stage') {
            scriptObject.echo "I am working in second stage"
            scriptObject.echo "paramsMap p2: "+paramsMap.p2
        }
    }
}
