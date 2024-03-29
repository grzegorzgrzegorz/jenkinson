# Jenkinson
This is Jenkins pipeline testing framework. It is used to run and verify pipeline behaviour outside jenkins.

## Declarative pipeline
Given is the declarative pipeline:

    pipeline {
    agent { label 'test' }
    stages {
        stage('First stage') {
            steps {
                echo 'First'
                sh 'mvn --version'
                env.TEST_GLOBAL_VAR='global_value'
            }
        }
        stage('Second stage') {
            steps {
                echo 'Second'
                sh 'java -version'
                env.SECOND_STAGE_VAR='value'
            }
        }
    }

Then it is possible to run it and make assertions:

### Reading input

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")

    //GIVEN
    String scriptContent = '''
    stage("test"){
    echo "testing"
    }
    '''
    jenkinson = Jenkinson.initializeFromText(scriptContent)

### Step is called

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
    //WHEN
    jenkinson.run()
    //THEN
    assert step("sh").isCalled()

### Step is called in specific stage

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
    //WHEN
    jenkinson.run()
    //THEN
    assert stage("First stage").calls("sh")

### Specific step is called

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
    //WHEN
    jenkinson.run()
    //THEN
    assert step("sh", "mvn").isCalled()

### Stage has environment variable

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
    //WHEN
    jenkinson.run()
    //THEN
    assert stage("First stage").hasEnvVariable("TEST_GLOBAL_VAR")

### Stage has environment variable which was set before

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
    //WHEN
    jenkinson.run()
    //THEN
    assert stage("Second stage").hasEnvVariable("TEST_GLOBAL_VAR")

## Scripted pipeline
Given is the scripted pipeline:

    node('master'){
        stage('First stage') {
            echo 'First'
            sh 'mvn --version'
            env.TEST_GLOBAL_VAR='global_value'
        }
        stage('Second stage') {
            echo 'Second'
            sh 'java --version"'
            env.SECOND_STAGE_VAR='value'
        }
    }

Then it is possible to run it and make all assertions just as for declarative pipeline.

## Advanced step handling
By default step is mocked and doesn't do any action: doesn't execute any code or return any value. However it is possible to change this behaviour for more advanced assertions.

### Execute step
    pipeline {
    agent { label 'test' }
        stages{
            stage('Second stage') {
                steps {
                    echo 'Second - we want to execute script just as in production'
                    def resultBasingOnRealExecution = sh(script: 'git --version', returnStdout: true)
                    echo 'resultBasingOnRealExecution:'+resultBasingOnRealExecution
                }
            }
        }
    }

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
    jenkinson.executeStep("sh").parameters(["git --version"])
    //WHEN
    jenkinson.run()
    //THEN
    assert stage('Second stage').calls("echo", "resultBasingOnRealExecution:git version")

### Execute step using custom code

    pipeline {
    agent { label 'test' }
        stages{
            stage('Third stage') {
                steps {
                    echo 'Third - we need to mimic computation basing on some input parameters and so we execute custom code instead of script'
                    def resultBasingOnCustomCode = sh(script: "complicatedAppWhichComputesResultInProduction inputData", returnStdout: true)
                    echo 'resultBasingOnCustomCode:'+resultBasingOnCustomCode
                    def resultBasingOnCustomCode2 = parameterlessCustomStep()
                    echo 'resultBasingOnCustomCode2:'+resultBasingOnCustomCode2
                }
            }
        }
    }

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
    def closure = { parameters -> return parameters[0].script}
    jenkinson.mockStep("sh").parameters(["complicatedAppWhichComputesResultInProduction"]).returnValue(closure)
    //WHEN
    jenkinson.run()
    //THEN
    assert stage('Third stage').calls("echo", "resultBasingOnCustomCode:complicatedAppWhichComputesResultInProduction inputData")

### Return value from a step

    pipeline {
    agent { label 'test' }
        stages{
            stage('Fourth stage') {
                steps {
                    echo 'Fourth - we just need fixed result'
                    def mockedResult = sh(script: "otherApp", returnStdout: true)
                    echo 'mockedResult:'+mockedResult
                }
            }
        }
    }

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("simplePipeline.groovy")
    jenkinson.mockStep("sh").parameters(["otherApp"]).returnValue("mocked result")
    //WHEN
    jenkinson.run()
    //THEN
    assert stage('Fourth stage').calls("echo", "mockedResult:mocked result")

## Singleton libraries
Most often singleton libraries are used in Jenkins as so called shared libraries. These can also be tested.
Example of singleton library:

    def call(param) {
    echo "param: " + param
    innerMethod(param)
    }
    
    def innerMethod(innerParam){
    echo "innerParam: "+innerParam
    }

### Step is called

    //GIVEN
    jenkinson = Jenkinson.initializeFromFile("singletonLibrary.groovy")
    //WHEN
    jenkinson.runMethod("call")
    //THEN
    assert step("echo", "param:").isCalled()

## Objective libraries
Much more interesting is the possibility of using objective libraries in Jenkins. 

ObjectiveLibrary.groovy:

    package objectiveLibrary
    
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

It can be tested as well:

### Step is called
    
    //GIVEN
    jenkinson = Jenkinson.initialize()
    paramsMap = ["p1": "p1_value", "p2": "p2_value"]
    objectiveLibrary = new ObjectiveLibrary(jenkinson.getPipelineScript(), paramsMap)
    objectiveLibrary.initialize()
    //WHEN
    objectiveLibrary.run()
    //THEN
    assert step("echo", "I am working in first stage").isCalled()

## More info
All the acceptance tests are serving also documentation purpose. One can browse src/test/groovy/acceptance tests for usage examples.
