package frc.team2412.robot.subsystem;

import static frc.team2412.robot.Hardware.*;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;

public class PostClimbSubsystem extends SubsystemBase implements Loggable {

    private Solenoid clampSolenoid;

    public PostClimbSubsystem() {
        clampSolenoid = new Solenoid(PNEUMATIC_HUB, PneumaticsModuleType.REVPH, POST_CLIMB_SOLENOID_UPWARDS);
        setClamp();
    }

    public void releaseClamp() {
        clampSolenoid.set(true);
    }

    public void setClamp() {
        clampSolenoid.set(false);
    }

}
