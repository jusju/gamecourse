package caveat.object;

import java.awt.Graphics2D;
import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.engine.util.CaveSound;
import caveat.object.type.CaveItemFeature;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveExplosive extends CaveItem {

	private int power;

	public CaveExplosive(CavePosition position, CaveMove move, int size, int counter, int power, int weight) {
		super(position, move, size, weight, counter);
		this.power = power;
		setFeature(CaveItemFeature.EXPLOSIVE, power);
		setColor(CaveDefaults.EXPLOSIVE_PARTICLE_COLOR);
		setSound(CaveSound.EXPLOSION, CaveDefaults.EXPLOSIVE_SOUND_FILENAME);
		setIndestructible(true);
	}

	public CaveExplosive() {
		this(null,null,CaveDefaults.EXPLOSIVE_SIZE,CaveDefaults.MINIMUM_EXPLOSIVE_DELAY,CaveDefaults.EXPLOSIVE_POWER, CaveDefaults.EXPLOSIVE_WEIGHT);
	}

	@Override
	public void draw(Graphics2D g2d, Cave cave, long delta) {
		CavePosition cavePosition = getCavePosition();
		super.draw(g2d, cave, delta);
		drawFilledCircle(cavePosition, getSize(), g2d, cave);
		int counter = (int) (getCounter()/(CaveDefaults.MINIMUM_EXPLOSIVE_DELAY*CaveDefaults.EXPLOSIVE_FUSE_MULTIPLIER));
		if (counter < 0) {
			counter = 0;
		}
		CavePosition fuseStart = getCavePosition().add(new CavePosition(0, (int)-getSize()/2));
		CavePosition fuseEnd = getCavePosition().add(new CavePosition(0, (int)-counter-getSize()/2));
		drawLine(fuseStart, fuseEnd, g2d, cave);
		g2d.setColor(CaveDefaults.SPARKLE_COLOR);
		for(int i=0; i<CaveDefaults.EXPLOSIVE_SPARKLE_AMOUNT; i++) {
			int randX = (int) ((Math.random()-.5)*CaveDefaults.EXPLOSIVE_SPARKLE_RADIUS);
			int randY = (int) ((Math.random()-.5)*CaveDefaults.EXPLOSIVE_SPARKLE_RADIUS);
			CavePosition sparklePosition = fuseEnd.add(new CavePosition(randX, randY));
			drawLine(sparklePosition, sparklePosition, g2d, cave);
		}
	}
	
	/**
	 * For inventory
	 */
	public void drawFixedPosition(Graphics2D g2d, Cave cave) {
		CavePosition cavePosition = getCavePosition();
		g2d.fillOval((int)cavePosition.getX()-getSize()/2, (int)cavePosition.getY()-getSize()/2, getSize(), getSize());
		int counter = getCounter()/(CaveDefaults.MINIMUM_EXPLOSIVE_DELAY/5);
		if (counter < 0) {
			counter = 0;
		}
		CavePosition fuseStart = getCavePosition().add(new CavePosition(0, (int)-getSize()/2));
		CavePosition fuseEnd = getCavePosition().add(new CavePosition(0, (int)-counter-getSize()/2));
		g2d.drawLine((int)fuseStart.getX(), (int)fuseStart.getY(), (int)fuseEnd.getX(), (int)fuseEnd.getY());
		g2d.setColor(CaveDefaults.SPARKLE_COLOR);
		for(int i=0; i<CaveDefaults.EXPLOSIVE_SPARKLE_AMOUNT; i++) {
			int randX = (int) ((Math.random()-.5)*CaveDefaults.EXPLOSIVE_SPARKLE_RADIUS);
			int randY = (int) ((Math.random()-.5)*CaveDefaults.EXPLOSIVE_SPARKLE_RADIUS);
			CavePosition sparklePosition = fuseEnd.add(new CavePosition(randX, randY));
			g2d.drawLine((int)sparklePosition.getX(), (int)sparklePosition.getY(), (int)sparklePosition.getX(), (int)sparklePosition.getY());
		}
	}
	
	public int getPower() {
		return this.power;
	}
	
	@Override
	public void createDestructionParticles(double amount, Cave cave) {
		ArrayList<CaveObject> explosionParticles = createParticles(cave, amount);
		explosionParticles.addAll(createLightParticles(amount, cave));
		for (CaveObject particle: explosionParticles) {
			particle.setCaveMove(calculateParticleMove(particle));
			cave.addCaveObject(particle);
		}
	}

	public ArrayList<CaveObject> createParticles(Cave cave, double amount) {
		ArrayList<CaveObject> particles = new ArrayList<CaveObject>();
		for (int i=0; i<amount*CaveDefaults.EXPLOSIVE_PARTICLE_MULTIPLIER ; i++) {
			CaveParticle caveParticle = new CaveParticle(getCenterPosition(cave), new CaveMove(0,0), CaveDefaults.PARTICLE_SIZE, CaveDefaults.EXPLOSIVE_PARTICLE_WEIGHT, (int)(CaveDefaults.PARTICLE_LIFETIME+Math.random()*CaveDefaults.PARTICLE_LIFETIME_RANDOM));
			caveParticle.setColor(CaveDefaults.EXPLOSIVE_PARTICLE_COLOR);
			caveParticle.setCollidable(true);
			if (getColor() != null) {
				caveParticle.setColor(getColor());
			}
			caveParticle.setCaveMove(calculateParticleMove(caveParticle));
			particles.add(caveParticle);
		}
		return particles;
	}

	private ArrayList<CaveObject> createLightParticles(double amount, Cave cave) {
		ArrayList<CaveObject> lightParticles = new ArrayList<CaveObject>();
		for (int i=0; i<amount; i++) {
			CaveLight light = new CaveLight(CavePosition.createPosition(getCenterPosition(cave)), new CaveMove(Math.random()*CaveDefaults.EXPLOSIVE_PARTICLE_RANDOM_SPEED+CaveDefaults.EXPLOSIVE_PARTICLE_MINIMUM_SPEED,Math.random()*Math.PI*2), CaveDefaults.EXPLOSIVE_PARTICLE_WEIGHT);
			light.setColor(getParticleColor());
			light.setCollidable(false);
			light.setFeature(CaveItemFeature.LIGHT, CaveDefaults.LIGHT_PARTICLE_BRIGHTNESS);
			light.setCounter((int)(CaveDefaults.GLOWING_PARTICLE_LIFETIME));//+Math.random()*CaveDefaults.PARTICLE_LIFETIME_RANDOM));
			light.setSize(1);
			light.setWeight((int) (CaveDefaults.EXPLOSIVE_PARTICLE_WEIGHT));
			light.setIndestructible(true);
			lightParticles.add(light);
		}
		return lightParticles;
	}
	
	private CaveMove calculateParticleMove(CaveObject caveParticle) {
		double powerWeightRatio = calculatePowerToWeightRatio(caveParticle);
		double randomDirection = Math.random()*Math.PI*2;
		return new CaveMove(Math.random() * powerWeightRatio * CaveDefaults.EXPLOSIVE_PARTICLE_RANDOM_SPEED + CaveDefaults.EXPLOSIVE_PARTICLE_MINIMUM_SPEED, randomDirection);
	}

	private double calculatePowerToWeightRatio(CaveObject caveParticle) {
		return Double.valueOf(getPower()) / (caveParticle.getWeight());
	}


}

