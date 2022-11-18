package com.passfailerror.assertion

class GeneralAssertion extends Assertion {

    static GeneralAssertion step(String stepName) {
        return new GeneralAssertion(stepName, null)
    }

    static GeneralAssertion step(String stepName, String param) {
        return new GeneralAssertion(stepName, param)
    }

    String item
    String param

    GeneralAssertion(String item, String param) {
        this.item = item
        this.param = param
    }

    boolean isCalled() {
        return resultStackValidator.itemIsCalled(item, param)
    }

}
