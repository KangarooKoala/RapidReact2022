package frc.team2412.robot.util.sendable;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class SendablePose3d implements Sendable {
    private Supplier<Pose3d> poseSupplier;

    public SendablePose3d(Pose3d pose) {
        this(() -> pose);
    }

    public SendablePose3d(Supplier<Pose3d> poseSupplier) {
        this.poseSupplier = poseSupplier;
    }

    private void addDoubleProperty(SendableBuilder builder, String title, ToDoubleFunction<Pose3d> map) {
        builder.addDoubleProperty(title, SendableUtil.mapNullableToDouble(poseSupplier, map), SendableUtil::doNothing);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        addDoubleProperty(builder, "Translation X", pose -> pose.getTranslation().getX());
        addDoubleProperty(builder, "Translation Y", pose -> pose.getTranslation().getY());
        addDoubleProperty(builder, "Translation Z", pose -> pose.getTranslation().getZ());
        addDoubleProperty(builder, "Rotation X Axis", pose -> pose.getRotation().getX());
        addDoubleProperty(builder, "Rotation Y Axis", pose -> pose.getRotation().getY());
        addDoubleProperty(builder, "Rotation Z Axis", pose -> pose.getRotation().getZ());
        addDoubleProperty(builder, "Quaternion W", pose -> pose.getRotation().getQuaternion().getW());
        addDoubleProperty(builder, "Quaternion X", pose -> pose.getRotation().getQuaternion().getX());
        addDoubleProperty(builder, "Quaternion Y", pose -> pose.getRotation().getQuaternion().getY());
        addDoubleProperty(builder, "Quaternion Z", pose -> pose.getRotation().getQuaternion().getZ());
    }
}
