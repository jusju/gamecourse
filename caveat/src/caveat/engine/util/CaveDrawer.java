package caveat.engine.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveCharacter;
import caveat.object.CaveExplosive;
import caveat.object.CaveImage;
import caveat.object.CaveObject;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveDrawer {

	private BufferedImage[] sprites;

	public void draw(Cave cave, Graphics g, long delta, double avgFps) {
		Graphics2D g2d = init(g);
		clearScreen(g2d);
		drawBoundaries(cave, g2d);
		drawDebugInfo(cave, delta, avgFps, g2d);
		drawPlayerInfo(cave, g2d);
		drawObjects(cave, delta, g2d);
		drawLitHitTarget(cave, g2d, delta);
		g.dispose();
	}

	private void drawObjects(Cave cave, long delta, Graphics2D g2d) {
		Object[] objectArray = (Object[]) cave.getCaveObjects().toArray();
		for (int i = objectArray.length - 1; i >= 0; i--) {
			CaveObject caveObject = (CaveObject) objectArray[i];
			CavePosition cavePosition = caveObject.getCavePosition();
			if (caveObject.getLight() > 0 && (
					caveObject.isVisible()
					&& CavePosition.distance(cavePosition, cave.getPlayerCharacter().getCavePosition()) < CaveDefaults.LIGHT_RADIUS + CaveDefaults.VIEW_SIZE
					) || !cave.isFOV()) {
				caveObject.applyLightToObjectColor(g2d);
				drawObject(caveObject, cavePosition, cave, delta, g2d);
			}
		}
	}

	private void drawPlayerInfo(Cave cave, Graphics2D g2d) {
		int bombAmountYPosition = CaveDefaults.WINDOW_PLAYER_POSITION - 30 - (10 * CaveDefaults.PLAYER_INVENTORY_VIEW_MULTIPLIER);
		CaveExplosive caveExplosive = new CaveExplosive(new CavePosition(CaveDefaults.PLAYER_INVENTORY_X, bombAmountYPosition), new CaveMove(), CaveDefaults.EXPLOSIVE_SIZE
				* CaveDefaults.PLAYER_INVENTORY_VIEW_MULTIPLIER, 2000, 0, 0);
		caveExplosive.drawFixedPosition(g2d, cave);
		int inventoryItemAmount = cave.getPlayerCharacter().getCaveCharacterInventory().getItems().size();
		g2d.drawString("" + inventoryItemAmount, CaveDefaults.PLAYER_INVENTORY_X - 7, bombAmountYPosition + 3);
		drawDamageMeter(g2d, cave);
	}

	private void drawDebugInfo(Cave cave, long delta, double avgFps, Graphics2D g2d) {
		g2d.setColor(CaveDefaults.FONT_COLOR);
		g2d.drawString(String.format("fps avg: %.1f", avgFps), 10, 10);
		if (delta == CaveDefaults.MAX_DELTA) {
			g2d.drawString("!", 100, 10);
		}
		if (1000 / (delta + 1) < avgFps) {
			g2d.drawString("^", 90, 10);
		}
		if (1000 / (delta + 1) > avgFps) {
			g2d.drawString("v", 90, 10);
		}
		// g2d.drawString("health: " + (int)(player.getHealth()),890,CaveDefaults.WINDOW_PLAYER_POSITION - 45);
		g2d.drawString("objects: " + (cave.getCaveObjects().size()), 300, 10);
		g2d.drawString("ground: " + (cave.getCaveObjects(CaveObjectType.GROUND).size()), 300, 20);
		g2d.drawString("walls: " + (cave.getCaveObjects(CaveObjectType.WALL).size()), 300, 30);
		// g2d.drawString("zoom: " + ((int)(cave.getCaveSizeMultiplier()*10)),400,10);
		g2d.drawString("orcs: " + (cave.getAllCaveObjects(CaveObjectType.CHARACTER).size() - 1), 200, 10);
	}

	private void drawBoundaries(Cave cave, Graphics2D g2d) {
		g2d.setColor(CaveDefaults.MONSTER_BLOOD_COLOR);
		int maxX = CaveDefaults.PLAY_AREA_X_SIZE + CaveDefaults.PLAYER_SIZE;
		int maxY = CaveDefaults.PLAY_AREA_Y_SIZE + CaveDefaults.PLAYER_SIZE;
		CaveObject.drawRect(new CavePosition(0 - CaveDefaults.PLAYER_SIZE / 2, 0 - CaveDefaults.PLAYER_SIZE / 2), new CavePosition(maxX, maxY), g2d, cave);
		CaveObject.drawRect(new CavePosition(-1 - CaveDefaults.PLAYER_SIZE / 2, -1 - CaveDefaults.PLAYER_SIZE / 2), new CavePosition(maxX + 2, maxY + 2), g2d, cave);
	}

	private void clearScreen(Graphics2D g2d) {
		g2d.setColor(new Color(0));
		g2d.fillRect(0, 0, CaveDefaults.WINDOW_X_SIZE + 20, CaveDefaults.WINDOW_Y_SIZE + 20);
	}

	private void drawObject(CaveObject caveObject, CavePosition cavePosition, Cave cave, long delta, Graphics2D g2d) {
		if (caveObject.getImage() != null) {
			drawSpriteFrame(g2d, cavePosition.getX(), cavePosition.getY(), caveObject.getLight(), caveObject.getImage(), cave);
			caveObject.getImage().setNextFrame(delta);
		} else {
			caveObject.draw(g2d, cave, delta);
		}
	}

	private void drawLitHitTarget(Cave cave, Graphics2D g2d, long delta) {
		CaveObject goal = cave.getPlayerCharacter().getHitGoal(cave);
		if (goal != null) {
			int maximumHueChange = 150;
			if (goal.getLight() < CaveDefaults.LIGHT_MAX_EFFECT_TO_OBJECT_BRIGHTNESS) {
				goal.addBrightness(10);
			}
			g2d.setColor(goal.calculateObjectLitColor(goal.getColor(), maximumHueChange));
			drawObject(goal, goal.getCavePosition(), cave, delta, g2d);
			// CaveObject.drawRect(goal.getCavePosition(), new CavePosition(1, 1), g2d, cave);
		}
	}

	private void drawDamageMeter(Graphics2D g2d, Cave cave) {
		int inventoryMultiplier = CaveDefaults.PLAYER_INVENTORY_VIEW_MULTIPLIER;
		CaveCharacter player = cave.getPlayerCharacter();
		double damage = (CaveDefaults.PLAYER_HEALTH - Double.valueOf(player.getHealth())) / 10;
		int maxHealth = CaveDefaults.PLAYER_HEALTH / 10;
		CavePosition damageStartPosition = new CavePosition(CaveDefaults.PLAYER_INVENTORY_X, CaveDefaults.WINDOW_PLAYER_POSITION + 20 + maxHealth * inventoryMultiplier);
		CavePosition damageEndPosition = damageStartPosition.add(new CavePosition(0, (damage - maxHealth) * inventoryMultiplier));
		CavePosition meterStartPosition = new CavePosition(CaveDefaults.PLAYER_INVENTORY_X + 5, CaveDefaults.WINDOW_PLAYER_POSITION + 20);
		CavePosition meterEndPosition = meterStartPosition.add(new CavePosition(0, maxHealth * inventoryMultiplier));
		g2d.setColor(CaveDefaults.PLAYER_BLOOD_COLOR);
		g2d.drawLine((int) damageStartPosition.getX(), (int) damageStartPosition.getY(), (int) damageEndPosition.getX(), (int) damageEndPosition.getY());
		g2d.setColor(CaveDefaults.FONT_COLOR);
		g2d.drawLine((int) meterStartPosition.getX(), (int) meterStartPosition.getY(), (int) meterEndPosition.getX(), (int) meterEndPosition.getY());
		g2d.drawLine((int) damageEndPosition.getX(), (int) damageEndPosition.getY(), (int) meterEndPosition.getX(), (int) damageEndPosition.getY());
	}

	public void drawHelp(CaveBuilder caveBuilder, Graphics g) {
		Graphics2D g2d = init(g);
		clearScreen(g2d);
		g2d.setColor(CaveDefaults.FONT_COLOR);
		g2d.drawString("Cave Adventure 2", 100, 70);

		int x = 100;
		int y = 100;
		g2d.drawString("Keys", x, y);
		g2d.drawString("Arrows   Move", x, y + 40);
		g2d.drawString("Shift        Pickaxe", x, y + 60);
		g2d.drawString("Ctrl         Bomb", x, y + 80);
		g2d.drawString("Q/ESC   Quit", x, y + 100);
		g2d.drawString("P/H         Pause & help", x, y + 120);
		g2d.drawString("Enter      Start game / Continue", x, y + 140);

		x = 400;
		y = 100;
		g2d.drawString("How to play", x, y);
		g2d.drawString("Use bombs to kill monsters.", x, y + 40);
		g2d.drawString("Use a pickaxe to destroy walls.", x, y + 60);
		g2d.drawString("(And kill monsters in case you run out of bombs..)", x, y + 80);
		g2d.drawString("Use torches to keep your torch burning.", x, y + 100);
		g2d.drawString("Set volume loud enough to hear footsteps.", x, y + 120);
		g2d.drawString("Set screen brightness to barely see", x, y + 140);
		g2d.setColor(CaveDefaults.MONSTER_BLOOD_COLOR);
		g2d.drawString("this text", x + 198, y + 140);
		g2d.setColor(CaveDefaults.FONT_COLOR);
		g2d.drawString(".", x + 241, y + 140);

		x = 100;
		y = 400;
		g2d.drawRect(x - 10, y - 20, 620, 450);

		g2d.drawString("FOR DEVELOPMENT", x, y);
		g2d.drawString("R Restart", x, y + 20);
		g2d.drawString("S Silence (growling)", x, y + 40);
		g2d.drawString("(shared function timer with pressing Ctrl)", x, y + 80);
		g2d.drawString("F1 FOV enabled", x, y + 100);
		g2d.drawString("F2 FOV disabled (zooms out to pause game)", x, y + 120);
		g2d.drawString("F3 More orcs", x, y + 140);
		g2d.drawString("F4 More walls", x, y + 160);
		g2d.drawString("F5 More bombs", x, y + 180);
		g2d.drawString("F6 More lights", x, y + 200);
		g2d.drawString("F7 More wall lights", x, y + 220);

		x += 300;
		y += 20;
		g2d.drawString("Zoom", x, y);
		g2d.drawString("Page Up        Up (Zooming out also pauses the game)", x, y + 20);
		g2d.drawString("Page Down   Down", x, y + 40);
		g2d.drawString("Home             Reset", x, y + 60);
		g2d.drawString("Insert       Torch full", x, y + 80);
		g2d.drawString("Delete     Torch empty", x, y + 100);
		g2d.drawString("End          Run", x, y + 120);

		x = 100;
		y = 700;
		g2d.drawString("Cave values", x, y - 20);
		g2d.drawString("Monsters " + caveBuilder.monsterAmount, x, y + 20);
		g2d.drawString("Walls " + caveBuilder.wallAmount + " * " + caveBuilder.wallLength, x, y + 40);
		g2d.drawString("Extralights " + caveBuilder.lightAmount, x, y + 60);
		g2d.drawString("Grounds " + caveBuilder.groundAmount, x, y + 80);
		g2d.drawString("Bombs " + caveBuilder.explosiveAmount, x, y + 100);

		x += 100;
		g2d.drawString("Altering keys", x, y);
		g2d.drawString("z -10 x +10", x, y + 20);
		g2d.drawString("f -1 g +1 * j -100 k +100", x, y + 40);
		g2d.drawString("c -10 v +10", x, y + 60);
		g2d.drawString("b -10 n +10", x, y + 80);
		g2d.drawString("t -10 y +10", x, y + 100);

	}

	private Graphics2D init(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		return g2d;
	}

	public void drawCollisionPositions(
			ArrayList<CavePosition> collisionPositions, Graphics g, Cave cave) {
		Graphics2D g2d = init(g);
		for (CavePosition position : collisionPositions) {
			g2d.setColor(CaveDefaults.FONT_COLOR);
			CaveObject.drawRect(position, new CavePosition(1, 1), g2d, cave);
		}
	}

	public void drawLoading(Graphics g, String phase, int i, long loadTime) {
		Graphics2D g2d = init(g);
		if (i == 0) {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, CaveDefaults.WINDOW_X_SIZE, CaveDefaults.WINDOW_Y_SIZE);
		}
		g2d.setColor(CaveDefaults.FONT_COLOR);
		g2d.drawString(""+loadTime, 600, 300 + (i-1) * 20);
		g2d.drawString("Loading " + phase + "...", 400, 300 + i * 20);
	}

	public void loadImages() {
		try {
			sprites = new BufferedImage[CaveDefaults.OBJECT_MAX_LIGHT];
			for (int light = 0; light < CaveDefaults.OBJECT_MAX_LIGHT; light++) {
				sprites[light] = ImageIO.read(new File(CaveDefaults.IMAGE_NAME));
				sprites[light] = fadeImage(sprites[light], light);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private BufferedImage fadeImage(BufferedImage image, double light) {
	        int width = image.getWidth();
	        int height = image.getHeight();
	        WritableRaster raster = image.getRaster();
	        height = 40;
	        for (int x = 0; x < width; x++) {
	            for (int y = 0; y < height; y++) {
	                int[] colors = raster.getPixel(x, y, (int[]) null);
	                colors[0]-=((CaveDefaults.OBJECT_MAX_LIGHT-light)/CaveDefaults.OBJECT_MAX_LIGHT)*255;
	                colors[1]-=((CaveDefaults.OBJECT_MAX_LIGHT-light)/CaveDefaults.OBJECT_MAX_LIGHT)*255;
	                colors[2]-=((CaveDefaults.OBJECT_MAX_LIGHT-light)/CaveDefaults.OBJECT_MAX_LIGHT)*255;
	                if (colors[0] < 0) {
	                	colors[0] = 0;
	                }
	                if (colors[1] < 0) {
	                	colors[1] = 0;
	                }
	                if (colors[2] < 0) {
	                	colors[2] = 0;
	                }
	                raster.setPixel(x, y, colors);
	            }
	        }
	        return image;
	}

	private void drawSpriteFrame(Graphics2D g2d, double x, double y, int light, CaveImage image, Cave cave) {
		if (light > CaveDefaults.OBJECT_MAX_LIGHT-1) {
			light = CaveDefaults.OBJECT_MAX_LIGHT-1;
		}
		drawSpriteFrame(sprites[light], g2d, x, y, image.getxSize(), image.getySize(), image.getFrameIndex(), CaveDefaults.IMAGE_COLUMN_AMOUNT, image.getCurrentFrame(), cave);
	}

//	public void drawSpriteFrame(Graphics2D g2d, int x, int y, int columns, int frame, Cave cave) {
//		drawSpriteFrame(sprites, g2d, x, y, columns, frame, 16, 16, cave);
//	}
//
//	public void drawSpriteFrame(Graphics2D g2d, int x, int y, int columns, int frame, int width, int height, Cave cave) {
//		drawSpriteFrame(sprites, g2d, x, y, columns, frame, width, height, cave);
//	}

	public void drawSpriteFrame(Image source, Graphics2D g2d, double x, double y, int width, int height, int frameIndex, int columns, int frame, Cave cave) {
		double multiplier = cave.getCaveSizeMultiplier();
		frame += frameIndex;
		int frameX = (frame % columns) * width;
		int frameY = (frame / columns) * height;
		int realX = (int) ((x- width / 2)*multiplier - CaveObject.getViewPointXAlteration(cave));
		int realY = (int) ((y- height / 2)*multiplier - CaveObject.getViewPointYAlteration(cave));
		int endX = realX + width;
		int endY = realY + height;
		g2d.drawImage(source, realX, realY, endX, endY, frameX, frameY, frameX + width, frameY + height, null);
	}
}
