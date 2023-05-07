package com.passfailerror.assertion

import groovy.transform.NullCheck

class GeneralAssertion extends Assertion {

    static GeneralAssertion step(String stepName) {
        return new GeneralAssertion(stepName)
    }

    static GeneralAssertion step(String stepName, String param) {
        return new GeneralAssertion(stepName, param)
    }

    final String item
    final String param

    @NullCheck
    GeneralAssertion(String item) {
        this.item = item
    }

    @NullCheck
    GeneralAssertion(String item, String param) {
        this.item = item
        this.param = param
    }

    boolean isCalled() {
        return resultStackValidator.itemIsCalled(item, param)
    }

}
