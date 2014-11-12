package vindinium.client.api;

/**
 * A JSON class to hold keys expected from a Vindinium JSON response
 */
public final class JSON {
	public static final String GAME = "game";
	public static final String HERO = "hero";
	public static final String TOKEN = "token";
	public static final String VIEW_URL = "viewUrl";
	public static final String PLAY_URL = "playUrl";
	
	public static final class Game {
		public static final String ID = "id";
		public static final String TURN = "turn";
		public static final String MAX_TURNS = "maxTurns";
		public static final String HEROES = "heroes";
		public static final String BOARD = "board";
		public static final String FINISHED = "finished";
		
		public static final class Board {
			public static final String SIZE = "size";
			public static final String TILES = "tiles";
		}
	}
	
	public static final class Hero {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String USER_ID = "userId";
		public static final String ELO = "elo";
		public static final String POSITION = "pos";
		public static final String LIFE = "life";
		public static final String GOLD = "gold";
		public static final String MINE_COUNT = "mineCount";
		public static final String SPAWN_POSITION = "spawnPos";
		public static final String CRASHED = "crashed";
		
		public static final class Position {
			public static final String X = "x";
			public static final String Y = "y";
		}
	}
}
