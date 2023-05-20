package com.passfailerror.assertion

import com.passfailerror.Jenkinson
import com.passfailerror.resultStack.validator.ResultStackValidator
import groovy.transform.NullCheck

class Assertion {

    static Assertion the(Jenkinson jenkinson) {
        return new Assertion(jenkinson.getResultStackValidator())
    }

    final DeclarativeAssertion declarativeAssertion
    final GeneralAssertion generalAssertion

    @NullCheck
    Assertion(ResultStackValidator resultStackValidator) {
        this.declarativeAssertion = new DeclarativeAssertion(resultStackValidator)
        this.generalAssertion = new GeneralAssertion(resultStackValidator)
    }

    DeclarativeAssertion stage(String stageName) {
        return declarativeAssertion.stage(stageName)
    }

    GeneralAssertion step(String stepName) {
        assert stepName != null, "contract violation"
        return generalAssertion.step(stepName)
    }

    GeneralAssertion step(String stepName, String parameter) {
        assert stepName != null && parameter != null, "contract violation"
        return generalAssertion.step(stepName, parameter)
    }

}
