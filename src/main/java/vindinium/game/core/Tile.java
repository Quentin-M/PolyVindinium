package vindinium.game.core;

/**
 * Enum for Vindinium board tiles.
 */
public enum Tile {
	AIR,
	WOODS,
	TAVERN,
	MINE_NEUTRAL,
	MINE_HERO0,
	MINE_HERO1,
	MINE_HERO2,
	MINE_HERO3,
	HERO0,
	HERO1,
	HERO2,
	HERO3;
	
	/**
	 * Return which player owns the tile (mine, hero)
	 * 
	 * @param t the tile
	 * @return The hero index (0..3) if the tile is owned by someone, -1 is the tile is owned by neutral, -2 otherwise
	 */
	public static int getOwner(Tile t) {
		int mineId = t.ordinal() - MINE_NEUTRAL.ordinal() - 1;
		if(mineId>=-1 && mineId<=3) return mineId;
		
		int heroId = t.ordinal() - HERO0.ordinal();
		if(heroId>=0 && heroId<=3) return heroId;
		
		return -2;
	}
	
	// MINE
	/**
	 * Return the mine tile owned by specified player
	 * @param id The hero index (0..3) or -1 for a neutral mine
	 * @return the mine tile
	 */
	public static Tile getMine(int id) {
		if(id<-1 || id>3) throw new IllegalArgumentException("Invalid Mine ID");
		return Tile.values()[MINE_HERO0.ordinal()+id];	
	}
	
	/**
	 * Return true if the tile is a mine
	 * @param t the tile
	 * @return true if the tile is a mine
	 */
	public static boolean isMine(Tile t) {
		return t.ordinal()>=MINE_NEUTRAL.ordinal() && t.ordinal()<=MINE_HERO3.ordinal();
	}
	
	// HERO
	/**
	 * Return the hero tile for specified player
	 * @param id The hero index (0..3)
	 * @return the hero tile
	 */
	public static Tile getHero(int id) {
		return Tile.values()[HERO0.ordinal()+id];
	}
	
	/**
	 * Return true if the tile is a hero
	 * @param t the tile
	 * @return true if the tile is a hero
	 */
	public static boolean isHero(Tile t) {
		return t.ordinal()>=HERO0.ordinal() && t.ordinal()<=HERO3.ordinal();
	}
}