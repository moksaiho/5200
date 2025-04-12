<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="ISO-8859-1">
        <title>Game Database Application</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                line-height: 1.6;
            }

            .container {
                max-width: 800px;
                margin: 0 auto;
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 5px;
                background-color: #f9f9f9;
            }

            h1 {
                color: #333;
                text-align: center;
                margin-bottom: 30px;
            }

            .feature-box {
                border: 1px solid #ddd;
                border-radius: 5px;
                padding: 15px;
                margin-bottom: 20px;
                background-color: white;
            }

            .feature-box h2 {
                margin-top: 0;
                color: #4CAF50;
            }

            .btn {
                display: inline-block;
                padding: 10px 15px;
                background-color: #4CAF50;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                font-weight: bold;
            }

            .btn:hover {
                background-color: #45a049;
            }

            .centered {
                text-align: center;
                margin-top: 30px;
            }
        </style>
    </head>

    <body>
        <div class="container">
            <h1>Game Database Web Application</h1>

            <div class="feature-box">
                <h2>Character Management</h2>
                <p>Browse, search, and update character information in the game database.</p>
                <ul>
                    <li>View a list of all characters in the game</li>
                    <li>Search for characters by name (partial match supported)</li>
                    <li>View detailed information about each character</li>
                    <li>Update character information</li>
                </ul>
            </div>

            <div class="feature-box">
                <h2>Detailed Character Information</h2>
                <p>The application provides detailed information about each character, including:</p>
                <ul>
                    <li>Basic character attributes (name, race, clan)</li>
                    <li>Player information</li>
                    <li>Character jobs and levels</li>
                    <li>Equipped items</li>
                </ul>
            </div>

            <div class="centered">
                <a href="findcharacters" class="btn">Start Exploring Characters</a>
            </div>
        </div>
    </body>

    </html>