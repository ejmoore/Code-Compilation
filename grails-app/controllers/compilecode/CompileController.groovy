package compilecode

import grails.converters.JSON
import groovy.json.internal.LazyMap
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovy.json.JsonSlurper
import org.grails.web.json.JSONObject

class CompileController {

    def index() {
        [files:FileRevision.findAll()]
    }

    def getToken() {
        def http = new HTTPBuilder('https://mtu.instructure.com/api/v1/users/self')

        LazyMap data

        http.request(Method.GET, ContentType.JSON) {
            headers.'Authorization' = "Bearer 8~GtH3u9fwWqgVHACA7i84Sfsh3R4QqSalPd7usvjrj4jmY94cXGBSsNBV5MKhLMt1"
            headers.'User-Agent' = 'Mozilla/5.0'

            response.success = { resp, json ->
                //println "Got response ${resp.statusLine}"
                //println "Content-Type ${resp.headers.'Content-Type'}"
                data = json
                //println json
            }

            response.failure = { resp, json ->
                println "Oops we ran into some trouble"
                println json
            }
        }


        println data.get("id")
        println data.get("name")

        if (CanvasUser.get(data.get("id") != null))
            new CanvasUser(id: data.get("id"), name: data.get("name")).save()

        redirect action: "courseList"
    }

    def courseList() {
        LazyMap data

        //Get User Information
        def http = new HTTPBuilder('https://mtu.instructure.com/api/v1/users/self')
        http.request(Method.GET, ContentType.JSON) {
            headers.'Authorization' = "Bearer 8~GtH3u9fwWqgVHACA7i84Sfsh3R4QqSalPd7usvjrj4jmY94cXGBSsNBV5MKhLMt1"
            headers.'User-Agent' = 'Mozilla/5.0'
            response.success = { resp, json ->
                //println "Got response ${resp.statusLine}"
                //println "Content-Type ${resp.headers.'Content-Type'}"
                data = json
            }
            response.failure = { resp, json ->
                println "Oops we ran into some trouble"
                println json
            }
        }
        CanvasUser cu = CanvasUser.get(data.get("id"))
        if (cu == null)
            cu = new CanvasUser(id: data.get("id"), name: data.get("name")).save()

        ArrayList courses

        //Get List of Courses
        http = new HTTPBuilder('https://mtu.instructure.com/api/v1/courses/')
        http.request(Method.GET, ContentType.JSON) {
            headers.'Authorization' = "Bearer 8~GtH3u9fwWqgVHACA7i84Sfsh3R4QqSalPd7usvjrj4jmY94cXGBSsNBV5MKhLMt1"
            headers.'User-Agent' = 'Mozilla/5.0'
            response.success = { resp, json ->
                //println "Got response ${resp.statusLine}"
                //println "Content-Type ${resp.headers.'Content-Type'}"
                courses = json
            }
            response.failure = { resp, json ->
                println "Oops we ran into some trouble"
                println json
            }
        }

        ArrayList<String> courseNames = new ArrayList<>()
        ArrayList<String> roles = new ArrayList<>()

        for (LazyMap lm : courses) {
            ArrayList enr = lm.get(("enrollments"))
            String role
            for (LazyMap lm2 : enr) {
                if (lm2.get("type") == "ta") {
                    role = "ta"
                    break
                }
                role = lm2.get("type")
            }
            if (lm.get("name") == null) continue
            courseNames.add(lm.get("name"))
            roles.add(role)
            println lm.get("name") + " with id " + lm.get("id") + " and I'm a " + role
        }

        render view: "courseList", model:[
                courseNames : courseNames,
                roles       : roles,
                user        : cu
        ]
    }

    def fileSubmission() {
        def f = request.getFile('filename')
        byte[] fileData = f.getBytes()
        FileRevision fr = new FileRevision(userId: "ejmoore", fileData: new String(fileData), compiled: false, filename: f.getOriginalFilename()).save()

        def process = ("java -jar bin/compile.jar " + fr.getId()).execute()

        while(process.alive){
            sleep(100)
        }

        process.waitFor()

//        render "return code: ${process.exitValue()}" +
//                " \nstderr: ${process.err.text}" +
//                " \nstdout: ${process.in.text}" +
//                " \nDid it compile? " + fr.getCompiled()

        redirect action: "reviewFile", params: [id: fr.getId()]
    }

    def reviewFile(Long id) {
        println "Reviewing File With ID:" + id

        FileRevision fr = FileRevision.get(id)
        List<Diagnostic> diagnostics = Diagnostic.findAllByFile(fr)

        [
                compiled: fr.compiled,
                diagnostics: diagnostics
        ]
    }

    def reviewPastFile() {
        println "Reviewing File With ID:" + params.fileId

        FileRevision fr = FileRevisiong.get(Integer.parseInt(params.fileId))
        List<Diagnostic> diagnostics = Diagnostic.findAllByFile(fr)

        render view: "reviewFile", model:[
                compiled: fr.compiled,
                diagnostics: diagnostics
        ]
    }

    def blah() {
        println "blah blah blah"
        render "blah"
    }
}
