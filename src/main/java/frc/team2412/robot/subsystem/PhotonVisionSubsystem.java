package frc.team2412.robot.subsystem;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team2412.robot.Hardware;
import frc.team2412.robot.apriltag.AprilTagFieldLayout;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.io.IOException;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

public class PhotonVisionSubsystem extends SubsystemBase implements Loggable {
    private static final AprilTagFieldLayout fieldLayout;
    static {
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
                }, EntryListenerFlags.kUpdate);
    }

    @Log
    public boolean hasTargets() {
        return latestResult.hasTargets();
    }
}
