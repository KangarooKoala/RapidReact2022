package frc.team2412.robot.apriltag;

import edu.wpi.first.math.geometry.Pose3d;
import java.util.Objects;

/**
 * This class is a mockup of the equivalent WPILib class.
 */
public class AprilTag {
    public int ID;
    public Pose3d pose;

    public AprilTag(int ID, Pose3d pose) {
        this.ID = ID;
        this.pose = pose;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AprilTag) {
            var other = (AprilTag) obj;
            return ID == other.ID && pose.equals(other.pose);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, pose);
    }

    @Override
    public String toString() {
        return "AprilTag(ID: " + ID + ", pose: " + pose + ")";
    }
}
