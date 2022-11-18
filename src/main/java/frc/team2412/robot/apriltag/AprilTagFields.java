package frc.team2412.robot.apriltag;

public enum AprilTagFields {
    k2022RapidReact("2022-rapidreact.json");

    public static final String kBaseResourceDir = "/edu/wpi/first/apriltag/";

    public static final AprilTagFields kDefaultField = k2022RapidReact;

    public final String m_resourceFile;

    AprilTagFields(String resourceFile) {
        m_resourceFile = kBaseResourceDir + resourceFile;
    }
}
