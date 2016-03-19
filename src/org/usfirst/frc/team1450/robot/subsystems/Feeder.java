package org.usfirst.frc.team1450.robot.subsystems;

import org.usfirst.frc.team1450.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Feeder extends Subsystem {
	CANTalon feederMotor;
	
	public void Init() {
		if (feederMotor == null)
		{
			feederMotor = new CANTalon(RobotMap.feederMotor);
			feederMotor.ConfigFwdLimitSwitchNormallyOpen(true);
			feederMotor.ConfigRevLimitSwitchNormallyOpen(true);
			feederMotor.enableBrakeMode(true);
			feederMotor.enableLimitSwitch(false, false);
//			feederMotor.reverseOutput(false);	//closed loop method
			feederMotor.setInverted(false);
		}
		Off();
	}
	
	public void Feed()
	{
		feederMotor.set(-1.0);
	}
	
	public void Release()
	{
		feederMotor.set(1.0);
	}
	
	public void Off()
	{
		feederMotor.set(0.0);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
