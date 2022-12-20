package frc.team2412.robot.subsystem;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team2412.robot.Hardware;
import frc.team2412.robot.apriltag.AprilTagFieldLayout;
import frc.team2412.robot.util.sendable.SendablePose3d;
import frc.team2412.robot.util.sendable.SendableTransform3d;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.io.IOException;
import java.util.Optional;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

/**
 * Subsystem for using PhotonVision on a camera.
 *
 * <p>
 * All poses are in the NWU (North-West-Up) coordinate system. Positive X is north (forward),
 * positive Y is west (left), and positive Z is up. All translations are in meters.
 *
 * <p>
 * Rotations are a bit more complicated. A +90 degree rotation around the +X axis takes the +Y axis
 * to the +Z axis, a +90 degree rotation around +Y takes +Z to +X, and a +90 degree rotation around
 * +Z takes +X to +Y. For a NWU coordinate system, this means positive rotations around the
 * positive axes are <em>clockwise</em> when viewed from the origin towards the positive axis. For
 * example, tilting your head right is a positive X rotation, tilting your head down is a positive
 * Y rotation, and turning your head left is a positive Z rotation.
 */
public class PhotonVisionSubsystem extends SubsystemBase implements Loggable {
    /**
     * Calculates the camera pose relative to the field, using the given target.
     *
     * <p>
     * Returns null if target is null or if no valid field pose could be found for the target (either
     * the target doesn't have an ID or we don't have a pose associated with the ID).
     *
     * @param target
     *            The target to use to calculate camera pose.
     * @return The calculated camera pose.
     */
    public static Pose3d getCameraPoseUsingTarget(PhotonTrackedTarget target) {
        if (target == null) {
            return null;
        }
        // If target doesn't have fiducial ID, value is -1 (which shouldn't be in the layout)
        Optional<Pose3d> tagPose = fieldLayout.getTagPose(target.getFiducialId());
        if (tagPose.isEmpty()) {
            return null;
        }
        // getBestCameraToTarget() shouldn't be null
        return tagPose.get().transformBy(target.getBestCameraToTarget().inverse());
    }

    // Because we have to handle an IOException, we can't initialize fieldLayout in the variable
    // declaration (private static final AprilTagFieldLayout fieldLayout = ...;)
    // Instead, we have to initialize it in a static initializer (the static { ... })
    private static final AprilTagFieldLayout fieldLayout;
    static {
        // This code runs when the class is initialized (same time as normal variable initializers)
        // We have to use temp so that the compiler knows we initialize fieldLayout exactly once
        // Otherwise, it thinks we might initialize it twice if we get an IOException after initializing it
        // in the try block (since then we'd run the catch block and initialize there too)
        AprilTagFieldLayout temp;
        try {
            temp = new AprilTagFieldLayout(Filesystem.getDeployDirectory().toPath().resolve("2022-rapidreact.json"));
        } catch (IOException e) {
            e.printStackTrace();
            temp = null;
        }
        fieldLayout = temp;
    }

    private PhotonCamera photonCamera;
    private PhotonPipelineResult latestResult;
    /**
     * Null if invalid, empty if no valid camera pose, otherwise contains the camera pose.
     */
    private Optional<Pose3d> cameraPose = null;

    @Log(name = "Camera Pose")
    @SuppressWarnings("unused")
    private SendablePose3d sendableCameraPose = new SendablePose3d(this::getCameraPose);

    @Log(name = "Camera To Target Transform")
    @SuppressWarnings("unused")
    private SendableTransform3d sendableCameraToTarget = new SendableTransform3d(
            () -> hasTargets() ? latestResult.getBestTarget().getBestCameraToTarget() : null);

    public PhotonVisionSubsystem() {
        NetworkTableInstance instance = NetworkTableInstance.getDefault();
        if (RobotBase.isSimulation()) {
            instance.stopServer();
            instance.startClient("localhost");
        }
        photonCamera = new PhotonCamera(Hardware.PHOTON_CAM);
        latestResult = photonCamera.getLatestResult();
        instance.getTable("photonvision")
                .getSubTable(Hardware.PHOTON_CAM)
                .getEntry("rawBytes")
                .addListener((notif) -> {
                    latestResult = photonCamera.getLatestResult();
                    cameraPose = null; // New data, invalidate camera pose
                }, EntryListenerFlags.kUpdate);
    }

    @Log
    public boolean hasTargets() {
        return latestResult.hasTargets();
    }

    /**
     * Calculates the camera pose using the best target. Returns null if there is no valid pose.
     *
     * @return The calculated camera pose.
     */
    public Pose3d getCameraPose() {
        if (cameraPose == null) {
            // No cached value, calculate current camera pose if possible
            if (!hasTargets()) {
                cameraPose = Optional.empty();
            } else {
                cameraPose = Optional.ofNullable(getCameraPoseUsingTarget(latestResult.getBestTarget()));
            }
        }
        // Unwrap Optional, defaulting to null if it's empty
        return cameraPose.orElse(null);
    }
}
