package game.model;

import java.util.Objects;

/**
 * Represents a job or class in the game.
 */
public class Job {
    private String jobID;
    private String jobName;

    public Job(String jobID, String jobName) {
        this.jobID = jobID;
        this.jobName = jobName;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(jobID, job.jobID) && 
               Objects.equals(jobName, job.jobName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobID, jobName);
    }

    @Override
    public String toString() {
        return String.format("Job(\"%s\", \"%s\")", jobID, jobName);
    }
} 