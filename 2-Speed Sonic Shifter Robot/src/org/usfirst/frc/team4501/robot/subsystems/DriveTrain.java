package org.usfirst.frc.team4501.robot.subsystems;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.usfirst.frc.team4501.robot.OI;
import org.usfirst.frc.team4501.robot.Robot;
import org.usfirst.frc.team4501.robot.RobotMap;
import org.usfirst.frc.team4501.robot.commands.DriveController;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {
	public enum ShifterState {
		SF_HIGH, SF_LOW;
	}

	RobotDrive drive;
	OI oi;

	Talon rightTalon;
	Talon leftTalon;

	DoubleSolenoid shifter;
	ShifterState state;

	Encoder L_Encoder;
	Encoder R_Encoder;
	

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public DriveTrain() {
		oi = Robot.oi;
		this.leftTalon = new Talon(RobotMap.LEFTMOTOR);
		this.rightTalon = new Talon(RobotMap.RIGHTMOTOR);

		this.drive = new RobotDrive(leftTalon, rightTalon);
		this.shifter = new DoubleSolenoid(RobotMap.Solenoids.HIGHGEAR, RobotMap.Solenoids.LOWGEAR);

		this.L_Encoder = new Encoder(RobotMap.Encoders.L_A, RobotMap.Encoders.L_B);
		this.R_Encoder = new Encoder(RobotMap.Encoders.R_A, RobotMap.Encoders.R_B);
	    

	}
	public void initDefaultCommand() {
		System.out.println("initDefaultCommand: Execute");
		setDefaultCommand(DriveController.instance);
	}

	public void arcadeTriggerDrive(double forwardTrigger, double reverseTrigger, double rotate, double squaredRotate) {
		double movement = reverseTrigger - forwardTrigger;
		squaredRotate *= 0.50;
		double bothJoysticks = squaredRotate + rotate;
		drive.arcadeDrive(movement, bothJoysticks);
	}

	public void arcadeDrive(double forward, double rotate) {
		drive.arcadeDrive(forward, rotate);
	}

	public void tankDrive(double leftValue, double rightValue) {
		drive.tankDrive(leftValue, rightValue);
	}
	
	public void stopMotors() {
		leftTalon.set(0);
		rightTalon.set(0);
	}

	public void sensorReset() {
		L_Encoder.reset();
		R_Encoder.reset();
	}

	public void getSensors() {
		SmartDashboard.putNumber("Right Encoder Distance", -this.R_Encoder.getDistance());
		SmartDashboard.putNumber("Left Encoder Distance", this.L_Encoder.getDistance());
		SmartDashboard.putNumber("Right Encoder Rate", -this.R_Encoder.getRate());
		SmartDashboard.putNumber("Left Encoder Rate", this.L_Encoder.getRate());
		SmartDashboard.putNumber("Right Rotations", (-this.R_Encoder.getDistance() / 2085));
		SmartDashboard.putNumber("Left Rotations", (this.L_Encoder.getDistance() / 2085));
	}

	public void highGear() {
		shifter.set(Value.kReverse);
		state = ShifterState.SF_HIGH;
	}

	public void lowGear() {
		shifter.set(Value.kForward);
		state = ShifterState.SF_LOW;
	}

	public void set(ShifterState state) {
		switch (state) {
		case SF_HIGH:
			this.highGear();
			break;

		case SF_LOW:
			this.lowGear();
			break;
		}
	}

	public ShifterState getState() {
		return state;
	}
}
