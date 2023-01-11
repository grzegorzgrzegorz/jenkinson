package com.passfailerror.dsl

import com.passfailerror.syntax.EmulableSteps

class EmulateDsl {

    def jenkinson
    def itemClass
    def item
    def paramNameList

    EmulateDsl(jenkinson, itemClass, item) {
        this.jenkinson = jenkinson
        this.itemClass = itemClass
        this.item = item
    }

    EmulateDsl parameters(paramNameList) {
        this.paramNameList = paramNameList
        return this
    }

    void setRealExecution(){
        if (itemClass == EmulableSteps.class) {
            def emulableSteps = jenkinson.getEmulableSteps()
            emulableSteps.addRealExecutions(item, paramNameList)
        } else {
            throw new RuntimeException("unsupported itemClass: " + itemClass.toString())
        }
    }

}
