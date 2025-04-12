<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="ISO-8859-1">
                    <title>Update Character</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 20px;
                        }

                        .form-container {
                            border: 1px solid #ddd;
                            border-radius: 5px;
                            padding: 20px;
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #f9f9f9;
                        }

                        .form-group {
                            margin-bottom: 15px;
                        }

                        label {
                            display: block;
                            font-weight: bold;
                            margin-bottom: 5px;
                        }

                        input[type="text"] {
                            width: 100%;
                            padding: 8px;
                            box-sizing: border-box;
                            border: 1px solid #ddd;
                            border-radius: 4px;
                        }

                        .button-group {
                            margin-top: 20px;
                        }

                        .submit-button {
                            padding: 10px 15px;
                            background-color: #4CAF50;
                            color: white;
                            border: none;
                            border-radius: 4px;
                            cursor: pointer;
                        }

                        .submit-button:hover {
                            background-color: #45a049;
                        }

                        .cancel-button {
                            padding: 10px 15px;
                            background-color: #f44336;
                            color: white;
                            text-decoration: none;
                            border-radius: 4px;
                            margin-left: 10px;
                        }

                        .cancel-button:hover {
                            background-color: #d32f2f;
                        }

                        .back-link {
                            margin-bottom: 20px;
                            display: block;
                        }

                        .message {
                            padding: 10px;
                            margin-bottom: 20px;
                            border-radius: 5px;
                        }

                        .success {
                            background-color: #dff0d8;
                            border: 1px solid #d6e9c6;
                            color: #3c763d;
                        }

                        .error {
                            background-color: #f2dede;
                            border: 1px solid #ebccd1;
                            color: #a94442;
                        }
                    </style>
                </head>

                <body>
                    <a href="characterdetail?characterId=<c:out value=" ${character.characterID}" />"
                    class="back-link">← Back to Character Details</a>

                    <h1>Update Character</h1>

                    <c:if test="${not empty successMessage}">
                        <div class="message success">
                            <c:out value="${successMessage}" />
                        </div>
                    </c:if>
                    <c:if test="${not empty errorMessage}">
                        <div class="message error">
                            <c:out value="${errorMessage}" />
                        </div>
                    </c:if>

                    <div class="form-container">
                        <form action="characterupdate" method="post">
                            <input type="hidden" name="characterId" value="${character.characterID}">

                            <div class="form-group">
                                <label for="firstName">First Name:</label>
                                <input type="text" id="firstName" name="firstName"
                                    value="${fn:escapeXml(character.firstName)}" required>
                            </div>

                            <div class="form-group">
                                <label for="lastName">Last Name:</label>
                                <input type="text" id="lastName" name="lastName"
                                    value="${fn:escapeXml(character.lastName)}" required>
                            </div>

                            <div class="button-group">
                                <input type="submit" class="submit-button" value="Update Character">
                                <a href="characterdetail?characterId=<c:out value=" ${character.characterID}" />"
                                class="cancel-button">Cancel</a>
                            </div>
                        </form>
                    </div>
                </body>

                </html>