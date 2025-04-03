package game.dal;

import game.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDao {

    /**
     * Creates a new job in the database.
     */
    public static Job create(Connection cxn, String jobID, String jobName) throws SQLException {
        String sql = "INSERT INTO Job (jobID, jobName) VALUES (?, ?)";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, jobID);
            stmt.setString(2, jobName);
            stmt.executeUpdate();
            return new Job(jobID, jobName);
        }
    }

    /**
     * Retrieves a job by ID.
     */
    public static Job getJobByID(Connection cxn, String jobID) throws SQLException {
        String sql = "SELECT * FROM Job WHERE jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, jobID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String jobName = rs.getString("jobName");
                    return new Job(jobID, jobName);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves jobs by name.
     */
    public static List<Job> getJobsByName(Connection cxn, String jobName) throws SQLException {
        String sql = "SELECT * FROM Job WHERE jobName = ?";
        List<Job> jobs = new ArrayList<>();
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, jobName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String jobID = rs.getString("jobID");
                    jobs.add(new Job(jobID, jobName));
                }
            }
        }
        return jobs;
    }

    /**
     * Updates the name of a job.
     */
    public static Job updateJobName(Connection cxn, Job job, String newName) throws SQLException {
        String sql = "UPDATE Job SET jobName = ? WHERE jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, job.getJobID());
            int updated = stmt.executeUpdate();
            if (updated == 1) {
                job.setJobName(newName);
                return job;
            } else {
                return null;
            }
        }
    }

    /**
     * Deletes a job.
     */
    public static void delete(Connection cxn, Job job) throws SQLException {
        String sql = "DELETE FROM Job WHERE jobID = ?";
        try (PreparedStatement stmt = cxn.prepareStatement(sql)) {
            stmt.setString(1, job.getJobID());
            stmt.executeUpdate();
        }
    }
}