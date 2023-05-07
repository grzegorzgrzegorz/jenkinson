package com.passfailerror.syntax


import groovy.util.logging.Slf4j

@Slf4j
class Sections extends Token {

    def sections = ["pipeline", "agent", "stages", "stage", "steps"]

    def mock(pipelineScript){
    }

    def mockDefaults(pipelineScript) {
        sections.each {
            section ->
                def currentSection = section
                pipelineScript.metaClass."$currentSection" = { Object... params ->
                    log.info(currentSection)
                    if (params.length > 1) {
                        params[1].call() // stage("name"){closure}
                    } else {
                        params[0].call() // steps{closure}
                    }
                    resultStackProcessor.storeInvocation(currentSection, params, pipelineScript.getBinding().getVariables())
                }

        }
    }
}
