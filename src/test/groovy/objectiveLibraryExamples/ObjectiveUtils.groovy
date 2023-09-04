package objectiveLibraryExamples

class ObjectiveUtils implements Serializable{

    private static final long serialVersionUID

    def scriptObject

    ObjectiveUtils(scriptObject){
        this.scriptObject = scriptObject
    }

    def checkNotNull(String param, String value){
        scriptObject.echo("check not null: "+param)
        if(value != null){
            scriptObject.echo(param + "==" + value)
        }
        else{
            scriptObject.error(param+" is null")
        }
    }

}
