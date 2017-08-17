<%--
  Created by IntelliJ IDEA.
  User: ejmoore
  Date: 8/3/17
  Time: 2:56 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Review File</title>
    <style>
        code {
            font-size:18px;
        }

        p {
            font-family:courier new,courier,monospace;
            font-size:20px;
        }

        p.warning {
            border: solid yellow;
        }

        p.error {
            border: solid red;
        }
    </style>
</head>

<body>
<p>
    Compiled: ${compiled}
</p>
<g:each var="diagnostic" in="${diagnostics}">
    <g:if test="${diagnostic.kind == 1}">
        <p class="warning">
            Warning
    </g:if>
    <g:if test="${diagnostic.kind == 0}">
        <p class="error">
            Error
    </g:if>
        &emsp;Line Number: ${diagnostic.lineNumber}
        &emsp;Column Number: ${diagnostic.columnNumber}
        <br>Message: ${diagnostic.message}
        <pre><code>
            ${diagnostic.code}
        </code></pre>
    </p>
    <br>
</g:each>
</body>
</html>