package caveat.object;

import java.awt.Graphics2D;

import caveat.engine.CaveDefaults;
import caveat.engine.util.CaveSound;
import caveat.object.type.CaveItemFeature;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveLight extends CaveItem {

	public CaveLight(CavePosition cavePosition, CaveMove caveMove, int weight) {
		super(cavePosition, caveMove);
		setWeight(weight);
	}

	public CaveLight(CavePosition cavePosition, CaveMove caveMove) {
		super(cavePosition, caveMove);
		setColor(CaveDefaults.LIGHT_COLOR);
		setParticleColor(CaveDefaults.LIGHT_COLOR);
		setSound(CaveSound.DESTRUCTION, CaveDefaults.LIGHT_DESTRUCTION_SOUND);
		setSound(CaveSound.DAMAGE, CaveDefaults.LIGHT_DAMAGE_SOUND);
		setHealth(CaveDefaults.LIGHT_STRENGHT);
		setSize(CaveDefaults.LIGHT_SIZE);
	}

	@Override
	public void draw(Graphics2D g2d, Cave cave, long delta) {
		if (hasFeature(CaveItemFeature.TORCH)) {
			drawTorch(g2d, cave, getCavePosition());
		} else {
			drawFilledCircle(getCavePosition(), getSize(), g2d, cave);
		}

		// g2d.setColor(CaveDefaults.FONT_COLOR);
		// int distance = CavePosition.distance(getCavePosition(), cave.getPlayerCharacter().getCavePosition());
		// g2d.drawString("light "+getLight(), (int)getCavePosition().getX(), (int)getCavePosition().getY()-30);
		// g2d.drawString("distance "+distance, (int)getCavePosition().getX(), (int)getCavePosition().getY()-40);
		// g2d.drawString("brightness "+getBrightnessChange(distance, getFeature(CaveItemFeature.LIGHT), CaveDefaults.LIGHT_MAX_BRIGHTNESS), (int)getCavePosition().getX(),
		// (int)getCavePosition().getY()+30);
		// g2d.drawString("dim " + ((Math.pow(distance,CaveDefaults.LIGHT_BRIGHTNESS_EXPONENTIAL) / Math.pow(CaveDefaults.VIEW_SIZE,CaveDefaults.LIGHT_BRIGHTNESS_EXPONENTIAL))*getLight()),
		// (int)getCavePosition().getX(), (int)getCavePosition().getY()+40);
	}

	private void drawTorch(Graphics2D g2d, Cave cave, CavePosition position) {
		int lightMaxCounter = CaveDefaults.LIGHT_DEPLETED_COUNTER;
		if (hasFeature(CaveItemFeature.PLAYER_ITEM)) {
			lightMaxCounter = CaveDefaults.PLAYER_LIGHT_MAX_COUNTER;
		} else {
			position = position.add(new CavePosition(0, 5));
		}
		double torchPercentage = Double.valueOf(getCounter()) / lightMaxCounter;
		if (getCounter() == -1000) {
			torchPercentage = 1;
		}
		applyLightToObjectColor(g2d);
		int torchSize = 3;
		int flameRandomSize = 3;
		int flameSize = 1;
		int inventoryMultiplier = CaveDefaults.PLAYER_INVENTORY_VIEW_MULTIPLIER * 2;
		for (int i = 0; i < torchPercentage * 3; i++) {
			double flameYPosition = ((flameSize + flameRandomSize * Math.random()) * torchPercentage);
			drawLine(position.add(new CavePosition(0, -torchSize)), position.add(new CavePosition((Math.random() - .5) * flameRandomSize / 2, -torchSize - flameYPosition)), g2d, cave);
			if (hasFeature(CaveItemFeature.PLAYER_ITEM)) {
				g2d.drawLine(CaveDefaults.PLAYER_INVENTORY_X, CaveDefaults.WINDOW_PLAYER_POSITION, (int) (CaveDefaults.PLAYER_INVENTORY_X + (Math.random() - .5) * flameRandomSize * 2),
						(int) (CaveDefaults.WINDOW_PLAYER_POSITION - (flameYPosition - flameSize) * torchPercentage * inventoryMultiplier) - 10);
				g2d.drawLine(CaveDefaults.PLAYER_INVENTORY_X, CaveDefaults.WINDOW_PLAYER_POSITION, (int) (CaveDefaults.PLAYER_INVENTORY_X + (Math.random() - .5) * flameRandomSize * 2),
						(int) (CaveDefaults.WINDOW_PLAYER_POSITION - ((Math.random())) * (flameYPosition - flameSize) * torchPercentage * inventoryMultiplier) - 10);
			}
		}
		if (hasFeature(CaveItemFeature.PLAYER_ITEM)) {
			g2d.setColor(CaveDefaults.FONT_COLOR);
			g2d.drawLine(CaveDefaults.PLAYER_INVENTORY_X + 5, CaveDefaults.WINDOW_PLAYER_POSITION - 10, CaveDefaults.PLAYER_INVENTORY_X + 5,
					(int) (CaveDefaults.WINDOW_PLAYER_POSITION - (flameRandomSize) * inventoryMultiplier) - 10);
			int torchGauge = (int) (CaveDefaults.WINDOW_PLAYER_POSITION - (flameRandomSize * torchPercentage * inventoryMultiplier) - 10);
			g2d.drawLine(CaveDefaults.PLAYER_INVENTORY_X + 5, torchGauge, CaveDefaults.PLAYER_INVENTORY_X, torchGauge);
			// g2d.drawString(String.format("%.0f", torchPercentage*100), 910, CaveDefaults.WINDOW_PLAYER_POSITION);
		}
		// g2d.setColor(Color.darkGray);
		drawLine(position, position.add(new CavePosition(0, -torchSize)), g2d, cave);
	}

	@Override
	public void createDestructionParticles(double amount, Cave cave) {
		super.createDestructionParticles(amount, cave);
		for (int i = 0; i < amount; i++) {
			CaveLight light = new CaveLight(CavePosition.createPosition(getCenterPosition(cave)), new CaveMove(Math.random() * CaveDefaults.OBJECT_DESTRUCTION_PARTICLE_RANDOM_SPEED
					+ CaveDefaults.OBJECT_DESTRUCTION_PARTICLE_MINIMUM_SPEED, Math.random() * Math.PI * 2));
			light.setColor(getParticleColor());
			light.setCollidable(false);
			light.setIndestructible(true);
			light.setFeature(CaveItemFeature.LIGHT, CaveDefaults.LIGHT_PARTICLE_BRIGHTNESS);
			light.setCounter((int) (CaveDefaults.PARTICLE_LIFETIME + Math.random() * CaveDefaults.PARTICLE_LIFETIME_RANDOM));
			light.setSize(1);
			cave.addCaveObject(light);
		}
	}

}
