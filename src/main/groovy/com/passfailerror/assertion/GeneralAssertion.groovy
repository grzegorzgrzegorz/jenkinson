package com.passfailerror.assertion

import com.passfailerror.Jenkinson
import com.passfailerror.resultStack.ResultStackValidator
import groovy.transform.NullCheck

class GeneralAssertion {

    final ResultStackValidator resultStackValidator

    def String item
    def String param

    @NullCheck
    GeneralAssertion(ResultStackValidator resultStackValidator) {
        this.resultStackValidator = resultStackValidator
    }

    GeneralAssertion step(String stepName){
        item = stepName
        return this
    }

    GeneralAssertion step(String stepName, String parameter){
        item = stepName
        param = parameter
        return this
    }

    boolean isCalled() {
        return resultStackValidator.itemIsCalled(item, param)
    }

}
