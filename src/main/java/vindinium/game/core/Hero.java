package vindinium.game.core;

/**
 * A Java POJO for a Vindinium Hero
 */
public class Hero {
	private int mId = -1;
	private String mName = null;
	private String mUserId = null;
	private int mELO = 0;
	private Position mPosition = null;
	private int mLife = 0;
	private int mGold = 0;
	private int mMineCount = 0;
	private Position mSpawnPosition = null;
	private boolean mCrashed = false;
	
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
		
		mId = hero.mId;
		mName = new String(hero.mName);
		mUserId = new String(hero.mUserId);
		mELO = hero.mELO;
		mPosition = new Position(hero.mPosition);
		mLife = hero.mLife;
		mGold = hero.mGold;
		mMineCount = hero.mMineCount;
		mSpawnPosition = new Position(hero.mSpawnPosition);
		mCrashed = hero.mCrashed;
	}

	/**
	 * Get the hero's id
	 */
	public int getId() {
		return mId;
	}
	
	/**
	 * Set the hero's id
	 * 
	 * @param id The id of the hero
	 */
	public void setId(int id) {
		mId = id;
	}

	/**
	 * Get the hero's name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Set the hero's name
	 * 
	 * @param name The name of the hero
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * Get the hero's user's id
	 */
	public String getUserId() {
		return mUserId;
	}

	/**
	 * Set the hero's user's id
	 * 
	 * @param userId The id of the hero's user
	 */
	public void setUserId(String userId) {
		this.mUserId = userId;
	}

	/**
	 * Get the hero's ELO rating
	 */
	public int getELO() {
		return mELO;
	}

	/**
	 * Set the hero's ELO rating
	 * 
	 * @param elo The ELO rating of this hero
	 */
	public void setELO(int elo) {
		this.mELO = elo;
	}

	/**
	 * Get the hero's position on the board
	 */
	public Position getPosition() {
		return mPosition;
	}

	/**
	 * Set the hero's position on the board
	 * 
	 * @param position The position of the hero on the board
	 */
	public void setPosition(Position position) {
		this.mPosition = position;
	}

	/**
	 * Get the hero's life points
	 */
	public int getLife() {
		return mLife;
	}

	/**
	 * Set the hero's life points
	 * 
	 * @param life The life points of this hero
	 */
	public void setLife(int life) {
		this.mLife = life;
	}

	/**
	 * Get the hero's gold count
	 */
	public int getGold() {
		return mGold;
	}

	/**
	 * Set the hero's gold count
	 * 
	 * @param gold The gold count for this hero
	 */
	public void setGold(int gold) {
		this.mGold = gold;
	}

	/**
	 * Get the hero's owned mine count
	 */
	public int getMineCount() {
		return mMineCount;
	}

	/**
	 * Set the hero's owned mine count
	 * 
	 * @param mineCount The owned mine count for this hero
	 */
	public void setMineCount(int mineCount) {
		this.mMineCount = mineCount;
	}

	/**
	 * Get the hero's spawn position on the board
	 */
	public Position getSpawnPosition() {
		return mSpawnPosition;
	}

	/**
	 * Set the hero's spawn position on the board
	 * 
	 * @param spawnPosition The spawn position of the hero on the board
	 */
	public void setSpawnPosition(Position spawnPosition) {
		this.mSpawnPosition = spawnPosition;
	}

	/**
	 * Get the hero's crash state
	 */
	public boolean isCrashed() {
		return mCrashed;
	}

	/**
	 * Set the hero to crashed (or not)
	 * 
	 * @param crashed TRUE = crashed
	 */
	public void setCrashed(boolean crashed) {
		this.mCrashed = crashed;
	}
}
