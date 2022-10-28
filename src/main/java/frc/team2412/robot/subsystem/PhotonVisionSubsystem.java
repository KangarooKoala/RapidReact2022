package frc.team2412.robot.subsystem;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team2412.robot.Hardware;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

public class PhotonVisionSubsystem extends SubsystemBase implements Loggable {
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
