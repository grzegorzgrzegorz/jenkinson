package com.passfailerror.assertion


import static com.passfailerror.Jenkinson.jenkinson

class Assertion {

    static DeclarativeAssertion stage(String stageName) {
        assert stageName != null, "contract violation"
        return new DeclarativeAssertion(jenkinson.getResultStackValidator()).stage(stageName)
    }

    static GeneralAssertion step(String stepName) {
        assert stepName != null, "contract violation"
        return new GeneralAssertion(jenkinson.getResultStackValidator()).step(stepName)
    }

    static GeneralAssertion step(String stepName, String parameter) {
        assert stepName != null && parameter != null, "contract violation"
        return new GeneralAssertion(jenkinson.getResultStackValidator()).step(stepName, parameter)
    }

}
