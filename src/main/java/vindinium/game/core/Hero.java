package vindinium.game.core;

/**
 * A Java POJO for a Vindinium Hero
 */
public class Hero {
	private int id = -1;
	private String name = null;
	private String userId = null;
	private int ELO = 0;
	private Position position = null;
	private int life = 0;
	private int gold = 0;
	private int mineCount = 0;
	private Position spawnPosition = null;
	private boolean crashed = false;
	
	/**
	 * Default constructor
	 */
	public Hero() { }

	/**
	 * Copy constructor
	 * @param hero the hero to copy
	 */
	public Hero(Hero hero) {
		if(hero == null) throw new IllegalArgumentException("Parameter can't be null");
		
		id = hero.id;
		name = new String(hero.name);
		userId = new String(hero.userId);
		ELO = hero.ELO;
		position = new Position(hero.position);
		life = hero.life;
		gold = hero.gold;
		mineCount = hero.mineCount;
		spawnPosition = new Position(hero.spawnPosition);
		crashed = hero.crashed;
	}

	/**
	 * Get the hero's id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set the hero's id
	 * 
	 * @param id The id of the hero
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get the hero's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the hero's name
	 * 
	 * @param name The name of the hero
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the hero's user's id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the hero's user's id
	 * 
	 * @param userId The id of the hero's user
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Get the hero's ELO rating
	 */
	public int getELO() {
		return ELO;
	}

	/**
	 * Set the hero's ELO rating
	 * 
	 * @param elo The ELO rating of this hero
	 */
	public void setELO(int elo) {
		this.ELO = elo;
	}

	/**
	 * Get the hero's position on the board
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Set the hero's position on the board
	 * 
	 * @param position The position of the hero on the board
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Get the hero's life points
	 */
	public int getLife() {
		return life;
	}

	/**
	 * Set the hero's life points
	 * 
	 * @param life The life points of this hero
	 */
	public void setLife(int life) {
		this.life = life;
	}

	/**
	 * Get the hero's gold count
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * Set the hero's gold count
	 * 
	 * @param gold The gold count for this hero
	 */
	public void setGold(int gold) {
		this.gold = gold;
	}

	/**
	 * Get the hero's owned mine count
	 */
	public int getMineCount() {
		return mineCount;
	}

	/**
	 * Set the hero's owned mine count
	 * 
	 * @param mineCount The owned mine count for this hero
	 */
	public void setMineCount(int mineCount) {
		this.mineCount = mineCount;
	}

	/**
	 * Get the hero's spawn position on the board
	 */
	public Position getSpawnPosition() {
		return spawnPosition;
	}

	/**
	 * Set the hero's spawn position on the board
	 * 
	 * @param spawnPosition The spawn position of the hero on the board
	 */
	public void setSpawnPosition(Position spawnPosition) {
		this.spawnPosition = spawnPosition;
	}

	/**
	 * Get the hero's crash state
	 */
	public boolean isCrashed() {
		return crashed;
	}

	/**
	 * Set the hero to crashed (or not)
	 * 
	 * @param crashed TRUE = crashed
	 */
	public void setCrashed(boolean crashed) {
		this.crashed = crashed;
	}
	
	/**
	 * Return Manhattan distance to another hero
	 * @param h the target hero
	 * @return distance to another hero
	 */
	public double getDistanceTo(Hero h) {
		return Math.abs(h.getPosition().getX() - position.getX()) + Math.abs(h.getPosition().getY() - position.getY());
	}
}
