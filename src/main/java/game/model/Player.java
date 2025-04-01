package game.model;

import java.util.Objects;

/**
 * Represents a player of the game.
 */
public class Player {
    private int playerID;
    private String fullName;
    private String email;

    public Player(int playerID, String fullName, String email) {
        this.playerID = playerID;
        this.fullName = fullName;
        this.email = email;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerID == player.playerID && 
               Objects.equals(fullName, player.fullName) && 
               Objects.equals(email, player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, fullName, email);
    }

    @Override
    public String toString() {
        return String.format("Player(%d, \"%s\", \"%s\")", 
                playerID, fullName, email);
    }
} 