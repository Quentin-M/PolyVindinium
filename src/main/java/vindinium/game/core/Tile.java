package vindinium.game.core;

/**
 * Enum for Vindinium board tiles.
 */
public enum Tile {
	AIR,
	WOODS,
	TAVERN,
	MINE_NEUTRAL,
	MINE_HERO1,
	MINE_HERO2,
	MINE_HERO3,
	MINE_HERO4,
	HERO1,
	HERO2,
	HERO3,
	HERO4;
	
	// MINE
	public static int getMineId(Tile t) {
		return t.ordinal()-MINE_NEUTRAL.ordinal(); // Return between 0 for neutral mine and 4 for Hero4 mine
	}
	
	public static Tile getMine(int id) {
		if(id<0 || id>4) throw new IllegalArgumentException("Invalid Mine ID"); // Takes form 0 for neutral mine to 4 or Hero4 mine
		return Tile.values()[MINE_NEUTRAL.ordinal()+id];	
	}
	
	public static boolean isMine(Tile t) {
		return t.ordinal()>=MINE_NEUTRAL.ordinal() && t.ordinal()<=MINE_HERO4.ordinal();
	}
	
	// HERO
	public static int getHeroId(Tile t) {
		return t.ordinal()-HERO1.ordinal(); // Return between 0 for HERO1 and 3 for Hero4
	}
	
	public static Tile getHero(int id) {
		return Tile.values()[HERO1.ordinal()+id]; // Return between 0 for Hero1 and 3 for Hero 4
	}
	
	public static boolean isHero(Tile t) {
		return t.ordinal()>=HERO1.ordinal() && t.ordinal()<=HERO4.ordinal();
	}
}