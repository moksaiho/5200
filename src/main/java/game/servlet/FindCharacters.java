package game.servlet;

import game.dal.*;
import game.model.*;
import game.model.Character;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for finding characters
 */
@WebServlet("/findcharacters")
public class FindCharacters extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindCharacters() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Map for storing messages.
        List<Character> characters = new ArrayList<>();
        
        try (Connection connection = ConnectionManager.getConnection()) {
            // No search criteria provided, return all characters (limited to 100 for performance)
            characters = CharacterDao.getAllCharacters(connection, 100);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        
        request.setAttribute("characters", characters);
        request.getRequestDispatcher("/FindCharacters.jsp").forward(request, response);
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Character> characters = new ArrayList<>();
        
        // Retrieve and validate search parameters
        String characterName = request.getParameter("characterName");
        
        try (Connection connection = ConnectionManager.getConnection()) {
            // If characterName is provided, search by name
            if (characterName != null && !characterName.trim().isEmpty()) {
                characters = CharacterDao.getCharactersByName(connection, characterName);
            } else {
                // Otherwise, return all characters (limited to 100 for performance)
                characters = CharacterDao.getAllCharacters(connection, 100);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        
        request.setAttribute("characters", characters);
        request.getRequestDispatcher("/FindCharacters.jsp").forward(request, response);
    }
} 