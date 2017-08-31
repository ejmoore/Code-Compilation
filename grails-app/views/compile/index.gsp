<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Upload a File</title>
</head>

<body>
<g:uploadForm action="fileSubmission">
    File: <input type="file" id="filename" name="filename"/>
    <br>
    <g:submitButton name="upload" value="GO"/>
</g:uploadForm>

<h1>Previous Submissions</h1>
<g:each in="${files}" var="file">
    <g:form>
        <p>
            ${file.filename}
            <g:field type="hidden" id ="fileId" name="fileId" value="${file.id}"/>
            <g:actionSubmit value="reviewPastFile"/>
        </p>
    </g:form>
</g:each>

<br><br><br>
<h1>TEST SECTION FOR GETTING LIST OF COURSES</h1>

<g:form action="getToken"><g:submitButton name="getToken"></g:submitButton></g:form>

</body>
</html>
