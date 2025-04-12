package game.servlet;

import game.dal.*;
import game.model.*;
import game.model.Character;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for updating character information
 */
@WebServlet("/characterupdate")
public class CharacterUpdate extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CharacterUpdate() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get the characterId from the request
        String characterIdStr = request.getParameter("characterId");
        Integer characterId = null;
        
        try {
            characterId = Integer.parseInt(characterIdStr);
        } catch (NumberFormatException | NullPointerException e) {
            // If the ID is invalid, redirect to the character list
            response.sendRedirect("findcharacters");
            return;
        }
        
        try (Connection connection = ConnectionManager.getConnection()) {
            // Get the character details
            Character character = CharacterDao.getCharacterByID(connection, characterId);
            
            if (character == null) {
                // If the character doesn't exist, redirect to the character list
                response.sendRedirect("findcharacters");
                return;
            }
            
            // Set the attributes for the JSP
            request.setAttribute("character", character);
            
            // Forward to the JSP
            request.getRequestDispatcher("/CharacterUpdate.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get form parameters
        String characterIdStr = request.getParameter("characterId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        
        // Validate inputs
        Integer characterId = null;
        try {
            characterId = Integer.parseInt(characterIdStr);
        } catch (NumberFormatException | NullPointerException e) {
            // If the ID is invalid, redirect to the character list
            response.sendRedirect("findcharacters");
            return;
        }
        
        if (firstName == null || firstName.trim().isEmpty() || 
            lastName == null || lastName.trim().isEmpty()) {
            // Error message for missing fields
            request.setAttribute("errorMessage", "All fields are required.");
            doGet(request, response);
            return;
        }
        
        try (Connection connection = ConnectionManager.getConnection()) {
            // Get the current character
            Character character = CharacterDao.getCharacterByID(connection, characterId);
            
            if (character == null) {
                // If the character doesn't exist, redirect to the character list
                response.sendRedirect("findcharacters");
                return;
            }
            
            // Update first name if changed
            if (!firstName.equals(character.getFirstName())) {
                character = CharacterDao.updateFirstName(connection, character, firstName);
            }
            
            // Update last name if changed
            if (!lastName.equals(character.getLastName())) {
                character = CharacterDao.updateLastName(connection, character, lastName);
            }
            
            // Success message
            request.setAttribute("successMessage", "Character updated successfully!");
            request.setAttribute("character", character);
            
            // Forward back to the update page
            request.getRequestDispatcher("/CharacterUpdate.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            doGet(request, response);
        }
    }
} 