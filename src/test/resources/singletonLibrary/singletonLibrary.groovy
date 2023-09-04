package singletonLibrary

def call(param) {
    echo "param: " + param
    innerMethod(param)
}

def innerMethod(innerParam){
    echo "innerParam: "+innerParam
}