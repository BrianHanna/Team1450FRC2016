package org.usfirst.frc.team1450.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1450.robot.Robot;

/**
 *
 */
public class DriveForward extends Command {
    public DriveForward() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drives);
    }
    
    static int stateMachinePtr = 0;
    static int loopCounter = 0;

    // Called just before this Command runs the first time
    protected void initialize() {
    	stateMachinePtr = 0;
    	loopCounter = 0;
    	Robot.drives.Drive(0.5, 0);
    	Timer.delay(3.0);
    	
    }

    // Called repeatedly when this Command is scheduled to run
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

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
