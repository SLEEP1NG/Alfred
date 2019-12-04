/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.*;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class Drivetrain implements Subsystem {
  
  public SpeedController leftTopMotor;
  public SpeedController leftMiddleMotor;
  public SpeedController leftBottomMotor;
  public SpeedController rightTopMotor;
  public SpeedController rightMiddleMotor;
  public SpeedController rightBottomMotor;

  public SpeedControllerGroup leftSpeedGroup, rightSpeedGroup;
  public DifferentialDrive drive;

  public Encoder leftGreyhill, rightGreyhill;

  public Drivetrain() {
    leftTopMotor = new PWMSparkMax(2);
    leftMiddleMotor = new PWMSparkMax(3);
    leftBottomMotor = new PWMSparkMax(1);

    rightTopMotor = new PWMSparkMax(6);
    rightMiddleMotor = new PWMSparkMax(7);
    rightBottomMotor = new PWMSparkMax(5);

    leftGreyhill = new Encoder(2,3);
    rightGreyhill = new Encoder(0,1);

    rightTopMotor.setInverted(true);
    rightMiddleMotor.setInverted(true);
    rightBottomMotor.setInverted(true);

    leftTopMotor.setInverted(true);
    leftMiddleMotor.setInverted(true);
    leftBottomMotor.setInverted(true);

    leftSpeedGroup = new SpeedControllerGroup(leftTopMotor, leftMiddleMotor, leftBottomMotor);
    rightSpeedGroup = new SpeedControllerGroup(rightTopMotor, rightMiddleMotor, rightBottomMotor);
    
    drive = new DifferentialDrive(leftSpeedGroup, rightSpeedGroup);

    drive.setSafetyEnabled(false);
  }
}