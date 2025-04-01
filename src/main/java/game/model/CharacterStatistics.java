package game.model;

import java.util.Objects;

/**
 * Represents a character's statistics.
 * This is a join entity between Character and StatisticType.
 */
public class CharacterStatistics {
    private Character character;
    private StatisticType statisticType;
    private int statValue;

    public CharacterStatistics(Character character, StatisticType statisticType, int statValue) {
        this.character = character;
        this.statisticType = statisticType;
        this.statValue = statValue;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public StatisticType getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(StatisticType statisticType) {
        this.statisticType = statisticType;
    }

    public int getStatValue() {
        return statValue;
    }

    public void setStatValue(int statValue) {
        this.statValue = statValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterStatistics that = (CharacterStatistics) o;
        return statValue == that.statValue && 
               Objects.equals(character, that.character) && 
               Objects.equals(statisticType, that.statisticType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, statisticType, statValue);
    }

    @Override
    public String toString() {
        return String.format("CharacterStatistics(Character(%d), StatisticType(\"%s\"), %d)", 
                character.getCharacterID(), statisticType.getStatName(), statValue);
    }
} 