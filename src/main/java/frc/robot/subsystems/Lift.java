
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.LiftMoveCommand;

public final class Lift extends Subsystem {
    private WPI_TalonSRX masterTalon;
    private WPI_VictorSPX followerTalon;

    private DigitalInput topLimitSwitch;
    private DigitalInput bottomLimitSwitch;

    // private DoubleSolenoid tiltSolenoid;
    //private Solenoid brakeSolenoid;

    public boolean rampDisabled;

    public Lift() {
        masterTalon = new WPI_TalonSRX(RobotMap.LIFT_MASTER_TALON_MOTOR_PORT);
        followerTalon = new WPI_VictorSPX(RobotMap.LIFT_FOLLOWER_VICTOR_MOTOR_PORT);

        followerTalon.follow(masterTalon);

        masterTalon.setNeutralMode(NeutralMode.Brake);
        followerTalon.setNeutralMode(NeutralMode.Brake);

        masterTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        // tiltSolenoid = new DoubleSolenoid(RobotMap.LIFT_TILT_SOLENOID_FORWARD_PORT,
                // RobotMap.LIFT_TILT_SOLENOID_REVERSE_PORT);
        // brakeSolenoid = new Solenoid(RobotMap.LIFT_BRAKE_SOLENOID_PORT);

        //topLimitSwitch = new DigitalInput(RobotMap.LIFT_TOP_LIMIT_SWITCH_PORT);
        //bottomLimitSwitch = new DigitalInput(RobotMap.LIFT_BOTTOM_LIMIT_SWITCH_PORT);

        enableRamping();

        /// Encoders
        masterTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new LiftMoveCommand());
    }

    public void resetEncoder() {
        masterTalon.setSelectedSensorPosition(0, 0, 0);
    }

    public void setHeight(double height) {
        masterTalon.setSelectedSensorPosition((int) (height / RobotMap.LIFT_ENCODER_RAW_MULTIPLIER), 0, 0);
    }

    // public boolean isAtTop() {
    //     boolean atTop = topLimitSwitch.get();
    //     if (atTop) {
    //         setEncoder(RobotMap.LIFT_MAX_HEIGHT);
    //     }
    //     return atTop;
    // }

    // public boolean isAtBottom() {
    //     boolean atBottom = bottomLimitSwitch.get();
    //     if (atBottom) {
    //         setEncoder(RobotMap.LIFT_MIN_HEIGHT);
    //     }
    //     return atBottom;
    // } 

    public void stopLift() {
        masterTalon.set(0);
        // enableBrake();
    }

    public void moveNoRamp(double speed) {
        System.out.println("moveNoRamp()");
        if (Math.abs(speed) < RobotMap.LIFT_MIN_SPEED) {
            stopLift();
        // } else if (isAtTop() || isAtBottom()) {
        //     stopLift();
        } else {
            // releaseBrake();
            masterTalon.set(speed);
        }
    }

    // rampMultiplier takes a distance from a hard limit, and calculates
    // multiplier for linear ramping
    public double rampMultiplier(double distance) {
        System.out.println("rampMultiplier()");
        double threshold = RobotMap.LIFT_RAMP_HEIGHT_THRESHOLD;
        if (threshold <= 0) {
            return 1.0;
        }
        if (distance > threshold) {
            return 1.0;
        }
        if (distance < 0) {
            return 1.0;
        }
        return distance / threshold;
    }

    // public void moveRamp(double desiredSpeed) {
    //     System.out.println("moveRamp");
    //     double currentHeight = getHeight();
    //     double speed = desiredSpeed;
    //     if (desiredSpeed < 0 && currentHeight < RobotMap.LIFT_RAMP_HEIGHT_THRESHOLD) {
    //         // If you want to move the lift down, get the distance from the bottom and adjust speed proportionally.
    //         double distanceFromBottom = currentHeight;
    //         speed = rampMultiplier(distanceFromBottom) * desiredSpeed;
    //         speed = Math.min(speed, -RobotMap.LIFT_MIN_SPEED);
    //     } else if (currentHeight > RobotMap.LIFT_MAX_HEIGHT - RobotMap.LIFT_RAMP_HEIGHT_THRESHOLD) {
    //         // If you want to move the lift up, get the distance from the top and adjust speed proportionally.
    //         double distanceFromTop = RobotMap.LIFT_MAX_HEIGHT - currentHeight;
    //         speed = rampMultiplier(distanceFromTop) * desiredSpeed;
    //         speed = Math.max(speed, RobotMap.LIFT_MIN_SPEED);
    //     }
    //     // If the current height isn't within the height range for ramping, move without ramping.
    //     forceMoveNoRamp(speed);
    // }

    public void moveLift(double speed) {
        System.out.println("moveLift()");
        moveNoRamp(speed);
        // if (rampDisabled) {
        //     moveNoRamp(speed);
        // } else {
        //     moveRamp(speed);
        // }
    }

    // public void tiltFoward() {
    //     tiltSolenoid.set(Value.kForward);
    // }

    // public void tiltBack() {
    //     tiltSolenoid.set(Value.kReverse);
    // }

    // public void enableBrake() {
    //     brakeSolenoid.set(false);
    // }

    // public void releaseBrake() {
    //     brakeSolenoid.set(true);
    // }

    public void enableRamping() {
        rampDisabled = false;
    }

    public void disableRamping() {
        rampDisabled = true;
    }
}
