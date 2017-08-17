package compilecode

class FileRevision {

    String userId
    boolean compiled
    String fileData
    String filename

    static mapping = {
        fileData column: 'fileData', type: 'text'
    }
}
