package caveat.object;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Random;

import caveat.engine.CaveDefaults;
import caveat.engine.util.CaveSound;
import caveat.engine.util.CaveSoundBank;
import caveat.object.type.CaveItemFeature;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveObject {

	private CavePosition cavePosition;

	private CaveMove caveMove;

	private int light = 0;

	private int size = CaveDefaults.ITEM_SIZE;

	private int weight = CaveDefaults.ITEM_WEIGHT;

	private boolean visible = true;

	private boolean removable = false;

	private boolean collidable = true;

	private boolean indestructible = false;

	private double health;

	private Color color = null;

	private CaveImage image;

	HashMap<CaveSound, AudioClip> sounds;

	private Color particleColor;

	private boolean update;

	private String objectType;

	public CaveObject() {
		setHealth(CaveDefaults.OBJECT_HEALTH);
		setObjectType(getClass().getSimpleName());
		sounds = new HashMap<CaveSound, AudioClip>();
	}

	public CaveObject(CavePosition cavePosition, CaveMove caveMove) {
		this();
		setCavePosition(cavePosition);
		setCaveMove(caveMove);
	}

	public void setCavePosition(CavePosition cavePosition) {
		this.cavePosition = cavePosition;
	}

	public CavePosition getCavePosition() {
		return cavePosition;
	}

	public void setCaveMove(CaveMove caveMove) {
		this.caveMove = caveMove;
	}

	public CaveMove getCaveMove() {
		return caveMove;
	}

	public CavePosition getRelativePositionForMove(double multiplier, long delta) {
		CavePosition position = new CavePosition(0, 0);
		if (getCavePosition() != null && getCaveMove() != null) {
			position.setUncheckedX(Math.cos(getCaveMove().getDirection()) * getCaveMove().getSpeed() * multiplier * delta / 1000);
			position.setUncheckedY(Math.sin(getCaveMove().getDirection()) * getCaveMove().getSpeed() * multiplier * delta / 1000);
		}
		return position;
	}

	public CavePosition getRelativeDisplacementForSize() {
		CavePosition position = new CavePosition(0, 0);
		if (getCavePosition() != null && getCaveMove() != null) {
			double xDelta = Math.cos(Double.valueOf(getCaveMove().getDirection()));
			double yDelta = Math.sin(Double.valueOf(getCaveMove().getDirection()));
			position.setUncheckedX(xDelta * getSize() / 2);
			position.setUncheckedY(yDelta * getSize() / 2);
		}
		return position;
	}

	public CavePosition getRelativeDisplacementForSizeWithAngle(double angle) {
		CavePosition position = new CavePosition(0, 0);
		if (getCavePosition() != null && getCaveMove() != null) {
			double direction = CaveMove.correctDirection(getCaveMove().getDirection() + angle);
			position.setUncheckedX(Math.cos(direction) * getSize() / 2);
			position.setUncheckedY(Math.sin(direction) * getSize() / 2);
		}
		return position;
	}

	public CavePosition getRelativeDisplacementWithDistance(double distance) {
		CavePosition position = new CavePosition(0, 0);
		if (getCavePosition() != null && getCaveMove() != null) {
			position.setUncheckedX(Math.cos(getCaveMove().getDirection()) * distance);
			position.setUncheckedY(Math.sin(getCaveMove().getDirection()) * distance);
		}
		return position;
	}

	public void setVisible(boolean isVisible) {
		this.visible = isVisible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void resetLight() {
		this.light = 0;
	}

	public int getLight() {
		return light;
	}

	public void addBrightness(int amount) {
		light += amount;
		if (light > CaveDefaults.OBJECT_MAX_LIGHT) {
			light = CaveDefaults.OBJECT_MAX_LIGHT;
		}
	}

	public void addBrightness(CaveLight caveLight, Cave cave) {
		double distance = CavePosition.accurateDistance(cavePosition, caveLight.getCavePosition());
		Integer brightness = caveLight.getFeature(CaveItemFeature.LIGHT);
		if (distance < brightness) {
			double additionalBrightness = ((brightness - distance) / brightness) * CaveDefaults.LIGHT_MAX_EFFECT_TO_OBJECT_BRIGHTNESS;
			if (caveLight.getCounter() != -1000) {
				if (caveLight.hasFeature(CaveItemFeature.PLAYER_ITEM)) {
					//additionalBrightness -= ((CaveDefaults.PLAYER_LIGHT_MAX_COUNTER - Double.valueOf(caveLight.getCounter())) / CaveDefaults.PLAYER_LIGHT_MAX_COUNTER) * CaveDefaults.LIGHT_MAX_EFFECT_TO_OBJECT_BRIGHTNESS * .5;
				}
				if (additionalBrightness < 0) {
					additionalBrightness = 0;
				}
			}
			light += additionalBrightness;
			if (cave.getRestartCounter() > 0) {
				light -= (CaveDefaults.RESTART_COUNTER - cave.getRestartCounter()) / (CaveDefaults.RESTART_COUNTER / 100);
			}
		}
		if (light > CaveDefaults.OBJECT_MAX_LIGHT) {
			light = CaveDefaults.OBJECT_MAX_LIGHT;
		}
	}

	public void setViewLightLevel(CaveCharacter player) {
		int lightOriginalValue = light;
		double distance = CavePosition.accurateDistance(getCavePosition(), player.getCavePosition());
		// double sqDistance = Math.pow(distance, CaveDefaults.LIGHT_BRIGHTNESS_EXPONENTIAL);
		// double sqDistanceMax = Math.pow(CaveDefaults.VIEW_SIZE, CaveDefaults.LIGHT_BRIGHTNESS_EXPONENTIAL);
		// if (sqDistance < sqDistanceMax) {
		// light -= (sqDistance / sqDistanceMax) * lightOriginalValue;
		double distanceMax = CaveDefaults.VIEW_SIZE;
		if (distance < distanceMax) {
			light -= (distance / distanceMax) * lightOriginalValue;
			if (light < 0) {
				light = 0;
			}
		}
	}

	public void applyLightToObjectColor(Graphics2D g2d) {
		Color litColor = CaveDefaults.AMBIENT_LIGHT_COLOR;
		if (getColor() != null) {
			litColor = getColor();
		}
		int maximumHueChange = CaveDefaults.MAXIMUM_HUE_CHANGE;
		if (this instanceof CaveLight) {
			maximumHueChange = 250;
			addBrightness(getLight());
		}

		if (this instanceof CaveGround) {
			maximumHueChange = 50;
		}

		if (getImage() == null) {
			litColor = calculateObjectLitColor(litColor, maximumHueChange);
		}
		g2d.setColor(litColor);

	}

	public Color calculateObjectLitColor(Color litColor, int maximumHueChange) {
		for (int i = 0; i < getLight(); i++) {
			int color = litColor.getRGB();
			int red = litColor.getRed();
			int green = litColor.getGreen();
			int blue = litColor.getBlue();
			if (green < 255 && red < 255 && blue < 255
					&& green < maximumHueChange && red < maximumHueChange && blue < maximumHueChange) {
				if (blue < 250 && green < maximumHueChange && red < maximumHueChange) {
					color += (CaveDefaults.BLUE);
				}
				if (green < 250 && blue < maximumHueChange && red < maximumHueChange) {
					color += (CaveDefaults.GREEN * 2);
				}
				if (red < 250 && green < maximumHueChange && blue < maximumHueChange) {
					color += (CaveDefaults.RED * 2);
				}
			}
			litColor = new Color(color);
		}
		return litColor;
	}

	public void draw(Graphics2D g2d, Cave cave, long delta) {
		drawSizeCircle(g2d, cave);
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * The "AI"
	 */
	public void setCourse(Cave cave, long delta) {
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public boolean getRemovable() {
		return this.removable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public boolean isCollidable() {
		return this.collidable;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setHealth(double health) {
		if (health < 0) {
			health = 0;
		}
		this.health = health;
	}

	public double getHealth() {
		return health;
	}

	public void reduceHealth(double damage) {
		setHealth(getHealth() - damage);
	}

	public void setIndestructible(boolean indestructible) {
		this.indestructible = indestructible;
	}

	public boolean isIndestructible() {
		return indestructible;
	}

	public Color getParticleColor() {
		return particleColor;
	}

	public void setParticleColor(Color color) {
		this.particleColor = color;
	}

	public void createDestructionParticles(double amount, Cave cave) {
		createDestructionParticles(null, amount, cave);
	}

	public void createDestructionParticles(CavePosition particlePosition, double amount, Cave cave) {
		if (particlePosition == null) {
			particlePosition = CavePosition.createPosition(getCenterPosition(cave));
		}
		for (int i = 0; i < amount; i++) {
			CaveObject particle = new CaveParticle(particlePosition, new CaveMove(Math.random() * CaveDefaults.OBJECT_DESTRUCTION_PARTICLE_RANDOM_SPEED
					+ CaveDefaults.OBJECT_DESTRUCTION_PARTICLE_MINIMUM_SPEED, Math.random() * Math.PI * 2), 1, 1, (int) (CaveDefaults.PARTICLE_LIFETIME + Math.random()
					* CaveDefaults.PARTICLE_LIFETIME_RANDOM));
			particle.setColor(getParticleColor());
			particle.setCollidable(false);
			cave.addCaveObject(particle);
		}
	}

	public void setSound(CaveSound caveSound, String filename) {
		sounds.put(caveSound, null);
		if (filename != null) {
			sounds.put(caveSound, new CaveSoundBank().loadAudioFile(filename));
		}
	}

	public void stopSound(CaveSound caveSound) {
		AudioClip sound = sounds.get(caveSound);
		if (sound != null) {
			sound.stop();
		}
	}

	public void playSound(CaveSound caveSound) {
		AudioClip sound = sounds.get(caveSound);
		if (sound != null) {
			sound.play();
		} else {
			// System.out.println("Sound missing: " + caveSound + " for " +
			// this.getClass().getName());
		}
	}

	protected void drawSizeCircle(Graphics2D g2d, Cave cave) {
		int circleSize = getSize();
		drawCircle(getCavePosition(), circleSize, g2d, cave);
	}

	protected void drawSizeLine(Graphics2D g2d, Cave cave) {
		CavePosition relativeDisplacementForSize = getRelativeDisplacementForSize();
		int startX = (int) (getCavePosition().getX() - relativeDisplacementForSize.getX());
		int startY = (int) (getCavePosition().getY() - relativeDisplacementForSize.getY());
		int endX = (int) (getCavePosition().getX() + relativeDisplacementForSize.getX());
		int endY = (int) (getCavePosition().getY() + relativeDisplacementForSize.getY());
		CavePosition startPosition = new CavePosition(startX, startY);
		CavePosition endPosition = new CavePosition(endX, endY);
		drawLine(startPosition, endPosition, g2d, cave);
	}

	protected void drawRandomTriangle(Graphics2D g2d, Cave cave, int maxSize) {
		Random rnd = new Random((long) (getCavePosition().getX() + getCavePosition().getY()));
		CavePosition randomPosition = CavePosition.randomUncheckedPosition(maxSize, maxSize, rnd, false);
		randomPosition = randomPosition.add(getCavePosition());
		drawLine(getCavePosition().add(getRelativeDisplacementForSize()), randomPosition, g2d, cave);
		drawLine(getCavePosition().add(getRelativeDisplacementForSizeWithAngle(Math.PI)), randomPosition, g2d, cave);
	}

	protected void drawCircle(CavePosition circlePosition, int circleSize, Graphics2D g2d, Cave cave) {
		double caveSizeMultiplier = cave.getCaveSizeMultiplier();
		int x = (int) ((circlePosition.getX() - circleSize / 2) * caveSizeMultiplier);
		int y = (int) ((circlePosition.getY() - circleSize / 2) * caveSizeMultiplier);
		int multipliedCircleSize = (int) (circleSize * caveSizeMultiplier);
		x -= getViewPointXAlteration(cave);
		y -= getViewPointYAlteration(cave);
		g2d.drawOval(x, y, multipliedCircleSize, multipliedCircleSize);
	}

	protected void drawFilledCircle(CavePosition circlePosition, int circleSize, Graphics2D g2d, Cave cave) {
		double caveSizeMultiplier = cave.getCaveSizeMultiplier();
		int x = (int) ((circlePosition.getX() - circleSize / 2) * caveSizeMultiplier);
		int y = (int) ((circlePosition.getY() - circleSize / 2) * caveSizeMultiplier);
		int multipliedCircleSize = (int) (circleSize * caveSizeMultiplier);
		x -= getViewPointXAlteration(cave);
		y -= getViewPointYAlteration(cave);
		g2d.fillOval(x, y, multipliedCircleSize, multipliedCircleSize);
	}

	protected void drawLine(CavePosition cavePosition1, CavePosition cavePosition2, Graphics2D g2d, Cave cave) {
		double caveSizeMultiplier = cave.getCaveSizeMultiplier();
		int startX = (int) (cavePosition1.getX() * caveSizeMultiplier);
		int startY = (int) (cavePosition1.getY() * caveSizeMultiplier);
		int endX = (int) (cavePosition2.getX() * caveSizeMultiplier);
		int endY = (int) (cavePosition2.getY() * caveSizeMultiplier);
		startX -= getViewPointXAlteration(cave);
		startY -= getViewPointYAlteration(cave);
		endX -= getViewPointXAlteration(cave);
		endY -= getViewPointYAlteration(cave);
		g2d.drawLine(startX, startY, endX, endY);
	}

	public static void drawRect(CavePosition cavePosition1, CavePosition cavePosition2, Graphics2D g2d, Cave cave) {
		double sizeMultiplier = cave.getCaveSizeMultiplier();
		int startX = (int) (cavePosition1.getX() * sizeMultiplier);
		int startY = (int) (cavePosition1.getY() * sizeMultiplier);
		int endX = (int) (cavePosition2.getX() * sizeMultiplier);
		int endY = (int) (cavePosition2.getY() * sizeMultiplier);
		startX -= getViewPointXAlteration(cave);
		startY -= getViewPointYAlteration(cave);
		g2d.drawRect(startX, startY, endX, endY);
	}

	public static void drawFilledRect(CavePosition cavePosition1, CavePosition cavePosition2, Graphics2D g2d, Cave cave) {
		double sizeMultiplier = cave.getCaveSizeMultiplier();
		int startX = (int) (cavePosition1.getX() * sizeMultiplier);
		int startY = (int) (cavePosition1.getY() * sizeMultiplier);
		int endX = (int) (cavePosition2.getX() * sizeMultiplier);
		int endY = (int) (cavePosition2.getY() * sizeMultiplier);
		startX -= getViewPointXAlteration(cave);
		startY -= getViewPointYAlteration(cave);
		g2d.fillRect(startX, startY, endX, endY);
	}

	public static double getViewPointXAlteration(Cave cave) {
		return getViewPointAlteration(cave, cave.getPlayerCharacter().getCavePosition().getX());
	}

	public static double getViewPointYAlteration(Cave cave) {
		return getViewPointAlteration(cave, cave.getPlayerCharacter().getCavePosition().getY());
	}

	private static double getViewPointAlteration(Cave cave, double coordinate) {
		return coordinate * cave.getCaveSizeMultiplier() - CaveDefaults.WINDOW_PLAYER_POSITION;
	}

	public CavePosition getCenterPosition(Cave cave) {
		return getCavePosition();
	}

	public double getDirectionAwayFrom(CaveObject caveObject) {
		if (caveObject instanceof CaveWall) {
			return CaveMove.correctDirection(getDirectionToWall(caveObject) + Math.PI);
		}
		return CaveMove.correctDirection(getDirectionTo(caveObject.getCavePosition()) + Math.PI);
	}

	public double getDirectionAwayFrom(CavePosition cavePosition) {
		return CaveMove.correctDirection(getDirectionTo(cavePosition) + Math.PI);
	}

	private double getDirectionToWall(CaveObject wallObject) {
		CavePosition wallPosition = wallObject.getCavePosition();
		CavePosition goalPosition = wallPosition.add(new CavePosition(wallObject.getRelativeDisplacementForSize().getX() / 2,
				wallObject.getRelativeDisplacementForSize().getY() / 2));
		return Math.atan2(goalPosition.getY() - getCavePosition().getY(), goalPosition.getX() - getCavePosition().getX());
	}

	public double getDirectionTo(CavePosition goalPosition) {
		return Math.atan2(goalPosition.getY() - getCavePosition().getY(), goalPosition.getX() - getCavePosition().getX());
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isUpdate() {
		return this.update;
	}

	public void setImage(CaveImage image) {
		this.image = image;
	}

	public CaveImage getImage() {
		return image;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String string) {
		this.objectType = string;
	}

	public void destroy(Cave cave) {
		createDestructionParticles(getSize(), cave);
	}

}
