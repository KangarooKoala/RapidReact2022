package frc.team2412.robot.util.logging;

import edu.wpi.first.networktables.NetworkTableValue;
import io.github.oblarg.oblog.Loggable;

public abstract class Constants implements Loggable {
    public NetworkTableValue getLoggedNTValue(String name) {
        return ConstantLogger.getLoggedNTValue(getClass(), name);
    }

    public Object getLoggedValue(String name) {
        return getLoggedNTValue(name).getValue();
    }

    public double getLoggedDouble(String name) {
        return getLoggedNTValue(name).getDouble();
    }
}
