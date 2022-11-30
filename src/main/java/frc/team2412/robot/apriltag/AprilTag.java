package frc.team2412.robot.apriltag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.math.geometry.Pose3d;
import java.util.Objects;

public class AprilTag {
    @JsonProperty(value = "ID")
    public int ID;

    @JsonProperty(value = "pose")
    public Pose3d pose;

    @JsonCreator
    public AprilTag(
            @JsonProperty(required = true, value = "ID") int ID,
            @JsonProperty(required = true, value = "pose") Pose3d pose) {
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
