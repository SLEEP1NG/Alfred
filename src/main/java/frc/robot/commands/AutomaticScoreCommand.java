/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.Timer;
import frc.util.Limelight;

public class AutomaticScoreCommand extends CommandGroup {
  public AutomaticScoreCommand() {
    if(Timer.getMatchTime() < 15 && Limelight.getTargetArea() > 0.06){
        addSequential(new FloopStartScoreCommand());
        addSequential(new WaitCommand(0.2));
        addSequential(new FloopPullCommand());
    }
  }
}
