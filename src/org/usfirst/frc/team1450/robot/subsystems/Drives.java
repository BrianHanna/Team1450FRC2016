
package org.usfirst.frc.team1450.robot.subsystems;

import org.usfirst.frc.team1450.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drives extends Subsystem {
	CANTalon leftDrive;
	CANTalon rightDrive;
	RobotDrive robotDrive;
	public void Init() {
		if (leftDrive == null)
		{
			leftDrive = new CANTalon(RobotMap.leftDrive);
			leftDrive.enableBrakeMode(true);
			leftDrive.reverseOutput(false);	//closed loop method
			leftDrive.setInverted(false);
			leftDrive.setEncPosition(0);
		}
		if (rightDrive == null)
		{
			rightDrive = new CANTalon(RobotMap.rightDrive);
			rightDrive.enableBrakeMode(true);
			rightDrive.reverseOutput(false);	//closed loop method
			rightDrive.setInverted(false);
			rightDrive.reverseSensor(true);
			rightDrive.setEncPosition(0);
		}
		if (robotDrive == null)
		{
			robotDrive = new RobotDrive(leftDrive,rightDrive);
			robotDrive.setSafetyEnabled(false);
		}
		Off();
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public void GetDriveMotorStats()
	{
		SmartDashboard.putNumber("leftDriveCurrent", leftDrive.getOutputCurrent());
		SmartDashboard.putNumber("leftDriveTemperature", leftDrive.getTemperature());
		SmartDashboard.putNumber("leftDriveVolt", leftDrive.getOutputVoltage());
		SmartDashboard.putNumber("rightDriveCurrent", rightDrive.getOutputCurrent());
		SmartDashboard.putNumber("rightDriveTemperature", rightDrive.getTemperature());
		SmartDashboard.putNumber("rightDriveVolt", rightDrive.getOutputVoltage());
		SmartDashboard.putNumber("leftDrivePos", leftDrive.getEncPosition());
		SmartDashboard.putNumber("rightDrivePos", rightDrive.getEncPosition());
	}
	
	public void ResetEncPos()
	{
		leftDrive.setEncPosition(0);
		rightDrive.setEncPosition(0);
	}
	
	public double GetEncPos()
	{
		return leftDrive.getEncPosition();
	}
	
	public void ArcadeDrive(double yAxis, double xAxis)
	{
		robotDrive.arcadeDrive(-yAxis, -xAxis, true);
	}
	
	public void Off()
	{
		robotDrive.stopMotor();
	}
	
	public void Drive(double magnitude, double curve)
	{
		robotDrive.drive(magnitude, curve);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}
