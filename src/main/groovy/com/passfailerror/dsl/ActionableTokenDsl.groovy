package com.passfailerror.dsl

import com.passfailerror.actionable.ActionableToken
import com.passfailerror.actionable.Steps
import groovy.transform.NullCheck

class ActionableTokenDsl {

    final def jenkinson
    final def itemClass
    final def item
    final ActionableToken actionableToken

    @NullCheck
    ActionableTokenDsl(jenkinson, itemClass, item, token) {
        this.jenkinson = jenkinson
        if (itemClass != Steps.class) {
            throw new RuntimeException("unsupported itemClass: " + itemClass.toString())
        }
        this.itemClass = itemClass
        this.item = item
        this.actionableToken = token
        jenkinson.getSteps().getEmulableTokenList().add(token)
    }

    ActionableTokenDsl parameters(paramNameList) {
        actionableToken.getTokenParamValueMap().put(item, paramNameList)
        return this
    }

    void setEmulator(emulatorClass) {
        actionableToken.getActionMap().put(item, emulatorClass)
    }

    void returnValue(value) {
        actionableToken.getActionMap().put(item, value)
     }

}
