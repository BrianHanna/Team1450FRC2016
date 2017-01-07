package org.usfirst.frc.team1450.robot.commands;

import org.usfirst.frc.team1450.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShootBall extends Command {

	public AutoShootBall() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drives);
        requires(Robot.feeder);
    }
	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}
	
	static int stateMachinePtr = 0;
	static int loopCounter = 0;

	@Override
	protected void execute() {
		switch (stateMachinePtr)
		{
			case 0:
				Robot.drives.Drive(SmartDashboard.getNumber("AutoDriveSpeed%",70)/100, 0);
				stateMachinePtr++;
				break;
			case 1:
				if (Robot.drives.GetEncPos() * 37 / 12593 >= 50)
				{
					Robot.drives.Drive(0, 0);
					Robot.feeder.Release();
					loopCounter = 0;
					stateMachinePtr++;
				}
				else
				{
					if (loopCounter++ >= (10 / 0.02))
					{
						Robot.drives.Drive(0, 0);
						Robot.feeder.Release();
						loopCounter = 0;
						stateMachinePtr++;
					}
				}
				break;
			case 2:
				if (loopCounter++ >= (3 / 0.02))
				{
					Robot.feeder.Off();
					stateMachinePtr++;
				}
				break;
			default:
				break;
		}
	}

	@Override
	protected void initialize() {
		stateMachinePtr = 0;
		loopCounter = 0;
		Robot.drives.ResetEncPos();
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
