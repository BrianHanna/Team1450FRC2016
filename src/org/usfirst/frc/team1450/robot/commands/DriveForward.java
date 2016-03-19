package org.usfirst.frc.team1450.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    	Robot.drives.ResetEncPos();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	switch ( stateMachinePtr )
    	{
    	case 0:
			Robot.drives.Drive(SmartDashboard.getNumber("AutoDriveSpeed%")/100, 0);
			stateMachinePtr++;
			break;
		case 1:
			if (Robot.drives.GetEncPos() * 37 / 12593 >= SmartDashboard.getNumber("AutoDriveDistance"))
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
			if (loopCounter++ >= (1.5 / 0.02))
			{
				Robot.feeder.Off();
				Robot.drives.SetRawMotor(0.5, 0.5);
				stateMachinePtr++;
			}
			break;
		case 3:
			if (((Robot.gyro.getAngle() >= 160) && (Robot.gyro.getAngle() <= 200)) || ((Robot.gyro.getAngle() <= -160) && (Robot.gyro.getAngle() >= -200)))
			{
				Robot.drives.Drive(0, 0);
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
