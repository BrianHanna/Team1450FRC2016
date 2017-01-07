
package org.usfirst.frc.team1450.robot.subsystems;

import org.usfirst.frc.team1450.robot.Robot;
import org.usfirst.frc.team1450.robot.RobotMap;

import com.ctre.CANTalon;
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
	static int loopCounter;
	static int nudgePtr = 0;
	static double lowPassFilteredSpeed;
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
	
	public void TeleopInit()
	{
		loopCounter = 0;
		lowPassFilteredSpeed = 0;
		nudgePtr = 0;
	}
	
	public void TeleopPeriodic(double robotAngle, double leftXAxis, double leftYAxis, double rightXAxis, double rightYAxis, boolean up, boolean right, boolean down, boolean left, double leftTrigger, double rightTrigger, boolean leftButton, boolean rightButton, int povAngle)
	{
		// Determine max speed
		double maxDriveSpeed = SmartDashboard.getNumber("MaxDriveSpeed%",70);
		if (maxDriveSpeed > 100)
		{
			maxDriveSpeed = 100;
		}
		if (maxDriveSpeed < 0)
		{
			maxDriveSpeed = 0;
		}
		maxDriveSpeed = maxDriveSpeed / 100;
		if (leftButton && (!rightButton) )
		{
			maxDriveSpeed = maxDriveSpeed * 0.25;
		}
		else
		{
			if (leftButton && rightButton )
			{
				maxDriveSpeed = maxDriveSpeed * 0.5;
			}
			else
			{
				if ((!leftButton) && rightButton )
				{
					maxDriveSpeed = maxDriveSpeed * 0.75;
				}
			}
		}
		
		//Get left stick drive command
		double driveCommand;
//		driveCommand = Math.sqrt((leftXAxis * leftXAxis) + (leftXAxis * leftXAxis));
//		if (leftXAxis <= 0)
//		{
//			driveCommand = -driveCommand;
//		}
		// to revert replace /*driveCommand*/ with /*leftXAxis*/
		driveCommand = leftYAxis;
		lowPassFilteredSpeed += (driveCommand - lowPassFilteredSpeed) * 0.3;
		double rightStickCommand = Math.sqrt((rightXAxis * rightXAxis) + (rightYAxis * rightYAxis));
		double rightStickRotation = 0;
		double stickAngle = 0;
		rightYAxis = -rightYAxis;
		if (rightXAxis == 0)
		{
			if (rightYAxis < 0)
			{
				stickAngle = 180;
			}
		}
		else
		{
			if (rightYAxis == 0)
			{
				if (rightXAxis < 0)
				{
					stickAngle = 270;
				}
				else
				{
					if (rightXAxis > 0)
					{
						stickAngle = 90;
					}
				}
			}
			else
			{
				stickAngle = Math.atan(Math.abs(rightXAxis/rightYAxis)) * 180 / Math.PI;
				if (rightYAxis < 0)
				{
					if (rightXAxis > 0)
					{
						stickAngle = 180 - stickAngle;
					}
					else
					{
						stickAngle = 180 + stickAngle;
					}
				}
				else
				{
					if (rightXAxis < 0)
					{
						stickAngle = 360 - stickAngle;
					}
				}
			}
		}
		SmartDashboard.putNumber("RightStickRawCmd", rightStickCommand);
		SmartDashboard.putNumber("StickAngle", stickAngle);
		double angleError = stickAngle - robotAngle;
		if (angleError > 180)
		{
			angleError = angleError - 360;
		}
		else
		{
			if (angleError < -180)
			{
				angleError = angleError + 360;
			}
		}
		rightStickRotation = rightStickCommand * (Math.sin((angleError * Math.PI / 180 / 2)));
		rightStickCommand = -1 * rightStickCommand * (Math.cos((angleError * Math.PI / 180 / 2)));
		SmartDashboard.putNumber("RightStickCmd", rightStickCommand);
		SmartDashboard.putNumber("RightStickRot", rightStickRotation);
		
		//robotAngle = 0

		//Drive robot
		SmartDashboard.putNumber("adjustedAngle", robotAngle);
		if (nudgePtr == 0)
		{
			if (povAngle == 0)
			{
				nudgePtr = 1;
			}
			if (povAngle == 180)
			{
				nudgePtr = 3;
			}
		}
		switch (nudgePtr)
		{
			case 0:
				if (up)	// Up
				{
					if ((robotAngle > 350) || (robotAngle < 10))
					{
						robotDrive.drive(1, 0);
					}
					else
					{
						if (robotAngle >= 180)
						{
							// right turn
							leftDrive.set(1.0);
							rightDrive.set(1.0);
						}
						else
						{
							// left turn
							leftDrive.set(-1.0);
							rightDrive.set(-1.0);
						}
					}
				}
				else
				{
					if (right)	// Right
					{
						if ((robotAngle > 80) && (robotAngle < 100))
						{
							robotDrive.drive(1, 0);
						}
						else
						{
							if ((robotAngle <= 85) || (robotAngle >= 270))
							{
								// right turn
								leftDrive.set(1.0);
								rightDrive.set(1.0);
							}
							else
							{
								// left turn
								leftDrive.set(-1.0);
								rightDrive.set(-1.0);
							}
						}
					}
					else
					{
						if (down)	// Down
						{
							if ((robotAngle > 170) && (robotAngle < 190))
							{
								robotDrive.drive(1, 0);
							}
							else
							{
								if (robotAngle <= 175)
								{
									// right turn
									leftDrive.set(1.0);
									rightDrive.set(1.0);
								}
								else
								{
									// left turn
									leftDrive.set(-1.0);
									rightDrive.set(-1.0);
								}
							}
						}
						else
						{
							if (left)	// Left
							{
								if ((robotAngle > 260) && (robotAngle < 280))
								{
									robotDrive.drive(1, 0);
								}
								else
								{
									if ((robotAngle <= 265) && (robotAngle >= 90))
									{
										// right turn
										leftDrive.set(1.0);
										rightDrive.set(1.0);
									}
									else
									{
										// left turn
										leftDrive.set(-1.0);
										rightDrive.set(-1.0);
									}
								}
							}
							else
							{
								if (leftTrigger > 0.2)
								{
									leftDrive.set(-leftTrigger);
									rightDrive.set(-leftTrigger);
								}
								else
								{
									if (rightTrigger > 0.2)
									{
										leftDrive.set(rightTrigger);
										rightDrive.set(rightTrigger);
									}
									else
									{
										if ((driveCommand > 0.1) || (driveCommand < -0.1))
										{
											robotDrive.arcadeDrive(-driveCommand * maxDriveSpeed, -leftXAxis, true);
										}
										else
										{
											robotDrive.arcadeDrive(-rightStickCommand * maxDriveSpeed, -rightStickRotation, true);
										}
									}
								}
							}
						}
					}
				}
				break;
			case 1:
				robotDrive.drive(0,0);
				leftDrive.setEncPosition(0);
				rightDrive.setEncPosition(0);
				robotDrive.drive(0.5, 0);
				nudgePtr++;
				loopCounter = 0;
				break;
			case 2:
				if (Robot.drives.GetEncPos() * 37 / 12593 >= 1)
				{
					robotDrive.drive(0, 0);
					loopCounter = 0;
					nudgePtr = 0;
				}
				else
				{
					if (loopCounter++ >= (3 / 0.02))
					{
						Robot.drives.Drive(0, 0);
						loopCounter = 0;
						nudgePtr = 0;
					}
				}
				break;
			case 3:
				robotDrive.drive(0,0);
				leftDrive.setEncPosition(0);
				rightDrive.setEncPosition(0);
				robotDrive.drive(-0.5, 0);
				nudgePtr++;
				loopCounter = 0;
				break;
			case 4:
				if (Robot.drives.GetEncPos() * 37 / 12593 <= -1.0)
				{
					robotDrive.drive(0, 0);
					loopCounter = 0;
					nudgePtr = 0;
				}
				else
				{
					if (loopCounter++ >= (3 / 0.02))
					{
						Robot.drives.Drive(0, 0);
						loopCounter = 0;
						nudgePtr = 0;
					}
				}
				break;
			default:
				break;
		}
		//Display drive stats
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
	
	public void EnableBreakingMode(boolean breakBool)
	{
		leftDrive.enableBrakeMode(breakBool);
		rightDrive.enableBrakeMode(breakBool);
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

	public void SetRawMotor(double leftMotPow, double rightMotPow) {
		leftDrive.set(leftMotPow);
		rightDrive.set(rightMotPow);
	}
}
