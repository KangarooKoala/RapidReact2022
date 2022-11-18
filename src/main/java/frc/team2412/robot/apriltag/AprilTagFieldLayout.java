package frc.team2412.robot.apriltag;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This class is a mockup of the equivalent WPILib class, which is why the naming convention is
 * different.
 */
public class AprilTagFieldLayout {
    public enum OriginPosition {
        kBlueAllianceWallRightSide,
        kRedAllianceWallRightSide,
    }

    private final Map<Integer, AprilTag> m_apriltags = new HashMap<>();
    private FieldDimensions m_fieldDimensions;
    private Pose3d m_origin;

    // Commented out so we don't need com.fasterxml.jackson for JSON
    // /**
    // * Construct a new AprilTagFieldLayout with values imported from a JSON file.
    // *
    // * @param path Path of the JSON file to import from.
    // * @throws IOException If reading from the file fails.
    // */
    // public AprilTagFieldLayout(String path) throws IOException {
    // this(Path.of(path));
    // }

    // /**
    // * Construct a new AprilTagFieldLayout with values imported from a JSON file.
    // *
    // * @param path Path of the JSON file to import from.
    // * @throws IOException If reading from the file fails.
    // */
    // public AprilTagFieldLayout(Path path) throws IOException {
    // AprilTagFieldLayout layout =
    // new ObjectMapper().readValue(path.toFile(), AprilTagFieldLayout.class);
    // m_apriltags.putAll(layout.m_apriltags);
    // m_fieldDimensions = layout.m_fieldDimensions;
    // setOrigin(OriginPosition.kBlueAllianceWallRightSide);
    // }

    /**
     * Construct a new AprilTagFieldLayout from a list of {@link AprilTag} objects.
     *
     * @param apriltags
     *            List of {@link AprilTag}.
     * @param fieldLength
     *            Length of the field in meters.
     * @param fieldWidth
     *            Width of the field in meters.
     */
    public AprilTagFieldLayout(List<AprilTag> apriltags, double fieldLength, double fieldWidth) {
        // This is what's currently (11/17/2022) in the WPILib code, will check if this is intentional.
        this(apriltags, new FieldDimensions(fieldLength, fieldWidth));
    }

    private AprilTagFieldLayout(List<AprilTag> apriltags, FieldDimensions fieldDimensions) {
        // To ensure the underlying semantics don't change with what kind of list is passed in
        for (AprilTag tag : apriltags) {
            m_apriltags.put(tag.ID, tag);
        }
        m_fieldDimensions = fieldDimensions;
        setOrigin(OriginPosition.kBlueAllianceWallRightSide);
    }

    public List<AprilTag> getTags() {
        return new ArrayList<>(m_apriltags.values());
    }

    /**
     * Sets the origin based on a pre-known enumeration of positions. The position is calculated from
     * values in the configuration file.
     *
     * <p>
     * This changes the {@link #getTagPose(int)} method to return the correct pose for your
     * alliance.
     *
     * @param position
     *            The predefined position
     */
    public void setOrigin(OriginPosition position) {
        switch (position) {
            case kBlueAllianceWallRightSide:
                setOrigin(new Pose3d());
                break;
            case kRedAllianceWallRightSide:
                setOrigin(
                        new Pose3d(
                                new Translation3d(m_fieldDimensions.fieldWidth, m_fieldDimensions.fieldLength, 0),
                                new Rotation3d(0, 0, Math.PI)));
                break;
            default:
                throw new IllegalArgumentException("Unsupported enum value");
        }
    }

    /**
     * Sets the origin for tag pose transformation.
     *
     * <p>
     * This changes the {@link #getTagPose(int)} method to return the correct pose for your
     * alliance.
     *
     * @param origin
     *            The new origin for tag transformations
     */
    public void setOrigin(Pose3d origin) {
        m_origin = origin;
    }

    /**
     * Gets an AprilTag pose by its ID.
     *
     * @param ID
     *            The ID of the tag.
     * @return The pose corresponding to the ID passed in or an empty optional if a tag with that ID
     *         was not found.
     */
    public Optional<Pose3d> getTagPose(int ID) {
        AprilTag tag = m_apriltags.get(ID);
        if (tag == null) {
            return Optional.empty();
        }
        return Optional.of(tag.pose.relativeTo(m_origin));
    }

    // Commented out so we don't need com.fasterxml.jackson for JSON
    // /**
    // * Serializes a AprilTagFieldLayout to a JSON file.
    // *
    // * @param path The path to write to.
    // * @throws IOException If writing to the file fails.
    // */
    // public void serialize(String path) throws IOException {
    // serialize(Path.of(path));
    // }

    // /**
    // * Serializes a AprilTagFieldLayout to a JSON file.
    // *
    // * @param path The path to write to.
    // * @throws IOException If writing to the file fails.
    // */
    // public void serialize(Path path) throws IOException {
    // new ObjectMapper().writeValue(path.toFile(), this);
    // }

    // /**
    // * Deserializes a field layout from a resource within a jar file.
    // *
    // * @param resourcePath The absolute path of the resource
    // * @return The deserialized layout
    // * @throws IOException If the resource could not be loaded
    // */
    // public static AprilTagFieldLayout loadFromResource(String resourcePath) throws IOException {
    // try (InputStream stream = AprilTagFieldLayout.class.getResourceAsStream(resourcePath);
    // InputStreamReader reader = new InputStreamReader(stream)) {
    // return new ObjectMapper().readerFor(AprilTagFieldLayout.class).readValue(reader);
    // }
    // }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AprilTagFieldLayout) {
            var other = (AprilTagFieldLayout) obj;
            return m_apriltags.equals(other.m_apriltags) && m_origin.equals(other.m_origin);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_apriltags, m_origin);
    }

    private static class FieldDimensions {
        public double fieldWidth;

        // @SuppressWarnings("MemberName")
        public double fieldLength;

        FieldDimensions(double fieldWidth, double fieldLength) {
            this.fieldWidth = fieldWidth;
            this.fieldLength = fieldLength;
        }
    }
}
