package caveat.engine;

import java.awt.Color;

public class CaveDefaults {

	// GAME

	public static final boolean FOV = true;

	public static final int WINDOW_X_SIZE = 700;

	public static final int WINDOW_Y_SIZE = 700;

	public static final int WINDOW_PLAYER_POSITION = WINDOW_Y_SIZE / 2;

	public static final int PLAY_AREA_X_SIZE = 2000;
	
	public static final int PLAY_AREA_Y_SIZE = 2000;

	public static final int CENTER_POSITION_Y = PLAY_AREA_X_SIZE / 2;

	public static final int CENTER_POSITION_X = PLAY_AREA_Y_SIZE / 2;

	public static final int PLAYER_INVENTORY_VIEW_MULTIPLIER = 5;

	public static final int PLAYER_INVENTORY_X = WINDOW_X_SIZE - 50;

	public static final int VIEW_SIZE = 300;

	public static final int RESTART_COUNTER = 1500;

	public static final int COUNTER_INVALID = -1000;

	public static final double VISION_ACCURACY = Math.PI / 10;

	public static final double INITIAL_CAVE_SIZE_MULTIPLIER = 1.3;

	public static final int INITIAL_EXPLOSIVE_AMOUNT = 99;

	public static final int INITIAL_LIGHT_AMOUNT = 0;

	public static final int INITIAL_MONSTER_AMOUNT = 100;

	public static final int INITIAL_GROUND_AMOUNT = 0;

	// PHYSICS

	public static final long MAX_DELTA = 40;

	public static final double FRICTION = .85;

	// OBJECTS

	public static final double OBJECT_HEALTH = 10;

	public static final double OBJECT_HIT_PARTICLE_COUNT = 1;

	public static final int OBJECT_PARTICLE_WEIGHT = 1;

	public static final double OBJECT_DESTRUCTION_PARTICLE_RANDOM_SPEED = 15;

	public static final double OBJECT_DESTRUCTION_PARTICLE_MINIMUM_SPEED = 10;

	public static final int OBJECT_MAX_LIGHT = 60;

	// GROUND

	public static final int GROUND_SIZE = 6;

	// WALLS

	public static final int WALL_AMOUNT = 8;

	public static final int WALL_SIZE = 12;

	public static final int WALL_LENGTH = 300;

	public static final int WALL_MINIMUM_DISTANCE = 100;

	public static final double WALL_STRENGTH = 1000;

	public static final double WALL_CURVE_MULTIPLIER = 1;

	public static final double WALL_PARTICLE_RANDOM_SPEED = 5;

	public static final double WALL_PARTICLE_MINIMUM_SPEED = 5;

	public static final double WALL_LIGHT_POSITIONING_PROBABILITY = 0;//.05; // TODO set to > 0

	public static final int FILLED_WALL_AMOUNT = 20;

	public static final int FILLED_WALL_SIZE = 40;

	// LIGHTS

	public static final int LIGHT_RADIUS = 100;

	public static final int LIGHT_SIZE = 5;

	public static final int LIGHT_STRENGHT = 30;

	public static final double LIGHT_MAX_EFFECT_TO_OBJECT_BRIGHTNESS = 50;

	public static final double LIGHT_BRIGHTNESS_EXPONENTIAL = 1;

	public static final int LIGHT_PARTICLE_BRIGHTNESS = 100;

	public static final int LIGHT_DEPLETED_COUNTER = 5000;

	// ITEMS

	public static final int ITEM_POWER = 1;

	public static final int ITEM_SIZE = 1;

	public static final int ITEM_WEIGHT = 1;

	public static final int PICKAXE_SIZE = 20;

	public static final double PICKAXE_DAMAGE = 30;

	public static final double PICKAXE_RANDOM_DAMAGE = 40;

	public static final int PICKAXE_HIT_RADIUS = 5;

	// MONSTERS

	public static final int MONSTER_HOMING_DISTANCE = 170;

	public static final int MONSTER_GROUPING_DISTANCE = 100;

	public static final int MONSTER_GROUP_SPACING = 50;

	public static final int MONSTER_EXPLOSIVE_EVASION_DISTANCE = 30;

	public static final int MONSTER_WALL_EVASION_DISTANCE = WALL_SIZE;

	public static final double MONSTER_SPEED = 15;

	public static final double MONSTER_TURN_SPEED = 3;

	public static final double MONSTER_ACCELERATION = 1;

	public static final int MONSTER_SIZE = 8;

	public static final double MONSTER_HEALTH = 100;

	public static final double MONSTER_HIT_DAMAGE = 20;

	// EXPLOSIVES

	public static final int EXPLOSIVE_SIZE = 6;

	public static final int EXPLOSIVE_WEIGHT = 1;

	public static final int EXPLOSIVE_POWER = 60;

	public static final int MINIMUM_EXPLOSIVE_DELAY = 2000; // time in milliseconds *1.5

	public static final double EXPLOSIVE_COUNTER_RANDOMNESS = .3; // probability to reduce counter

	public static final double EXPLOSIVE_SPARKLE_RADIUS = 4;

	public static final int EXPLOSIVE_SPARKLE_AMOUNT = 4;

	public static final double EXPLOSIVE_FUSE_MULTIPLIER = .2;

	public static final double EXPLOSIVE_THRUST_POWER_MULTIPLIER = .3;

	public static final double EXPLOSIVE_PRESSURE_DAMAGE_MULTIPLIER = 1;

	public static final double EXPLOSIVE_PRESSURE_DAMAGE_AREA = .5;

	public static final int EXPLOSIVE_PARTICLE_MULTIPLIER = 6;

	public static final int EXPLOSIVE_PARTICLE_WEIGHT = 5;

	public static final double EXPLOSIVE_PARTICLE_MINIMUM_SPEED = 10;

	public static final double EXPLOSIVE_PARTICLE_RANDOM_SPEED = 15;

	// PARTICLES

	public static final int PARTICLE_SIZE = 2;

	public static final double PARTICLE_FLYING_FRICTION = .8;

	public static final double PARTICLE_FRICTION_ON_HIT = .5;

	public static final int PARTICLE_LIFETIME = 300; // 3000

	public static final int PARTICLE_LIFETIME_RANDOM = 1000;

	public static final double GLOWING_PARTICLE_LIFETIME = LIGHT_DEPLETED_COUNTER; // TODO fixme - fixed to prevent surroundings from darkening

	public static final double PARTICLE_DAMAGE_MULTIPLIER = .5; // multiplied with particle speed

	public static final double PARTICLE_DAMAGE_MINIMUM_SPEED = 2;

	// PLAYER

	public static final int PLAYER_FUNCTION_DELAY = 1000;

	public static final double PLAYER_SPEED = 22;

	public static final double PLAYER_ACCELERATION = 3;

	public static final double PLAYER_TURN_SPEED = 5;

	public static final int PLAYER_WALK_SOUND_INTERVAL = 600;

	public static final int PLAYER_HEALTH = 100;

	public static final int PLAYER_SIZE = 10;

	public static final int PLAYER_LIGHT_BRIGHTNESS = 70;

	public static final int PLAYER_LIGHT_MAX_COUNTER = 45000;

	// COLORS

	public static final int BLUE = 1;

	public static final int GREEN = 256;

	public static final int RED = 65536;

	public static final Color FONT_COLOR = Color.gray;// new Color(16777215);

	public static final Color SPARKLE_COLOR = new Color(13158600);

	public static final Color EXPLOSIVE_PARTICLE_COLOR = new Color(1509120); // new Color(3947520);//new Color(5263360);//new Color(6553600);

	public static final Color EYE_COLOR = new Color(16777215);

	public static final Color PLAYER_COLOR = new Color(655370); // new Color(1971230);

	public static final Color PLAYER_BLOOD_COLOR = new Color(4456448);

	public static final Color MONSTER_COLOR = new Color(0); // 1776411

	public static final Color MONSTER_BLOOD_COLOR = new Color(3584);

	public static final Color LIGHT_COLOR = new Color(1315840); // 8479744 //13158400

	public static final Color AMBIENT_LIGHT_COLOR = new Color(0);// 1776411	// 1315860

	public static final Color TORCH_LIGHT_COLOR = new Color(13158400);

	public static final int MAXIMUM_HUE_CHANGE = 100;

	// SOUNDS

	public static final String EXPLOSIVE_SOUND_FILENAME = "bomb_explosion.wav";

	public static final String EXPLOSIVE_COUNTER_SOUND_FILENAME = "bomb_counter.wav";

	public static final String PLAYER_DAMAGE_SOUND_FILENAME = "player_hit.wav";

	public static final String MONSTER_NOTICE_SOUND_FILENAME = "goblin_notice.wav";

	public static final String MONSTER_GROWL_SOUND_FILENAME = "goblin_growl.wav";

	public static final String MONSTER_DIE_SOUND_FILENAME = "goblin_die.wav";

	public static final String MONSTER_DAMAGE_SOUND_FILENAME = "goblin_hit.wav";

	public static final String PLAYER_WALK_SOUND_FILENAME = "player_walk1.wav";

	public static final String PLAYER_ALT1_WALK_SOUND_FILENAME = "player_walk2.wav";

	public static final String PLAYER_ALT2_WALK_SOUND_FILENAME = "player_walk3.wav";

	public static final String PLAYER_ALT3_WALK_SOUND_FILENAME = "player_walk4.wav";

	public static final String PLAYER_PICK_SOUND_FILENAME = "player_pick2.wav";

	public static final String LIGHT_DESTRUCTION_SOUND = "light_destruction.wav";

	public static final String WALL_DESTRUCTION_SOUND = "wall_destruction.wav";

	public static final String LIGHT_DAMAGE_SOUND = "light_hit.wav";

	public static final String PLAYER_SWITCH_SOUND_FILENAME = "player_switch.wav";

	// IMAGERY
	
	public static final int IMAGE_COLUMN_AMOUNT = 20;

	public static final String IMAGE_NAME = "images/caveat.png";

	// CLASS NAMES
	
	public static final String CAVEWALL = "CaveWall";

	public static final String CAVECHARACTER = "CaveCharacter";

	public static final String CAVEPARTICLE = "CaveParticle";

	public static final String CAVELIGHT = "CaveLight";

	public static final String CAVEEXPLOSIVE = "CaveExplosive";

	public static final String CAVEFILLEDWALL = "CaveFilledWall";

}