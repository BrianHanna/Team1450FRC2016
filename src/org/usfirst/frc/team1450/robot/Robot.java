
package org.usfirst.frc.team1450.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

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
	public static ADXRS450_Gyro gyro;
	Servo camServoY;
	Servo camServoX;
	Command autonomousCommand;
	SendableChooser<Command> chooser;
	boolean standardArcadeDrive = true;

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
		oi.aButton1.whileHeld(new FeedBoulder());
		oi.aButton1.whenReleased(new FeederOff());
		oi.bButton1.whileHeld(new ReleaseBoulder());
		oi.bButton1.whenReleased(new FeederOff());
		chooser = new SendableChooser<Command>();
		chooser.addDefault("Drive Forward", new DriveForward());
		CameraServer.getInstance().startAutomaticCapture();
        gyro = new ADXRS450_Gyro();
        chooser.addObject("Shoot Boulder Autonomous", new AutoShootBall());
		chooser.addObject("Do nothing", new ExampleCommand());
		camServoY = new Servo(1);
		camServoX = new Servo(0);
		camServoX.set(-0.197);
		camServoY.set(0.855);
		SmartDashboard.putData("Auto mode", chooser);
		SmartDashboard.putNumber("MaxDriveSpeed%", 100);
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
	
	double camXPosition;
	double camYPosition;
	
	public void autonomousInit() {
		autonomousCommand = (Command) chooser.getSelected();
		camXPosition=-0.197;
		camYPosition=0.765;
		camServoX.set(camXPosition);
		camServoY.set(camYPosition);
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
		camXPosition=-0.197;
		camYPosition=0.765;
		camServoX.set(camXPosition);
		camServoY.set(camYPosition);
		loopCounter = 0;
		drives.TeleopInit();
		tower.TeleopInit();
		armControl.TeleopInit();
	}

	double lowPassFilteredSpeed = 0.0;
	double camXFiltered = 0.0;
	double camYFiltered = 0.0;
	static int loopCounter = 0;
	
	
	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		if (oi.controller1.getRawButton(RobotMap.xBoxStartButton))
		{
			gyro.reset();
		}
		double robotAngle = gyro.getAngle();
		int angleDiv = (int) (robotAngle / 360);
		if (robotAngle < 0)
		{
			angleDiv = angleDiv - 1;
		}
		robotAngle = robotAngle - (angleDiv * 360);
		drives.TeleopPeriodic(robotAngle, oi.controller1.getRawAxis(RobotMap.xBoxLeftX), oi.controller1.getRawAxis(RobotMap.xBoxLeftY), oi.controller1.getRawAxis(RobotMap.xBoxRightX), oi.controller1.getRawAxis(RobotMap.xBoxRightY), oi.controller1.getRawButton(RobotMap.xBoxYButton), oi.controller1.getRawButton(RobotMap.xBoxBButton), oi.controller1.getRawButton(RobotMap.xBoxAButton), oi.controller1.getRawButton(RobotMap.xBoxXButton), oi.controller1.getRawAxis(RobotMap.xBoxLeftTrigger), oi.controller1.getRawAxis(RobotMap.xBoxRightTrigger), oi.controller1.getRawButton(RobotMap.xBoxLeftButton), oi.controller1.getRawButton(RobotMap.xBoxRightButton), oi.controller1.getPOV());
		armControl.TeleopPeriodic(oi.controller2.getRawAxis(RobotMap.xBoxLeftY));
		tower.TeleopPeriodic(oi.controller2.getRawAxis(RobotMap.xBoxRightY));
		SmartDashboard.putNumber("GyroAngle", gyro.getAngle());
		camXFiltered += ((oi.controller2.getRawAxis(RobotMap.xBoxRightX)*1) - camXFiltered) * 0.3;
		camYFiltered += (oi.controller2.getRawAxis(RobotMap.xBoxRightY) - camYFiltered) * 0.3;
		if (((oi.controller2.getRawAxis(RobotMap.xBoxRightX)*-1) > 0.5) || ((oi.controller2.getRawAxis(RobotMap.xBoxRightX)*-1) < -0.5))
		{
			camXPosition += (oi.controller2.getRawAxis(RobotMap.xBoxRightX)*-1) * 0.01;
		}
		if ((oi.controller2.getRawAxis(RobotMap.xBoxRightY) > 0.2) || (oi.controller2.getRawAxis(RobotMap.xBoxRightY) < -0.2))
		{
			camYPosition += oi.controller2.getRawAxis(RobotMap.xBoxRightY) * 0.01;
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
