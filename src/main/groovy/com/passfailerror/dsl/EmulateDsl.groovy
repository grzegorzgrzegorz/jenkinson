package com.passfailerror.dsl

import com.passfailerror.syntax.EmulableToken
import com.passfailerror.syntax.Steps
import groovy.transform.NullCheck

class EmulateDsl {

    final def jenkinson
    final def itemClass
    final def item
    final EmulableToken emulableToken

    @NullCheck
    EmulateDsl(jenkinson, itemClass, item, token) {
        this.jenkinson = jenkinson
        if (itemClass != Steps.class) {
            throw new RuntimeException("unsupported itemClass: " + itemClass.toString())
        }
        this.itemClass = itemClass
        this.item = item
        this.emulableToken = token
        jenkinson.getSteps().getEmulableTokenList().add(token)
    }

    EmulateDsl parameters(paramNameList) {
        emulableToken.getTokenParamValueMap().put(item, paramNameList)
        return this
    }

    void setEmulator(emulatorClass) {
        emulableToken.getActionMap().put(item, emulatorClass)
    }

    void returnValue(value) {
        emulableToken.getActionMap().put(item, value)
     }

}
