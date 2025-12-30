package com.passfailerror.syntax

import com.passfailerror.resultStack.processor.ResultStackProcessor
import groovy.transform.NullCheck
import groovy.util.logging.Slf4j

@Slf4j
class Sections implements Token {

    final ResultStackProcessor resultStackProcessor

    @NullCheck
    Sections(resultStackProcessor) {
        this.resultStackProcessor = resultStackProcessor
    }

    def mock(pipelineScript) {
    }
}
