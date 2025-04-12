<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="ISO-8859-1">
                    <title>Find Characters</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 20px;
                        }

                        table {
                            border-collapse: collapse;
                            width: 100%;
                        }

                        th,
                        td {
                            border: 1px solid #ddd;
                            padding: 8px;
                            text-align: left;
                        }

                        th {
                            background-color: #f2f2f2;
                        }

                        tr:nth-child(even) {
                            background-color: #f9f9f9;
                        }

                        .form-container {
                            margin-bottom: 20px;
                            padding: 15px;
                            background-color: #f5f5f5;
                            border-radius: 5px;
                        }
                    </style>
                </head>

                <body>
                    <h1>Find Characters</h1>
                    <div class="form-container">
                        <form action="findcharacters" method="post">
                            <h2>Search for Characters by Name:</h2>
                            <p>
                                <label for="characterName">Character Name (First or Last):</label>
                                <input id="characterName" name="characterName"
                                    value="${fn:escapeXml(param.characterName)}">
                            </p>
                            <p>
                                <input type="submit" value="Search">
                                <input type="button" value="Reset" onclick="location.href='findcharacters'">
                            </p>
                        </form>
                    </div>

                    <h2>Results</h2>
                    <table>
                        <tr>
                            <th>Character Name</th>
                            <th>Race</th>
                            <th>Clan</th>
                            <th>Player</th>
                        </tr>
                        <c:forEach items="${characters}" var="character">
                            <tr>
                                <td><a href="characterdetail?characterId=<c:out value=" ${character.characterID}" />">
                                    <c:out value="${character.firstName} ${character.lastName}" /></a>
                                </td>
                                <td>
                                    <c:out value="${character.clan.race.raceName}" />
                                </td>
                                <td>
                                    <c:out value="${character.clan.clanName}" />
                                </td>
                                <td>
                                    <c:out value="${character.player.fullName}" />
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </body>

                </html>