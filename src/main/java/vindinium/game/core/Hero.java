package vindinium.game.core;

import java.util.ArrayList;

import vindinium.bot.core.ShortestPath;

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
	 * Return Manhattan distance to another hero (doesn't take care of obstacles)
	 * @param p the target position
	 * @return distance to a position
	 */
	public int getDistanceTo(Position p) {
		return Math.abs(p.getX() - position.getX()) + Math.abs(p.getY() - position.getY());
	}
	
	/**
	 * Return exact distance to another hero
	 * @param p the target position
	 * @return distance to a position or Integer.MAX_VALUE if we didn't find any path
	 * @throws NoPathException throwed if no path can be found
	 */
	public int getExactDistanceTo(Board b, Position p) throws NoPathException {
		ArrayList<Position> path = ShortestPath.aStar(b, position, p);
		
		return path.size() - 2; // (Exclude start and end)
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ELO;
		result = prime * result + (crashed ? 1231 : 1237);
		result = prime * result + gold;
		result = prime * result + id;
		result = prime * result + life;
		result = prime * result + mineCount;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((spawnPosition == null) ? 0 : spawnPosition.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Hero)) return false;
		
		Hero other = (Hero) obj;
		if (ELO != other.ELO) return false;
		if (crashed != other.crashed) return false;
		if (gold != other.gold) return false;
		if (id != other.id) return false;
		if (life != other.life) return false;
		if (mineCount != other.mineCount) return false;
		if (name == null) if (other.name != null) return false;
		else if (!name.equals(other.name)) return false;
		if (position == null) if (other.position != null) return false;
		else if (!position.equals(other.position)) return false;
		if (spawnPosition == null) if (other.spawnPosition != null) return false;
		else if (!spawnPosition.equals(other.spawnPosition)) return false;
		if (userId == null) if (other.userId != null) return false;
		else if (!userId.equals(other.userId)) return false;
		
		return true;
	}
}
