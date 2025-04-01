package game.model;

import java.util.Objects;

/**
 * Represents a character in the game owned by a player.
 */
public class Character {
    private int characterID;
    private Player player;
    private Clan clan;
    private String firstName;
    private String lastName;

    public Character(int characterID, Player player, Clan clan, String firstName, String lastName) {
        this.characterID = characterID;
        this.player = player;
        this.clan = clan;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return characterID == character.characterID && 
               Objects.equals(player, character.player) && 
               Objects.equals(clan, character.clan) && 
               Objects.equals(firstName, character.firstName) && 
               Objects.equals(lastName, character.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterID, player, clan, firstName, lastName);
    }

    @Override
    public String toString() {
        return String.format("Character(%d, Player(%d), Clan(%d), \"%s\", \"%s\")", 
                characterID, player.getPlayerID(), clan.getClanID(), firstName, lastName);
    }
} 