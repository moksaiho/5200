package game.model;

import java.util.Objects;

/**
 * Represents a race in the game.
 */
public class Race {
    private int raceID;
    private String raceName;

    public Race(int raceID, String raceName) {
        this.raceID = raceID;
        this.raceName = raceName;
    }

    public int getRaceID() {
        return raceID;
    }

    public void setRaceID(int raceID) {
        this.raceID = raceID;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return raceID == race.raceID && 
               Objects.equals(raceName, race.raceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceID, raceName);
    }

    @Override
    public String toString() {
        return String.format("Race(%d, \"%s\")", raceID, raceName);
    }
} 