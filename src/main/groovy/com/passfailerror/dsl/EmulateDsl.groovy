package com.passfailerror.dsl


import com.passfailerror.syntax.Steps

class EmulateDsl {

    def jenkinson
    def itemClass
    def item
    def paramNameList
    Steps steps

    EmulateDsl(jenkinson, itemClass, item) {
        this.jenkinson = jenkinson
        this.steps = jenkinson.getSteps()
        if (itemClass != Steps.class) {
            throw new RuntimeException("unsupported itemClass: " + itemClass.toString())
        }
        this.itemClass = itemClass
        this.item = item
    }

    EmulateDsl parameters(paramNameList) {
        this.paramNameList = paramNameList
        steps.getTokenParamValueMap().put(item, paramNameList)
        return this
    }

    void setRealExecution() {
        steps.getExecutingToken().getActionMap().put(item, null)
        steps.getExecutingToken().getTokenParamValueMap().putAll(steps.getTokenParamValueMap())
    }

    void setEmulator(emulatorClass) {
        steps.getEmulatingToken().getActionMap().put(item, emulatorClass)
        steps.getEmulatingToken().getTokenParamValueMap().putAll(steps.getTokenParamValueMap())
    }

    void returnValue(value) {
        steps.getReturningValueToken().getActionMap().put(item, value)
        steps.getReturningValueToken().getTokenParamValueMap().putAll(steps.getTokenParamValueMap())
    }

}
