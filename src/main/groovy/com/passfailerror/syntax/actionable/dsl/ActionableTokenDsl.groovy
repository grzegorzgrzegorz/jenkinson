package com.passfailerror.syntax.actionable.dsl

import com.passfailerror.syntax.actionable.ActionableToken
import com.passfailerror.syntax.Steps
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
        jenkinson.getSteps().getActionableList().add(token)
    }

    ActionableTokenDsl parameters(paramNameList) {
        actionableToken.getTokenParamValueMap().put(item, paramNameList)
        return this
    }

    void setEmulator(emulatorClass) { //todo: setEmulator should only be available for emulateStep
        actionableToken.getActionMap().put(item, emulatorClass)
    }

    void returnValue(value) { //todo: returnValue should only be available for mockStep
        actionableToken.getActionMap().put(item, value)
     }

}
