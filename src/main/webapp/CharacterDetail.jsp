<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="ISO-8859-1">
                    <title>Character Details</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 20px;
                        }

                        .card {
                            border: 1px solid #ddd;
                            border-radius: 5px;
                            padding: 15px;
                            margin-bottom: 20px;
                            background-color: #f9f9f9;
                        }

                        .section {
                            margin-bottom: 25px;
                        }

                        h2 {
                            color: #333;
                            border-bottom: 2px solid #ccc;
                            padding-bottom: 5px;
                        }

                        table {
                            border-collapse: collapse;
                            width: 100%;
                            margin-top: 10px;
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

                        .button {
                            display: inline-block;
                            padding: 10px 15px;
                            background-color: #4CAF50;
                            color: white;
                            text-decoration: none;
                            border-radius: 4px;
                            margin-top: 10px;
                        }

                        .button:hover {
                            background-color: #45a049;
                        }

                        .back-link {
                            margin-bottom: 20px;
                            display: block;
                        }
                    </style>
                </head>

                <body>
                    <a href="findcharacters" class="back-link">← Back to Characters List</a>

                    <div class="card">
                        <h1>
                            <c:out value="${character.firstName} ${character.lastName}" />
                        </h1>

                        <div class="section">
                            <h2>Basic Information</h2>
                            <p><strong>Race:</strong>
                                <c:out value="${character.clan.race.raceName}" />
                            </p>
                            <p><strong>Clan:</strong>
                                <c:out value="${character.clan.clanName}" />
                            </p>
                            <p><strong>Player:</strong>
                                <c:out value="${character.player.fullName}" />
                            </p>
                            <p><strong>Player Email:</strong>
                                <c:out value="${character.player.email}" />
                            </p>
                        </div>

                        <div class="section">
                            <h2>Jobs</h2>
                            <c:choose>
                                <c:when test="${empty characterJobs}">
                                    <p>This character has no jobs.</p>
                                </c:when>
                                <c:otherwise>
                                    <table>
                                        <tr>
                                            <th>Job</th>
                                            <th>Level</th>
                                            <th>Experience Points</th>
                                        </tr>
                                        <c:forEach items="${characterJobs}" var="characterJob">
                                            <tr>
                                                <td>
                                                    <c:out value="${characterJob.job.jobName}" />
                                                </td>
                                                <td>
                                                    <c:out value="${characterJob.level}" />
                                                </td>
                                                <td>
                                                    <c:out value="${characterJob.experiencePoints}" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="section">
                            <h2>Equipment</h2>
                            <c:choose>
                                <c:when test="${empty equippedItems}">
                                    <p>This character has no equipment.</p>
                                </c:when>
                                <c:otherwise>
                                    <table>
                                        <tr>
                                            <th>Slot</th>
                                            <th>Item Name</th>
                                            <th>Item Level</th>
                                            <th>Required Level</th>
                                        </tr>
                                        <c:forEach items="${equippedItems}" var="equippedItem">
                                            <tr>
                                                <td>
                                                    <c:out value="${equippedItem.slot}" />
                                                </td>
                                                <td>
                                                    <c:out value="${equippedItem.gear.item.itemName}" />
                                                </td>
                                                <td>
                                                    <c:out value="${equippedItem.gear.item.itemLevel}" />
                                                </td>
                                                <td>
                                                    <c:out value="${equippedItem.gear.requiredLevel}" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <a href="characterupdate?characterId=<c:out value=" ${character.characterID}" />"
                        class="button">Edit Character</a>
                    </div>
                </body>

                </html>