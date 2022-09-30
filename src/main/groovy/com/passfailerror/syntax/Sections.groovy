package com.passfailerror.syntax

import com.passfailerror.resultStack.ResultStackProcessor
import groovy.util.logging.Slf4j

@Singleton
@Slf4j
class Sections implements Syntax{

    def sections = ["pipeline", "agent", "stages", "stage", "steps"]

    def mock(pipelineScript) {
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
                    ResultStackProcessor.getInstance().storeInvocation(currentSection, params, pipelineScript.getBinding().getVariables())
                }

        }
    }
}
