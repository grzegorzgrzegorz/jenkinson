package com.passfailerror.dsl

import com.passfailerror.syntax.EmulableSteps

class EmulateDsl {

    def jenkinson
    def itemClass
    def item
    def paramNameList
    EmulableSteps emulableSteps

    EmulateDsl(jenkinson, itemClass, item) {
        this.jenkinson = jenkinson
        this.emulableSteps = jenkinson.getEmulableSteps()
        if (itemClass != EmulableSteps.class) {
            throw new RuntimeException("unsupported itemClass: " + itemClass.toString())
        }
        this.itemClass = itemClass
        this.item = item
    }

    EmulateDsl parameters(paramNameList) {
        this.paramNameList = paramNameList
        emulableSteps.getTokenParamValueMap().put(item, paramNameList)
        return this
    }

    void setRealExecution(){
        emulableSteps.getRealExecutionList().add(item)
    }

    void setEmulator(emulatorClass){
        emulableSteps.getEmulatorMap().put(item, emulatorClass)
    }

    void returnValue(value){
        emulableSteps.getReturnValueMap().put(item, value)
    }

}
