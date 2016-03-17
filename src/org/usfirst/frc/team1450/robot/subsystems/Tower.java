package org.usfirst.frc.team1450.robot.subsystems;

import org.usfirst.frc.team1450.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
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
			
			//PID code
			leftTowerMotor.set(0);
			leftTowerMotor.changeControlMode(TalonControlMode.Speed);
			leftTowerMotor.setP(0.037);
			leftTowerMotor.setI(0.0005);
			leftTowerMotor.setD(0);
			leftTowerMotor.set(0);
			leftTowerMotor.reverseSensor(true);
		}
		if (rightTowerMotor == null)
		{
			rightTowerMotor = new CANTalon(RobotMap.rightTowerMotor);
			rightTowerMotor.ConfigFwdLimitSwitchNormallyOpen(true);
			rightTowerMotor.ConfigRevLimitSwitchNormallyOpen(true);
			rightTowerMotor.enableBrakeMode(true);
			rightTowerMotor.enableLimitSwitch(false, false);
//			rightTowerMotor.reverseOutput(true);	//closed loop method
			rightTowerMotor.setInverted(false);
			
			//PID code
			rightTowerMotor.set(0);
			rightTowerMotor.changeControlMode(TalonControlMode.Speed);
			rightTowerMotor.setP(0.037);
			rightTowerMotor.setI(0.0005);
			rightTowerMotor.setD(0);
			rightTowerMotor.set(0);
			rightTowerMotor.reverseSensor(true);
		}
		Off();
	}
	
	static double leftMaxVel = 0;
	static double rightMaxVel = 0;
	
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
		SmartDashboard.putNumber("leftTowerVel", leftTowerMotor.getEncVelocity());
		SmartDashboard.putNumber("rightTowerVel", rightTowerMotor.getEncVelocity());
	}
	
	//~460,000 encoders per 14.25"
	
	public void Move(double leftOut, double rightOut)
	{
		if ((leftOut < 0.2) && (leftOut > -0.2))
		{
			leftOut = 0.0;
		}
		if (leftOut < 0)
		{
			if (leftTowerMotor.getEncPosition() > 500000)
			{
				leftOut = 0.0;
			}
		}
		if (rightOut < 0)
		{
			if (rightTowerMotor.getEncPosition() > 500000)
			{
				rightOut = 0.0;
			}
		}
		leftOut = leftOut * SmartDashboard.getNumber("TowerSpeed%") / 100; 
		leftTowerMotor.set(leftOut /*Following for PID*/ * 8000/*16162.5*/);
		if ((rightOut < 0.2) && (rightOut > -0.2))
		{
			rightOut = 0.0;
		}
		rightOut = rightOut * SmartDashboard.getNumber("TowerSpeed%") / 100;
		SmartDashboard.putNumber("TowerSpeedCmd", rightOut * 8000/*16162.5*/);
		rightTowerMotor.set(rightOut/*Following for PID*/ * 8000/*16162.5*/);
		rightTowerMotor.configMaxOutputVoltage(8);
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
