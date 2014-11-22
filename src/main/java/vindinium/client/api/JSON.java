package vindinium.client.api;

import java.util.HashMap;
import java.util.Map;

import vindinium.game.core.Tile;

/**
 * A JSON class to hold keys expected from a Vindinium JSON response
 */
final class JSON {
	protected static final String GAME = "game";
	protected static final String HERO = "hero";
	protected static final String TOKEN = "token";
	protected static final String VIEW_URL = "viewUrl";
	protected static final String PLAY_URL = "playUrl";
	
	protected static final class Game {
		protected static final String ID = "id";
		protected static final String TURN = "turn";
		protected static final String MAX_TURNS = "maxTurns";
		protected static final String HEROES = "heroes";
		protected static final String BOARD = "board";
		protected static final String FINISHED = "finished";
		
		protected static final class Board {
			protected static final String SIZE = "size";
			protected static final String TILES = "tiles";
			
			protected static final HashMap<String, Tile> TILE_MAP = new HashMap<String, Tile>();
			protected static final HashMap<Tile, String> CHAR_MAP = new HashMap<Tile, String>();
			
			static {
				TILE_MAP.put("  ", Tile.AIR);
				TILE_MAP.put("##", Tile.WOODS);
				TILE_MAP.put("[]", Tile.TAVERN);
				TILE_MAP.put("@1", Tile.HERO0);
				TILE_MAP.put("@2", Tile.HERO1);
				TILE_MAP.put("@3", Tile.HERO2);
				TILE_MAP.put("@4", Tile.HERO3);
				TILE_MAP.put("$-", Tile.MINE_NEUTRAL);
				TILE_MAP.put("$1", Tile.MINE_HERO0);
				TILE_MAP.put("$2", Tile.MINE_HERO1);
				TILE_MAP.put("$3", Tile.MINE_HERO2);
				TILE_MAP.put("$4", Tile.MINE_HERO3);
				
				for (Map.Entry<String, Tile> entry : TILE_MAP.entrySet()) {
				    CHAR_MAP.put(entry.getValue(), entry.getKey());
				}
			}
		}
	}
	
	protected static final class Hero {
		protected static final String ID = "id";
		protected static final String NAME = "name";
		protected static final String USER_ID = "userId";
		protected static final String ELO = "elo";
		protected static final String POSITION = "pos";
		protected static final String LIFE = "life";
		protected static final String GOLD = "gold";
		protected static final String MINE_COUNT = "mineCount";
		protected static final String SPAWN_POSITION = "spawnPos";
		protected static final String CRASHED = "crashed";
		
		protected static final class Position {
			protected static final String X = "x";
			protected static final String Y = "y";
		}
	}
}
