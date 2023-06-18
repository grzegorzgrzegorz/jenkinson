package com.passfailerror.syntax

import com.passfailerror.resultStack.processor.ResultStackProcessor
import groovy.transform.NullCheck
import groovy.util.logging.Slf4j

@Slf4j
class Sections implements Token {

    final ResultStackProcessor resultStackProcessor
    def defaultSections = ["pipeline", "agent", "stages", "stage", "steps"]

    @NullCheck
    Sections(resultStackProcessor){
        this.resultStackProcessor = resultStackProcessor
    }

    def mock(pipelineScript) {
    }

    def mockDefaults(pipelineScript) {
        defaultSections.each {
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
