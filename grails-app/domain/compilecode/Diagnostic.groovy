package compilecode

class Diagnostic {

    long lineNumber
    long columnNumber
    String message
    FileRevision file
    int kind    //0 is error
                //1 is warning
    String code

    static constraints = {
    }
}
