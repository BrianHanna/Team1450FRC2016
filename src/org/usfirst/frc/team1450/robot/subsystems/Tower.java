package org.usfirst.frc.team1450.robot.subsystems;

import org.usfirst.frc.team1450.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Tower extends Subsystem {
	CANTalon leftTowerMotor;
	CANTalon rightTowerMotor;
	
	public void Init() {
		if (leftTowerMotor == null)
		{
			leftTowerMotor = new CANTalon(RobotMap.leftTowerMotor);
			leftTowerMotor.ConfigFwdLimitSwitchNormallyOpen(true);
			leftTowerMotor.ConfigRevLimitSwitchNormallyOpen(true);
			leftTowerMotor.enableBrakeMode(true);
			leftTowerMotor.enableLimitSwitch(false, false);
//			leftTowerMotor.reverseOutput(false);	//closed loop method
			leftTowerMotor.setInverted(false);
		}
		if (rightTowerMotor == null)
		{
			rightTowerMotor = new CANTalon(RobotMap.rightTowerMotor);
			rightTowerMotor.ConfigFwdLimitSwitchNormallyOpen(true);
			rightTowerMotor.ConfigRevLimitSwitchNormallyOpen(true);
			rightTowerMotor.enableBrakeMode(true);
			rightTowerMotor.enableLimitSwitch(false, false);
//			rightTowerMotor.reverseOutput(true);	//closed loop method
			rightTowerMotor.setInverted(true);
		}
		Off();
	}
	
	public void GetMotorStatus(boolean leftSwitch, boolean rightSwitch)
	{
		if (leftSwitch)
		{
			leftTowerMotor.setEncPosition(0);
		}
		if (rightSwitch)
		{
			rightTowerMotor.setEncPosition(0);
		}
		SmartDashboard.putNumber("leftTowerPos", leftTowerMotor.getEncPosition());
		SmartDashboard.putNumber("rightTowerPos", rightTowerMotor.getEncPosition());
	}
	
	public void Move(double leftOut, double rightOut)
	{
		if ((leftOut < 0.2) && (leftOut > -0.2))
		{
			leftOut = 0.0;
		}
		if (leftOut < 0)
		{
			if (leftTowerMotor.getEncPosition() > 400000)
			{
				leftOut = 0.0;
			}
		}
		if (rightOut < 0)
		{
			if (rightTowerMotor.getEncPosition() > 400000)
			{
				rightOut = 0.0;
			}
		}
		leftOut = leftOut * SmartDashboard.getNumber("TowerSpeed%") / 100; 
		leftTowerMotor.set(leftOut);
		if ((rightOut < 0.2) && (rightOut > -0.2))
		{
			rightOut = 0.0;
		}
		rightOut = rightOut * SmartDashboard.getNumber("TowerSpeed%") / 100;
		rightTowerMotor.set(rightOut);
	}
	
	public void Up()
	{
		leftTowerMotor.set(1.0);
		rightTowerMotor.set(1.0);
	}
	
	public void Down()
	{
		leftTowerMotor.set(-1.0);
		rightTowerMotor.set(-1.0);
	}
	
	public void Off()
	{
		leftTowerMotor.set(0.0);
		rightTowerMotor.set(0.0);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
