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
        return step(stepName, null)
    }

    GeneralAssertion step(String stepName, String parameter){
        assert stepName != null && stepName =~ /^(\w+|\d+)/, "contract violation"
        item = stepName
        param = parameter
        return this
    }

    boolean isCalled() {
        assert item != null && item =~ /^(\w+|\d+)/, "contract violation"
        return resultStackValidator.itemIsCalled(item, param)
    }

}
