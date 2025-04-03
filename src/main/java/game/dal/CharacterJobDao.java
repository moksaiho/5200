package game.dal;

import game.model.Character;
import game.model.Job;
import game.model.CharacterJob;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterJobDao {

    /**
     * Creates a CharacterJob record (associates a character with a job).
     */
    public static CharacterJob create(Connection cxn, Character character, Job job, int level, int experiencePoints) throws SQLException {
        String sql = "INSERT INTO CharacterJob (characterID, jobID, level, experiencePoints) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            stmt.setString(2, job.getJobID());
            stmt.setInt(3, level);
            stmt.setInt(4, experiencePoints);
            stmt.executeUpdate();
            return new CharacterJob(character, job, level, experiencePoints);
        }
    }

    /**
     * Retrieves a CharacterJob by character ID and job ID.
     */
    public static CharacterJob getByCharacterAndJob(Connection cxn, int characterID, String jobID) throws SQLException {
        String sql = "SELECT * FROM CharacterJob WHERE characterID = ? AND jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, characterID);
            stmt.setString(2, jobID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int level = rs.getInt("level");
                    int exp = rs.getInt("experiencePoints");

                    Character character = CharacterDao.getCharacterByID(cxn, characterID);
                    Job job = JobDao.getJobByID(cxn, jobID);

                    return new CharacterJob(character, job, level, exp);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Gets all jobs for a given character.
     */
    public static List<CharacterJob> getJobsForCharacter(Connection cxn, Character character) throws SQLException {
        String sql = "SELECT * FROM CharacterJob WHERE characterID = ?";
        List<CharacterJob> list = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, character.getCharacterID());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String jobID = rs.getString("jobID");
                    int level = rs.getInt("level");
                    int exp = rs.getInt("experiencePoints");

                    Job job = JobDao.getJobByID(cxn, jobID);
                    list.add(new CharacterJob(character, job, level, exp));
                }
            }
        }
        return list;
    }

    /**
     * Updates experience points for a character's job.
     */
    public static CharacterJob updateExperiencePoints(Connection cxn, CharacterJob cj, int newExp) throws SQLException {
        String sql = "UPDATE CharacterJob SET experiencePoints = ? WHERE characterID = ? AND jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, newExp);
            stmt.setInt(2, cj.getCharacter().getCharacterID());
            stmt.setString(3, cj.getJob().getJobID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                cj.setExperiencePoints(newExp);
                return cj;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a CharacterJob record.
     */
    public static void delete(Connection cxn, CharacterJob cj) throws SQLException {
        String sql = "DELETE FROM CharacterJob WHERE characterID = ? AND jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, cj.getCharacter().getCharacterID());
            stmt.setString(2, cj.getJob().getJobID());
            stmt.executeUpdate();
        }
    }
}