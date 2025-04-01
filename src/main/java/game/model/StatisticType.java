package game.model;

import java.util.Objects;

/**
 * Represents a character statistic type in the game.
 */
public class StatisticType {
    private String statName;
    private String description;

    public StatisticType(String statName, String description) {
        this.statName = statName;
        this.description = description;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticType that = (StatisticType) o;
        return Objects.equals(statName, that.statName) && 
               Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statName, description);
    }

    @Override
    public String toString() {
        return String.format("StatisticType(\"%s\", \"%s\")", 
                statName, description);
    }
} 