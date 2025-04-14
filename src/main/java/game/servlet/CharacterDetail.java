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
 * Servlet implementation for displaying character details
 */
@WebServlet("/characterdetail")
public class CharacterDetail extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CharacterDetail() {
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
        System.out.println("characterId: " + characterIdStr); // log for debugging
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
            
            // Get the character's jobs
            List<CharacterJob> characterJobs = CharacterJobDao.getJobsForCharacter(connection, character);
            
            // Get the character's equipped items
            List<EquippedItem> equippedItems = EquippedItemDao.getEquippedItemsByCharacter(connection, character);
            
            // Set the attributes for the JSP
            request.setAttribute("character", character);
            request.setAttribute("characterJobs", characterJobs);
            request.setAttribute("equippedItems", equippedItems);
            
            // Forward to the JSP
            request.getRequestDispatcher("/CharacterDetail.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }
} 