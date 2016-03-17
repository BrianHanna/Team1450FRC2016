package org.usfirst.frc.team1450.robot.commands;

import org.usfirst.frc.team1450.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveBackwards extends Command {

	public DriveBackwards() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drives);
    }
	
	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		switch ( stateMachinePtr )
    	{
    	case 0:
    		if (loopCounter++ >= (3 / 0.02))
    		{
    			Robot.drives.Drive(0.0, 0.0);
    			stateMachinePtr++;
    		}
    		break;
    		
    		default:
    			break;
    	}
	}
	
	static int stateMachinePtr = 0;
    static int loopCounter = 0;

	@Override
	protected void initialize() {
		stateMachinePtr = 0;
    	loopCounter = 0;
    	Robot.drives.Drive(-0.5, 0);
    	Timer.delay(3.0);
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
