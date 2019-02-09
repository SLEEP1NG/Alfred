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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class DrivetrainDriveStraightCommand extends DrivetrainMoveInchesCommand {

    private PIDController rotationPIDController;
    private double gyroPIDOutput;

    public DrivetrainDriveStraightCommand(double distance, double speed) {
        super(distance, speed);
        requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
        super.initialize();
        Robot.drivetrain.resetGyro();
        rotationPIDController = new PIDController(0, 0, 0, new GyroPIDSource(), new GyroPIDOutput());
        rotationPIDController.setSetpoint(Robot.drivetrain.getGyroAngle());
        rotationPIDController.setPID(SmartDashboard.getNumber("DriveStraightGyroPID P", 0),
                SmartDashboard.getNumber("DriveStraightGyroPID I", 0),
                SmartDashboard.getNumber("DriveStraightGyroPID D", 0));
        rotationPIDController.enable();
    }

    @Override
    protected void execute() {
        double sign = Math.signum(this.distance - Robot.drivetrain.getGreyhillDistance());
        Robot.drivetrain.tankDrive(sign * speed + getGyroPIDOutput(), sign * speed - getGyroPIDOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return super.isFinished();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        super.end();
        rotationPIDController.setPID(0, 0, 0);
        rotationPIDController.disable();
    }

    protected double getGyroPIDOutput() {
        return gyroPIDOutput;
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
            gyroPIDOutput = output;
        }
    }
}