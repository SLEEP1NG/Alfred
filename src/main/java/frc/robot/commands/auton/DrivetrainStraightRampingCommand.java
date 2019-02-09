/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auton;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class DrivetrainStraightRampingCommand extends DrivetrainDriveStraightCommand {

    private PIDController speedPIDController;
    private double speedPIDOutput;
    private boolean inRange;
    private double timeInRange;

    public DrivetrainStraightRampingCommand(double distance) {
        super(distance, 1);
    }

    @Override
    protected void initialize() {
        super.initialize();
        Robot.drivetrain.setRamp(SmartDashboard.getNumber("DriveStraight RampSeconds", 2.5));
        speedPIDController = new PIDController(0, 0, 0, new GyroPIDSource(), new GyroPIDOutput());
        speedPIDController.setSetpoint(distance);
        speedPIDController.setAbsoluteTolerance(3);
        speedPIDController.setPID(SmartDashboard.getNumber("DriveStraightRampingPID P", 0),
                SmartDashboard.getNumber("DriveStraightRampingPID I", 0),
                SmartDashboard.getNumber("DriveStraightRampingPID D", 0));
        speedPIDController.enable();
    }

    @Override
    protected void execute() {
        if (Math.abs(speedPIDOutput) < 0.15) {
            if (speedPIDController.onTarget()) {
                speedPIDOutput = 0;
            } else {
                speedPIDOutput = 0.15 * Math.signum(speedPIDOutput);
            }
        }
        speedPIDOutput = Math.min(Math.max(-1, speedPIDOutput), 1);

        double left = speedPIDOutput + getGyroPIDOutput();
        double right = speedPIDOutput - getGyroPIDOutput();
        Robot.drivetrain.tankDrive(left, right);
    }

    @Override
    protected boolean isFinished() {
        if (speedPIDController.onTarget() && !inRange) {
            timeInRange = Timer.getFPGATimestamp();
            inRange = true;
        } else if (!inRange) {
            inRange = false;
        }
        return inRange && Timer.getFPGATimestamp() - timeInRange > 0.5;
    }

    @Override
    protected void end() {
        super.end();
        Robot.drivetrain.setRamp(0);
        speedPIDController.setPID(0, 0, 0);
        speedPIDController.disable();
    }

    private class GyroPIDSource implements PIDSource {
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        @Override
        public double pidGet() {
            return Robot.drivetrain.getGyroAngle();
        }
    }

    private class GyroPIDOutput implements PIDOutput {
        @Override
        public void pidWrite(double output) {
            speedPIDOutput = output;
        }
    }
}
