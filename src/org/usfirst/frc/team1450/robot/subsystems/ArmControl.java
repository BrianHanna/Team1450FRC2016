
package org.usfirst.frc.team1450.robot.subsystems;

import org.usfirst.frc.team1450.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;

/**
 *
 */
public class ArmControl extends Subsystem {
    
	CANTalon leftArmMotor;
	CANTalon rightArmMotor;
	DigitalInput leftArmDown;
	DigitalInput leftArmUp;
	DigitalInput rightArmDown;
	DigitalInput rightArmUp;
	public void Init() {
		if (leftArmMotor == null)
		{
			leftArmMotor = new CANTalon(RobotMap.leftArmMotor);
			leftArmMotor.ConfigFwdLimitSwitchNormallyOpen(true);
			leftArmMotor.ConfigRevLimitSwitchNormallyOpen(true);
			leftArmMotor.enableBrakeMode(true);
			leftArmMotor.enableLimitSwitch(true, true);
//			leftArmMotor.reverseOutput(false);	//closed loop method
			leftArmMotor.setInverted(true);
		}
		if (rightArmMotor == null)
		{
			rightArmMotor = new CANTalon(RobotMap.rightArmMotor);
			rightArmMotor.ConfigFwdLimitSwitchNormallyOpen(true);
			rightArmMotor.ConfigRevLimitSwitchNormallyOpen(true);
			rightArmMotor.enableBrakeMode(true);
			rightArmMotor.enableLimitSwitch(true, true);
//			rightArmMotor.reverseOutput(true);	//closed loop method
			rightArmMotor.setInverted(false);
		}
		if (leftArmDown == null)
		{
			leftArmDown = new DigitalInput(RobotMap.leftArmDownSwitch);
		}
		if (leftArmUp == null)
		{
			leftArmUp = new DigitalInput(RobotMap.leftArmUpSwitch);
		}
		if (rightArmDown == null)
		{
			rightArmDown = new DigitalInput(RobotMap.rightArmDownSwitch);
		}
		if (rightArmUp == null)
		{
			rightArmUp = new DigitalInput(RobotMap.rightArmUpSwitch);
		}
		LeftOffCommand();
		RightOffCommand();
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public void TeleopInit()
	{
		//
	}
	
	public void TeleopPeriodic(double leftY)
	{
//		SmartDashboard.putBoolean("leftArmDown", leftArmDown.get());
//		SmartDashboard.putBoolean("leftArmUp", leftArmUp.get());
//		SmartDashboard.putBoolean("rightArmDown", rightArmDown.get());
//		SmartDashboard.putBoolean("rightArmUp", rightArmUp.get());
		if (leftY < -0.2) {
			if (!leftArmUp.get())
			{
				leftArmMotor.set(0.0);
			}
			else
			{
				leftArmMotor.set(leftY * 0.5);
			}
			if (!rightArmUp.get())
			{
				rightArmMotor.set(0.0);
			}
			else
			{
				rightArmMotor.set(leftY * 0.5);
			}
		} else {
			if (leftY > 0.2) {
				if (!leftArmDown.get())
				{
					leftArmMotor.set(0.0);
				}
				else
				{
					leftArmMotor.set(leftY * 0.5);
				}
				if (!rightArmDown.get())
				{
					rightArmMotor.set(0.0);
				}
				else
				{
					rightArmMotor.set(leftY * 0.5);
				}
			} else {
				leftArmMotor.set(0.0);
				rightArmMotor.set(0.0);
			}
		}
	}

    public void LeftUpCommand(double leftOut) {
        leftArmMotor.set(leftOut);
    }
    
    public void LeftDownCommand(double leftOut) {
    	leftArmMotor.set(leftOut);
    }
    
    public void LeftOffCommand() {
    	leftArmMotor.set(0.0);
    }
    
    public void RightUpCommand(double rightOut) {
        rightArmMotor.set(rightOut);
    }
    
    public void RightDownCommand(double rightOut) {
    	rightArmMotor.set(rightOut);
    }
    
    public void RightOffCommand() {
    	rightArmMotor.set(0.0);
    }

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}

