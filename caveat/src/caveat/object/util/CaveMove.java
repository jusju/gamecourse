package caveat.object.util;

import caveat.engine.CaveDefaults;

public class CaveMove {

	private double speed;
	
	private double direction;

	private double xSpeed;
	
	private double ySpeed;
	
	public CaveMove() {
		speed = 0;
		direction = 0;
		xSpeed = 0;
		ySpeed = 0;
	}

	public CaveMove(double speed, double direction) {
		this(speed,direction,0,0);
	}
	
	public CaveMove(double speed, double direction, double xSpeed, double ySpeed) {
		this.speed = speed;
		this.direction = direction;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public void setDirection(double direction) {
		this.direction = correctDirection(direction);
	}

	public static double correctDirection(double direction) {
		while (direction > Math.PI*2 || direction < 0) {
			if (direction > Math.PI*2)
				direction -= Math.PI*2;
			if (direction < 0)
				direction += Math.PI*2;
		}
		return direction;
	}

	public double getDirection() {
		return direction;
	}

	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	public double getXSpeed() {
		return xSpeed;
	}

	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	public double getYSpeed() {
		return ySpeed;
	}

	public void addXSpeed(double speed) {
		xSpeed += speed;
	}
	
	public void addYSpeed(double speed) {
		ySpeed += speed;
	}

	public static boolean isSimilarDirection(double direction1, double direction2) {
		double degreeOfAccuracy = CaveDefaults.VISION_ACCURACY;
		double directionDifference = Math.abs(direction2 - direction1);
		if (directionDifference < degreeOfAccuracy ) {
			return true;
		}
		directionDifference = Math.abs(direction2+Math.PI*2 - direction1);
		if (directionDifference < degreeOfAccuracy) {
			return true;
		}
		directionDifference = Math.abs(direction1+Math.PI*2 - direction2);
		return directionDifference < degreeOfAccuracy;
	}
	
}
