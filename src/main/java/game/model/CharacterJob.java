package game.model;

import java.util.Objects;

/**
 * Represents a character's progression in a specific job.
 * This is a join entity between Character and Job.
 */
public class CharacterJob {
    private Character character;
    private Job job;
    private int level;
    private int experiencePoints;

    public CharacterJob(Character character, Job job, int level, int experiencePoints) {
        this.character = character;
        this.job = job;
        this.level = level;
        this.experiencePoints = experiencePoints;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterJob that = (CharacterJob) o;
        return level == that.level && 
               experiencePoints == that.experiencePoints && 
               Objects.equals(character, that.character) && 
               Objects.equals(job, that.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, job, level, experiencePoints);
    }

    @Override
    public String toString() {
        return String.format("CharacterJob(Character(%d), Job(\"%s\"), %d, %d)", 
                character.getCharacterID(), job.getJobID(), level, experiencePoints);
    }
} 