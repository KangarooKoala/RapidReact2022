package frc.team2412.robot.util.logging;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.ShuffleboardContainerWrapper;

public class LoggableSubsystem extends SubsystemBase implements Loggable {
    protected Set<Class<? extends Constants>> loggedConstantClasses = new HashSet<>();

    @Override
    public void addCustomLogging(ShuffleboardContainerWrapper container) {
        ;
    }
}
