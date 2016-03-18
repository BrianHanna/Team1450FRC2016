
package org.usfirst.frc.team1450.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.DigitalInput;

import org.usfirst.frc.team1450.robot.commands.DriveForward;
import org.usfirst.frc.team1450.robot.commands.ExampleCommand;
import org.usfirst.frc.team1450.robot.commands.AutoShootBall;
import org.usfirst.frc.team1450.robot.commands.ReleaseBoulder;
import org.usfirst.frc.team1450.robot.commands.FeedBoulder;
import org.usfirst.frc.team1450.robot.commands.FeederOff;
import org.usfirst.frc.team1450.robot.subsystems.ArmControl;
import org.usfirst.frc.team1450.robot.subsystems.Drives;
import org.usfirst.frc.team1450.robot.subsystems.Feeder;
import org.usfirst.frc.team1450.robot.subsystems.Tower;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ArmControl armControl = new ArmControl();
	public static final Drives drives = new Drives();
	public static final Tower tower = new Tower();
	public static final Feeder feeder = new Feeder();
	public static OI oi;
	Compressor c;
	CameraServer camServ;
	ADXRS450_Gyro gyro;
	Servo camServoY;
	Servo camServoX;
	DigitalInput leftTowerDown;
	DigitalInput rightTowerDown;
	DigitalInput leftArmDown;
	DigitalInput leftArmUp;
	DigitalInput rightArmDown;
	DigitalInput rightArmUp;
	Command autonomousCommand;
	SendableChooser chooser;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		oi = new OI();
		armControl.Init();
		drives.Init();
		tower.Init();
		feeder.Init();
		leftTowerDown = new DigitalInput(RobotMap.leftTowerHomeSwitch);
		rightTowerDown = new DigitalInput(RobotMap.rightTowerHomeSwitch);
		leftArmDown = new DigitalInput(RobotMap.leftArmDownSwitch);
		leftArmUp = new DigitalInput(RobotMap.leftArmUpSwitch);
		rightArmDown = new DigitalInput(RobotMap.rightArmDownSwitch);
		rightArmUp = new DigitalInput(RobotMap.rightArmUpSwitch);
		oi.aButton1.whileHeld(new FeedBoulder());
		oi.aButton1.whenReleased(new FeederOff());
		oi.bButton1.whileHeld(new ReleaseBoulder());
		oi.bButton1.whenReleased(new FeederOff());
		chooser = new SendableChooser();
		chooser.addDefault("Drive Forward", new DriveForward());
		c = new Compressor(1);
		c.setClosedLoopControl(true);
		c.start();
		camServ = CameraServer.getInstance();
        try
        {
	        camServ.setQuality(50);
	        camServ.startAutomaticCapture("cam0");
	        
        }
        catch (Exception e)
        {
        	//
        }
        gyro = new ADXRS450_Gyro();
        chooser.addObject("Shoot Boulder Autonomous", new AutoShootBall());
		chooser.addObject("Do nothing", new ExampleCommand());
		camServoY = new Servo(1);
		camServoX = new Servo(0);
		camServoX.set(-0.025);
		camServoY.set(0.855);
		SmartDashboard.putData("Auto mode", chooser);
		SmartDashboard.putNumber("MaxDriveSpeed%", 95);
		SmartDashboard.putNumber("TowerSpeed%", 100);
		SmartDashboard.putNumber("AutoDriveSpeed%", 95);
		SmartDashboard.putNumber("AutoDriveDistance", 17 * 12);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	public void autonomousInit() {
		autonomousCommand = (Command) chooser.getSelected();
		

//		String autoSelected = SmartDashboard.getString("Auto Selector", "Drive Forward");
//		switch (autoSelected) {
//		case "Drive Forward":
//			autonomousCommand = new DriveForward();
//
//			break;
//		case "Drive Backwards":
//		default:
//			autonomousCommand = new DriveBackwards();
//			break;
//		}

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	double lowPassFilteredSpeed = 0.0;
	double camXFiltered = 0.0;
	double camYFiltered = 0.0;
	double camXPosition=-0.197;
	double camYPosition=0.765;
	
	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("GyroAngle", gyro.getAngle());
//		SmartDashboard.putBoolean("leftTowerDown", leftTowerDown.get());
//		SmartDashboard.putBoolean("rightTowerDown", rightTowerDown.get());
//		SmartDashboard.putBoolean("leftArmDown", leftArmDown.get());
//		SmartDashboard.putBoolean("leftArmUp", leftArmUp.get());
//		SmartDashboard.putBoolean("rightArmDown", rightArmDown.get());
//		SmartDashboard.putBoolean("rightArmUp", rightArmUp.get());
		drives.GetDriveMotorStats();
		if (oi.controller2.getRawAxis(RobotMap.xBoxLeftY) < -0.2) {
			if (!leftArmUp.get())
			{
				armControl.LeftOffCommand();
			}
			else
			{
				armControl.LeftUpCommand(oi.controller2.getRawAxis(RobotMap.xBoxLeftY) * 0.5);
			}
			if (!rightArmUp.get())
			{
				armControl.RightOffCommand();
			}
			else
			{
				armControl.RightUpCommand(oi.controller2.getRawAxis(RobotMap.xBoxLeftY) * 0.5);
			}
		} else {
			if (oi.controller2.getRawAxis(RobotMap.xBoxLeftY) > 0.2) {
				if (!leftArmDown.get())
				{
					armControl.LeftOffCommand();
				}
				else
				{
					armControl.LeftDownCommand(oi.controller2.getRawAxis(RobotMap.xBoxLeftY) * 0.5);
				}
				if (!rightArmDown.get())
				{
					armControl.RightOffCommand();
				}
				else
				{
					armControl.RightDownCommand(oi.controller2.getRawAxis(RobotMap.xBoxLeftY) * 0.5);
				}
			} else {
				armControl.LeftOffCommand();
				armControl.RightOffCommand();
			}
		}
		camXFiltered += ((oi.controller1.getRawAxis(RobotMap.xBoxRightX)*1) - camXFiltered) * 0.3;
		camYFiltered += (oi.controller1.getRawAxis(RobotMap.xBoxRightY) - camYFiltered) * 0.3;
		if (((oi.controller1.getRawAxis(RobotMap.xBoxRightX)*-1) > 0.5) || ((oi.controller1.getRawAxis(RobotMap.xBoxRightX)*-1) < -0.5))
		{
			camXPosition += (oi.controller1.getRawAxis(RobotMap.xBoxRightX)*-1) * 0.01;
		}
		if ((oi.controller1.getRawAxis(RobotMap.xBoxRightY) > 0.2) || (oi.controller1.getRawAxis(RobotMap.xBoxRightY) < -0.2))
		{
			camYPosition += oi.controller1.getRawAxis(RobotMap.xBoxRightY) * 0.01;
		}
		if (camXPosition > 1)
		{
			camXPosition=1;
		}
		if (camXPosition < -1)
		{
			camXPosition=-1;
		}
		if (camYPosition > 1)
		{
			camYPosition=1;
		}
		if (camYPosition < -1)
		{
			camYPosition=-1;
		}
		SmartDashboard.putNumber("CamX", camXPosition);
		SmartDashboard.putNumber("CamY", camYPosition);
		//drives.ArcadeDrive(lowPassFilteredSpeed, oi.controller1.getX(Hand.kLeft));	//drives with lowPassFilter
		double maxDriveSpeed = SmartDashboard.getNumber("MaxDriveSpeed%");
		if (maxDriveSpeed > 100)
		{
			maxDriveSpeed = 100;
		}
		if (maxDriveSpeed < 0)
		{
			maxDriveSpeed = 0;
		}
		maxDriveSpeed = maxDriveSpeed / 100;
		double driveCommand = Math.sqrt((oi.controller1.getY(Hand.kLeft) * oi.controller1.getY(Hand.kLeft)) + (oi.controller1.getX(Hand.kLeft) * oi.controller1.getX(Hand.kLeft)));
		if (oi.controller1.getY(Hand.kLeft) <= 0)
		{
			driveCommand = -driveCommand;
		}
		// to revert replace /*driveCommand*/ with /*oi.controller1.getY(Hand.kLeft)*/
		driveCommand = oi.controller1.getY(Hand.kLeft);
		lowPassFilteredSpeed += (driveCommand - lowPassFilteredSpeed) * 0.3;
		drives.ArcadeDrive(driveCommand * maxDriveSpeed, oi.controller1.getX(Hand.kLeft));
		tower.GetMotorStatus(!leftTowerDown.get(), !rightTowerDown.get());
		double leftOut, rightOut;
		leftOut = oi.controller2.getRawAxis(RobotMap.xBoxRightY);
		rightOut = oi.controller2.getRawAxis(RobotMap.xBoxRightY);
		SmartDashboard.putBoolean("LeftTowerDown", !leftTowerDown.get());
		SmartDashboard.putBoolean("LeftTowerDown", !leftTowerDown.get());
		SmartDashboard.putNumber("TowerMove", oi.controller2.getRawAxis(RobotMap.xBoxRightY));
		if (oi.controller2.getRawAxis(RobotMap.xBoxRightY) > 0 )
		{
			if (!leftTowerDown.get())
			{
				leftOut = 0;
			}
			if (!rightTowerDown.get())
			{
				rightOut = 0;
			}
		}
		tower.Move(leftOut, rightOut);
		camServoX.set(-camXPosition);
    	camServoY.set(camYPosition);
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}
}
