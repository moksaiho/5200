package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Gear;
import model.GearJob;
import model.Job;

public class GearJobDao {
	/**
     * Creates a GearJob record (associates a gear with a job).
     */
    public static GearJob create(Connection cxn, Gear gear, Job job) throws SQLException {
        String sql = "INSERT INTO GearJob (itemID, jobID) VALUES (?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, gear.getItemID());
            stmt.setString(2, job.getJobID());
            stmt.executeUpdate();
            return new GearJob(gear, job);
        }
    }

    /**
     * Retrieves a GearJob by item ID and job ID.
     */
    public static GearJob getByGearAndJob(Connection cxn, int itemID, String jobID) throws SQLException {
        String sql = "SELECT * FROM GearJob WHERE itemID = ? AND jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            stmt.setString(2, jobID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Gear gear = GearDao.getGearByID(cxn, itemID);
                    Job job = JobDao.getJobByID(cxn, jobID);
                    return new GearJob(gear, job);
                } else {
                    return null;
                }
            }
        }
    }

}
