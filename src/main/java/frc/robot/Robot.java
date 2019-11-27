/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.subsystems.Drivetrain;


import com.stuypulse.stuylib.math.streams.*;
import com.stuypulse.stuylib.math.streams.filters.*;

import com.stuypulse.stuylib.input.*;
import com.stuypulse.stuylib.input.gamepads.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {

    public static NetKeyGamepad gamepad = new NetKeyGamepad(0);
    public static Drivetrain drivetrain = new Drivetrain();
    public static Compressor compressor = new Compressor();

    public static IStream mRawForwards = () -> gamepad.getLeftY();
    public static IStream mRawSideways = () -> gamepad.getLeftX();

    public static IStream mForwards = new FilteredIStream(mRawForwards, new IStreamFilterGroup(
        new BasicFilters.Square(),
        new RollingAverage(16),
        new BasicFilters.Deadband(0.1),
        (x) -> x * 0.5
    ));

    public static IStream mSideways = new FilteredIStream(mRawSideways, new IStreamFilterGroup(
        new BasicFilters.Square(),
        new RollingAverage(8),
        new BasicFilters.Deadband(0.1),
        (x) -> x * 0.5
    ));

    public static double mTime = 0.0;

    public void robotInit() {
        compressor.stop();
    }

    public void robotPeriodic() { }
    public void disabledInit() { }
    public void disabledPeriodic() { }
    public void autonomousInit() { }
    public void autonomousPeriodic() { }

    public void teleopInit() {
        mTime = 0.0;
    }

    public void teleopPeriodic() {
        double forwards = mForwards.get();
        double sideways = mSideways.get();

        if(Math.abs(sideways) < 0.05) { mTime = 0.0; }
        else { mTime += Math.PI / 50.0; }

        double lmotor = forwards + Math.cos(mTime) * sideways;
        double rmotor = forwards + Math.sin(mTime) * sideways;
        
        drivetrain.tankDrive(lmotor, rmotor);
    }

    public void testPeriodic() {}
}