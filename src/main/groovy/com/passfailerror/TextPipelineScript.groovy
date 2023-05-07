package com.passfailerror

import groovy.transform.NullCheck

class TextPipelineScript {

    final String text

    @NullCheck
    TextPipelineScript(String text) {
        this.text = text
    }

    def get() {
        def binding = new Binding()
        binding.setProperty("env", [:])
        GroovyShell shell = new GroovyShell(binding)
        return shell.parse(text)
    }

}
