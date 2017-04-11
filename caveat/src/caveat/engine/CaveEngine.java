package caveat.engine;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import caveat.engine.util.CaveBuilder;
import caveat.engine.util.CaveCleaner;
import caveat.engine.util.CaveCollider;
import caveat.engine.util.CaveDrawer;
import caveat.engine.util.CaveExploder;
import caveat.engine.util.CaveInput;
import caveat.engine.util.CaveLighter;
import caveat.engine.util.CaveMover;
import caveat.engine.util.CaveSound;
import caveat.engine.util.CaveVision;
import caveat.object.Cave;
import caveat.object.CaveCharacter;
import caveat.object.CaveExplosive;
import caveat.object.CaveItem;
import caveat.object.CaveObject;
import caveat.object.CavePickaxe;
import caveat.object.character.CaveCharacterInventory;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveEngine extends Canvas {

	private static final long serialVersionUID = 1L;

	private Cave cave;

	private CaveBuilder caveBuilder;

	private CaveLighter caveLighter;

	private CaveMover caveMover;

	private CaveExploder caveExploder;

	private CaveVision caveVision;

	private CaveDrawer caveDrawer;

	private CaveInput caveInput;

	private BufferStrategy strategy;

	private CaveCleaner caveCleaner;

	private CaveCollider caveCollider;

	private boolean currentNoticed = false;

	private double avgFps = 0;

	public CaveEngine() throws Exception {
		init();
		initEngine();
	}

	public void start() throws Exception {
		long lastLoopTime = System.currentTimeMillis();

		Integer hitSoundCounter = 0;
		int walkCounter = CaveDefaults.PLAYER_WALK_SOUND_INTERVAL;

		cave.setPaused(true);
		cave.setRestartCounter(-1000);
		while (true) {
			// cave.setFOV(false);
			// cave.setCaveSizeMultiplier(.2);

			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			avgFps = (1000 / (delta + 1) * .05 + avgFps * .95);
			if (delta > CaveDefaults.MAX_DELTA) {
				delta = CaveDefaults.MAX_DELTA;
			}
			delta = (long) (delta*1.2); // TODO temp speed hack

			CaveCharacter playerCharacter = cave.getPlayerCharacter();
			double initialHealth = playerCharacter.getHealth();

			if (cave.isPaused()) {
				drawHelp((int) delta);
				walkCounter = playWalkSound(walkCounter, (long) (delta / (2 + Math.random())), playerCharacter);
			} else {
				if (cave.isInitialised()) {
					cave.updateCurrentCaveObjects();
					caveVision.setVisibility(cave);
					if (cave.getCaveSizeMultiplier() >= 1) {
						caveMover.move(cave, delta);
						caveExploder.handleExplosives(cave, delta);
						caveCleaner.clean(cave, delta);
						caveCollider.collisionPositions = new ArrayList<CavePosition>();
						caveCollider.checkCollisions(cave, delta);
					}
					caveLighter.light(cave);
					caveDrawer.draw(cave, strategy.getDrawGraphics(), delta, avgFps);
//					caveDrawer.drawCollisionPositions(caveCollider.collisionPositions, strategy.getDrawGraphics(), cave);
					strategy.show();
					if (delta < CaveDefaults.MAX_DELTA) {
						Thread.sleep(CaveDefaults.MAX_DELTA - delta);
					}
					playerCharacter.reduceFunctionTimer(delta);

					handlePlayerDamage(hitSoundCounter, delta, playerCharacter, initialHealth);

					walkCounter = playWalkSound(walkCounter, delta, playerCharacter);

					handleGoblinGrowl();
				}
				handleEndings(delta);

			}

			if (cave.getRestartCounter() < 0) {
				handleKeyPresses(delta);
			}

		}
	}

	private void handlePlayerDamage(Integer hitSoundCounter, long delta, CaveCharacter playerCharacter, double initialHealth) {
		double currentHealth = playerCharacter.getHealth();
		if (initialHealth - currentHealth > 0 && hitSoundCounter == 0) {
			playerCharacter.playSound(CaveSound.DAMAGE);
			hitSoundCounter = (int) (delta);
		}
		if (hitSoundCounter > 0) {
			hitSoundCounter--;
		}
	}

	private int playWalkSound(int walkCounter, long delta, CaveCharacter playerCharacter) {
		if (walkCounter > CaveDefaults.PLAYER_WALK_SOUND_INTERVAL) {
			int random = (int) (Math.random() * 2);
			switch (random) {
			case 0:
				playerCharacter.playSound(CaveSound.MOVE);
				break;
			case 1:
				playerCharacter.playSound(CaveSound.ALT_MOVE1);
				break;
			default:
				playerCharacter.playSound(CaveSound.MOVE);
				break;
			}
			walkCounter = 0;
		}
		if (playerCharacter.getCaveMove().getSpeed() > 0 || cave.isPaused()) {
			walkCounter += delta;
		} else {
			walkCounter = CaveDefaults.PLAYER_WALK_SOUND_INTERVAL;
		}

		return walkCounter;
	}

	private void handleGoblinGrowl() {
		boolean hasNoticed = false;
		ArrayList<CaveObject> caveObjects = cave.getCaveObjects(CaveObjectType.CHARACTER);
		for (CaveObject caveObject : caveObjects) {
			if (!caveObject.equals(cave.getPlayerCharacter())) {
				if (((CaveCharacter) caveObject).hasNoticed()) {
					hasNoticed = true;
					break;
				}
			}
		}
		if (hasNoticed && !currentNoticed) {
			cave.loopSound(CaveSound.AMBIENT);
			currentNoticed = true;
		}
		if (!hasNoticed) {
			cave.stopSound(CaveSound.AMBIENT);
			currentNoticed = false;
		}
	}

	private void handleEndings(long delta) throws InterruptedException, Exception {
		if (cave.getRestartCounter() > 0) {
			cave.setRestartCounter((int) (cave.getRestartCounter() - delta));
		} else if (cave.getRestartCounter() <= 0 && cave.getRestartCounter() != -1000) {
			currentNoticed = false;
			cave.stopSound(CaveSound.AMBIENT);
			drawHelp(500);
			init();
			cave.setPaused(true);
			cave.setRestartCounter(-1000);
		}
		if ((cave.getAllCaveObjects(CaveObjectType.CHARACTER).size()) == 1 && !cave.isPaused()) {
			caveBuilder.createMonsters(cave);
		}
	}

	private void init() throws Exception {
		initCave();
		initDrawer();
		initInput();
		caveBuilder.fill(cave, 0);
	}

	private void startGame() {
		long startTime = System.currentTimeMillis();
		drawLoadingPhase("Player character", 0, startTime);
		caveBuilder.fill(cave, 1);
		drawLoadingPhase("Monsters", 1, startTime);
		caveBuilder.fill(cave, 2);
		drawLoadingPhase("Walls", 2, startTime);
		caveBuilder.fill(cave, 3);
		drawLoadingPhase("Wall torches", 3, startTime);
		caveBuilder.fill(cave, 4);
		drawLoadingPhase("More torches", 4, startTime);
		caveBuilder.fill(cave, 5);
		drawLoadingPhase("Cave ground", 5, startTime);
		caveBuilder.fill(cave, 6);
		drawLoadingPhase("Images", 6, startTime);
		caveDrawer.loadImages();
		drawLoadingPhase("Physics", 7, startTime);
		initMover();
		initExploder();
		initCleaner();
		initCollisioner();
		initLighter();
		initVision();
		cave.setInitialised(true);
	}

	private void drawLoadingPhase(String name, int number, long startTime) {
		Graphics graphics = strategy.getDrawGraphics();
		caveDrawer.drawLoading(graphics, name, number, System.currentTimeMillis() - startTime);
		strategy.show();
		graphics.dispose();
	}

	private void initMover() {
		caveMover = new CaveMover();
	}

	private void initExploder() {
		caveExploder = new CaveExploder();
	}

	private void initCleaner() {
		caveCleaner = new CaveCleaner();
	}

	private void initCollisioner() {
		caveCollider = new CaveCollider();
	}

	private void initEngine() {
		addKeyListener(caveInput);
		JFrame container = new JFrame("Cave Adventure 2");
		container.setLocation(0, 0);
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(CaveDefaults.WINDOW_X_SIZE, CaveDefaults.WINDOW_Y_SIZE));
		panel.setLayout(null);
		setBounds(0, 0, CaveDefaults.WINDOW_X_SIZE + 20, CaveDefaults.WINDOW_Y_SIZE + 20);
		panel.add(this);

		setIgnoreRepaint(true);

		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}

	private void initInput() {
		caveInput = new CaveInput();
		addKeyListener(caveInput);
		setFocusable(true);
	}

	private void initDrawer() {
		caveDrawer = new CaveDrawer();
	}

	private void initLighter() {
		caveLighter = new CaveLighter();
	}

	private void initVision() {
		caveVision = new CaveVision();
	}

	private void initCave() {
		caveBuilder = new CaveBuilder();
		cave = caveBuilder.build();
	}

	private void handleKeyPresses(long delta) {
		CaveMove caveMove = cave.getPlayerCharacter().getCaveMove();
		CaveCharacter character = ((CaveCharacter) cave.getPlayerCharacter());
		CavePosition playerPosition = character.getCavePosition();
		double playerSpeed = caveMove.getSpeed();

		if (caveInput.isKeyPressed(KeyEvent.VK_LEFT)) {
			caveMove.setDirection(caveMove.getDirection() - CaveDefaults.PLAYER_TURN_SPEED * delta / 1000);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_RIGHT)) {
			caveMove.setDirection(caveMove.getDirection() + CaveDefaults.PLAYER_TURN_SPEED * delta / 1000);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_UP)) {
			if (playerSpeed < CaveDefaults.PLAYER_SPEED) {
				caveMove.setSpeed(playerSpeed + CaveDefaults.PLAYER_ACCELERATION);
			}
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_END)) {
			caveMove.setSpeed(CaveDefaults.PLAYER_SPEED * 10);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_DOWN)) {
			if (playerSpeed > -CaveDefaults.PLAYER_SPEED / 2) {
				caveMove.setSpeed(playerSpeed - CaveDefaults.PLAYER_ACCELERATION);
			}
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_Q) || caveInput.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			System.exit(0);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_H) || caveInput.isKeyPressed(KeyEvent.VK_P)) {
			cave.setPaused(true);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_ENTER)) {
			cave.setPaused(false);
			if (cave.getCaveObjects().size() <= 1) {
				startGame();
			}
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_SHIFT)) {
			handlePickaxeHit(character);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_CONTROL)) {
			handleExplosiveSetting(character, playerPosition);
		}

		// TESTING KEYS
		if (caveInput.isKeyPressed(KeyEvent.VK_R)) {
			currentNoticed = true;
			cave.stopSound(CaveSound.AMBIENT);
			cave.setRestartCounter(0);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_S)) {
			currentNoticed = true;
			cave.stopSound(CaveSound.AMBIENT);
		}
		if (cave.getPlayerCharacter().getFunctionTimer() <= 0) {
			if (caveInput.isKeyPressed(KeyEvent.VK_F1)) {
				cave.setFOV(true);
				cave.getPlayerCharacter().setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
			}
			if (caveInput.isKeyPressed(KeyEvent.VK_F2)) {
				cave.setFOV(false);
				cave.setCaveSizeMultiplier(.2);
				cave.getPlayerCharacter().setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
			}
			if (caveInput.isKeyPressed(KeyEvent.VK_F3)) {
				caveBuilder.createMonsters(cave);
				cave.getPlayerCharacter().setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
			}
			if (caveInput.isKeyPressed(KeyEvent.VK_F4)) {
				// caveBuilder.createWalls(cave);
				cave.getPlayerCharacter().setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
			}
			if (caveInput.isKeyPressed(KeyEvent.VK_F5)) {
				for (int i = 0; i < CaveDefaults.INITIAL_EXPLOSIVE_AMOUNT; i++)
					cave.getPlayerCharacter().getCaveCharacterInventory().add(new CaveExplosive());
				cave.getPlayerCharacter().setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
			}
			if (caveInput.isKeyPressed(KeyEvent.VK_F6)) {
				caveBuilder.createFreeLights(cave);
				cave.getPlayerCharacter().setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
			}
			if (caveInput.isKeyPressed(KeyEvent.VK_F7)) {
				caveBuilder.createWallLights(cave);
				cave.getPlayerCharacter().setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
			}
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_PAGE_DOWN)) {
			if (cave.getCaveSizeMultiplier() < 2.5) {
				cave.setCaveSizeMultiplier(cave.getCaveSizeMultiplier() + .1);
			}
			if (!cave.isFOV() && cave.getCaveSizeMultiplier() >= 1) {
				cave.setFOV(true);
			}
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_PAGE_UP)) {
			if (cave.getCaveSizeMultiplier() >= .3) {
				cave.setCaveSizeMultiplier(cave.getCaveSizeMultiplier() - .1);
			}
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_HOME)) {
			cave.setCaveSizeMultiplier(1);
			cave.setFOV(true);
		}
		if (cave.getAllCaveObjects().size() != 1) {
			CaveItem torch = cave.getTorch();
			if (torch != null) {
				if (caveInput.isKeyPressed(KeyEvent.VK_INSERT)) {
					torch.setCounter(CaveDefaults.PLAYER_LIGHT_MAX_COUNTER);
				}
				if (caveInput.isKeyPressed(KeyEvent.VK_DELETE)) {
					torch.setCounter(0);
				}
			}
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_Z)) {
			caveBuilder.monsterAmount -= 10;
			caveBuilder.monsterAmount = caveInput.validAmount(caveBuilder.monsterAmount);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_X)) {
			caveBuilder.monsterAmount += 10;
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_F)) {
			caveBuilder.wallAmount -= 1;
			caveBuilder.wallAmount = caveInput.validAmount(caveBuilder.wallAmount);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_G)) {
			caveBuilder.wallAmount += 1;
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_J)) {
			caveBuilder.wallLength -= 100;
			caveBuilder.wallLength = caveInput.validAmount(caveBuilder.wallLength);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_K)) {
			caveBuilder.wallLength += 100;
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_C)) {
			caveBuilder.lightAmount -= 10;
			caveBuilder.lightAmount = caveInput.validAmount(caveBuilder.lightAmount);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_V)) {
			caveBuilder.lightAmount += 10;
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_B)) {
			caveBuilder.groundAmount -= 300;
			caveBuilder.groundAmount = caveInput.validAmount(caveBuilder.groundAmount);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_N)) {
			caveBuilder.groundAmount += 300;
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_T)) {
			caveBuilder.explosiveAmount -= 10;
			caveBuilder.explosiveAmount = caveInput.validAmount(caveBuilder.explosiveAmount);
		}
		if (caveInput.isKeyPressed(KeyEvent.VK_Y)) {
			caveBuilder.explosiveAmount += 10;
			if (caveBuilder.explosiveAmount > 99) {
				caveBuilder.explosiveAmount = 99;
			}
		}
	}

	private void handleExplosiveSetting(CaveCharacter character, CavePosition playerPosition) {
		if (character.getFunctionTimer() < 0) {
			CaveExplosive explosive = null;
			CaveCharacterInventory inventory = character.getCaveCharacterInventory();
			boolean explosiveFound = false;
			for (CaveObject item : inventory.getItems()) {
				if (item instanceof CaveExplosive) {
					explosive = (CaveExplosive) item;
					explosiveFound = true;
				}
			}
			if (explosiveFound) {
				explosive.setCavePosition(CavePosition.createPosition(playerPosition));
				explosive.setCaveMove(new CaveMove(0, 0));
				explosive.setSound(CaveSound.EXPLOSION, CaveDefaults.EXPLOSIVE_SOUND_FILENAME);
				explosive.setSound(CaveSound.COUNTER, CaveDefaults.EXPLOSIVE_COUNTER_SOUND_FILENAME);
				explosive.playSound(CaveSound.COUNTER);
				cave.addCaveObject(explosive);
				character.setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
				inventory.remove(explosive);
			} else {
				character.playSound(CaveSound.MOVE);
			}
		}
	}

	private void handlePickaxeHit(CaveCharacter character) {
		if (character.getFunctionTimer() < 0) {
			CavePickaxe pickaxe = new CavePickaxe(character.getCavePosition(), character.getCaveMove(), CaveDefaults.PICKAXE_SIZE, CaveDefaults.PLAYER_FUNCTION_DELAY);
			CaveObject goal = null;
			goal = character.getHitGoal(cave);
			if (goal != null) {
				if (goal.getObjectType().equals(CaveDefaults.CAVEFILLEDWALL)) {
					CavePosition hitPosition = CaveCollider.getIntersectionPosition(goal, pickaxe);
					pickaxe.createDestructionParticles(hitPosition, CaveDefaults.OBJECT_HIT_PARTICLE_COUNT, cave);
					pickaxe.setHitTargetPosition(hitPosition);
				}
				// if (pickaxe.getHitTargetPosition() == null) {
				// pickaxe.setHitTargetPosition(goal.getCavePosition());
				// }
				character.playSound(CaveSound.HIT);
				character.setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
				goal.reduceHealth(CaveDefaults.PICKAXE_DAMAGE + Math.random() * CaveDefaults.PICKAXE_RANDOM_DAMAGE);
				// goal.createDestructionParticles(CaveDefaults.OBJECT_HIT_PARTICLE_COUNT, cave);
			} else {
				character.playSound(CaveSound.MISS);
				character.setFunctionTimer(CaveDefaults.PLAYER_FUNCTION_DELAY);
			}
			cave.addCaveObject(pickaxe);
		}
	}

	private void drawHelp(int timeToShow) {
		Graphics graphics = strategy.getDrawGraphics();
		caveDrawer.drawHelp(caveBuilder, graphics);
		strategy.show();
		graphics.dispose();
		try {
			Thread.sleep(timeToShow);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
