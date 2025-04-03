package game.dal;

import game.model.Gear;
import game.model.GearJob;
import game.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for GearJob join table: associates a Gear item with a Job that can use it.
 */
public class GearJobDao {

    /**
     * Creates a new GearJob record in the database.
     */
    public static GearJob create(Connection cxn, Gear gear, Job job) throws SQLException {
        String sql = "INSERT INTO GearJob (itemID, jobID) VALUES (?, ?)";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, gear.getItemID());
            ps.setString(2, job.getJobID());
            ps.executeUpdate();
        }
        return new GearJob(gear, job);
    }

    /**
     * Retrieves a GearJob by itemID and jobID (the primary key).
     */
    public static GearJob getByItemAndJob(Connection cxn, int itemID, String jobID) throws SQLException {
        String sql = "SELECT * FROM GearJob WHERE itemID = ? AND jobID = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, itemID);
            ps.setString(2, jobID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Gear gear = GearDao.getGearByID(cxn, itemID);
                    Job job = JobDao.getJobByID(cxn, jobID);
                    if (gear != null && job != null) {
                        return new GearJob(gear, job);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all GearJob records for a given Gear item (non-PK search).
     */
    public static List<GearJob> getByGear(Connection cxn, Gear gear) throws SQLException {
        List<GearJob> results = new ArrayList<>();
        String sql = "SELECT * FROM GearJob WHERE itemID = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, gear.getItemID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String jobID = rs.getString("jobID");
                    Job job = JobDao.getJobByID(cxn, jobID);
                    if (job != null) {
                        results.add(new GearJob(gear, job));
                    }
                }
            }
        }
        return results;
    }

    /**
     * Retrieves all GearJob records for a given Job (non-PK search).
     */
    public static List<GearJob> getByJob(Connection cxn, Job job) throws SQLException {
        List<GearJob> results = new ArrayList<>();
        String sql = "SELECT * FROM GearJob WHERE jobID = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setString(1, job.getJobID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int itemID = rs.getInt("itemID");
                    Gear gear = GearDao.getGearByID(cxn, itemID);
                    if (gear != null) {
                        results.add(new GearJob(gear, job));
                    }
                }
            }
        }
        return results;
    }

    /**
     * Example update method: modifies the jobID in the GearJob record.
     */
    public static GearJob updateJob(Connection cxn, GearJob gj, Job newJob) throws SQLException {
        String sql = "UPDATE GearJob SET jobID = ? WHERE itemID = ? AND jobID = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setString(1, newJob.getJobID());
            ps.setInt(2, gj.getGear().getItemID());
            ps.setString(3, gj.getJob().getJobID());
            int updated = ps.executeUpdate();
            if (updated == 1) {
                // Update in-memory
                gj.setJob(newJob);
                return gj;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a GearJob record from the database.
     */
    public static void delete(Connection cxn, GearJob gj) throws SQLException {
        String sql = "DELETE FROM GearJob WHERE itemID = ? AND jobID = ?";
        try (PreparedStatement ps = cxn.prepareStatement(sql)) {
            ps.setInt(1, gj.getGear().getItemID());
            ps.setString(2, gj.getJob().getJobID());
            ps.executeUpdate();
        }
    }
}
