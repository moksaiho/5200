package game.model;

import java.util.Objects;

/**
 * Represents which jobs can use a specific gear.
 * This is a join entity between Gear and Job.
 */
public class GearJob {
    private Gear gear;
    private Job job;

    public GearJob(Gear gear, Job job) {
        this.gear = gear;
        this.job = job;
    }

    public Gear getGear() {
        return gear;
    }

    public void setGear(Gear gear) {
        this.gear = gear;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GearJob gearJob = (GearJob) o;
        return Objects.equals(gear, gearJob.gear) && 
               Objects.equals(job, gearJob.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gear, job);
    }

    @Override
    public String toString() {
        return String.format("GearJob(Item(%d), Job(\"%s\"))", 
                gear.getItemID(), job.getJobID());
    }
} 