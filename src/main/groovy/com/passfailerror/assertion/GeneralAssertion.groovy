package com.passfailerror.assertion

import com.passfailerror.resultStack.ResultStackValidator

class GeneralAssertion extends Assertion{

    static GeneralAssertion step(String stepName){
        return new GeneralAssertion(stepName, null)
    }

    static GeneralAssertion step(String stepName, String param){
        return new GeneralAssertion(stepName, param)
    }

    String item
    String param

    GeneralAssertion(String item, String param){
        this.item = item
        this.param = param
    }

    boolean isCalled(){
        return ResultStackValidator.instance.itemIsCalled(item, param)
    }

}
