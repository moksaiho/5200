package game.model;

import java.util.Objects;

/**
 * Represents a clan in the game.
 */
public class Clan {
    private int clanID;
    private String clanName;
    private Race race;  // Using Race object instead of just the raceID for better object orientation

    public Clan(int clanID, String clanName, Race race) {
        this.clanID = clanID;
        this.clanName = clanName;
        this.race = race;
    }

    public int getClanID() {
        return clanID;
    }

    public void setClanID(int clanID) {
        this.clanID = clanID;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clan clan = (Clan) o;
        return clanID == clan.clanID && 
               Objects.equals(clanName, clan.clanName) && 
               Objects.equals(race, clan.race);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clanID, clanName, race);
    }

    @Override
    public String toString() {
        return String.format("Clan(%d, \"%s\", Race(%d))", 
                clanID, clanName, race.getRaceID());
    }
} 