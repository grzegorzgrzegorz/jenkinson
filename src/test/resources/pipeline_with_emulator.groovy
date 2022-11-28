package com.passfailerror

pipeline {
    agent { label 'test' }
    stages {
        stage('First stage') {
            steps {
                echo 'First - we do not care about this sh invocation'
                def defaultResult = sh 'mvn --version'
                echo 'defaultResult:'+defaultResult
            }
        }
        stage('Second stage') {
            steps {
                echo 'Second - we want to execute script just as in production'
                def resultBasingOnRealExecution = sh(script: 'git --version', returnStdout: true)
                echo 'resultBasingOnRealExecution:'+resultBasingOnRealExecution
            }
        }
        stage('Third stage') {
            steps {
                echo 'Third - we need to mimic computation basing on some input parameters and so we execute custom code instead of script'
                def resultBasingOnCustomCode = sh(script: "complicatedAppWhichComputesResultInProduction ${env.WORKSPACE}", returnStdout: true)
                echo 'resultBasingOnCustomCode:'+resultBasingOnCustomCode
            }
        }
        stage('Fourth stage') {
            steps {
                echo 'Fourth - we just need fixed result'
                def mockedResult = sh(script: "otherApp", returnStdout: true)
                echo 'mockedResult:'+mockedResult
            }
        }
    }
}