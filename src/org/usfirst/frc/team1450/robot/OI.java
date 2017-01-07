package org.usfirst.frc.team1450.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {
	Joystick controller1 = new Joystick(0);
	Joystick controller2 = new Joystick(1);
	JoystickButton aButton1 = new JoystickButton(controller2,4);
	JoystickButton bButton1 = new JoystickButton(controller2,3);
}

