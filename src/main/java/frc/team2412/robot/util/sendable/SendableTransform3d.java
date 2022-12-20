package frc.team2412.robot.util.sendable;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class SendableTransform3d implements Sendable {
    private Supplier<Transform3d> transformSupplier;

    public SendableTransform3d(Transform3d transform) {
        this(() -> transform);
    }

    public SendableTransform3d(Supplier<Transform3d> transformSupplier) {
        this.transformSupplier = transformSupplier;
    }

    private void addDoubleProperty(SendableBuilder builder, String title, ToDoubleFunction<Transform3d> map) {
        builder.addDoubleProperty(title, SendableUtil.mapNullableToDouble(transformSupplier, map),
                SendableUtil::doNothing);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        addDoubleProperty(builder, "Translation X", transform -> transform.getTranslation().getX());
        addDoubleProperty(builder, "Translation Y", transform -> transform.getTranslation().getY());
        addDoubleProperty(builder, "Translation Z", transform -> transform.getTranslation().getZ());
        addDoubleProperty(builder, "Rotation X Axis", transform -> transform.getRotation().getX());
        addDoubleProperty(builder, "Rotation Y Axis", transform -> transform.getRotation().getY());
        addDoubleProperty(builder, "Rotation Z Axis", transform -> transform.getRotation().getZ());
        addDoubleProperty(builder, "Quaternion W", transform -> transform.getRotation().getQuaternion().getW());
        addDoubleProperty(builder, "Quaternion X", transform -> transform.getRotation().getQuaternion().getX());
        addDoubleProperty(builder, "Quaternion Y", transform -> transform.getRotation().getQuaternion().getY());
        addDoubleProperty(builder, "Quaternion Z", transform -> transform.getRotation().getQuaternion().getZ());
    }
}
