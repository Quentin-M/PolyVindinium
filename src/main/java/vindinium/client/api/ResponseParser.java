package vindinium.client.api;

import org.json.JSONArray;
import org.json.JSONObject;

import vindinium.game.core.Game;
import vindinium.game.core.Hero;
import vindinium.game.core.Position;
import vindinium.game.core.Tile;

/**
 * A parser for deserializing a JSON response from a Vindinium server and turning
 * it into a Response POJO.
 */
public class ResponseParser {
	/**
	 * Parses a JSONObject into Game object
	 * If game object is null, create a new one
	 * 
	 * @param response A JSONObject in the response data schema expected from a Vindinium server
	 * @return A new or updated game object
	 */
	public static Game parseResponseJson(Game game, JSONObject response) {
		JSONObject gameObject = response.getJSONObject(JSON.GAME);
		JSONObject heroObject = response.getJSONObject(JSON.HERO);
		JSONObject boardObject = gameObject.getJSONObject(JSON.Game.BOARD);
		JSONArray heroesArray = gameObject.getJSONArray(JSON.Game.HEROES);
		String tiles = boardObject.getString(JSON.Game.Board.TILES);
		
		if(game==null) {
			// It is a new game, create an entire game object
			game = new Game(gameObject.getString(JSON.Game.ID), gameObject.getInt(JSON.Game.MAX_TURNS), response.getString(JSON.TOKEN), response.getString(JSON.VIEW_URL), response.getString(JSON.PLAY_URL), boardObject.getInt(JSON.Game.Board.SIZE), heroObject.getInt(JSON.Hero.ID)-1);
						
			// Create board
			for(int i = 0; i<tiles.length(); i+=2) {
				int x = (i/2) / game.getBoard().getSize();
				int y = (i/2) % game.getBoard().getSize();
				Tile t = JSON.Game.Board.TILE_MAP.get(tiles.substring(i, i+2));
				
				game.getBoard().setTile(x, y, t);
				
				if(Tile.isMine(t)) {
					// Create mine
					game.getBoard().getMines().add(new Position(x, y));
				} else if(t == Tile.TAVERN) {
					// Create tavern
					game.getBoard().getTaverns().add(new Position(x, y));
				}
			}
			
			// Parse heroes
			for(int i = 0; i < heroesArray.length(); i++) {
				JSONObject currentHeroObject = heroesArray.getJSONObject(i);
				
				game.getHeroes()[i] = new Hero();
				game.getHeroes()[i].setId(currentHeroObject.getInt(JSON.Hero.ID)-1);
				game.getHeroes()[i].setName(currentHeroObject.getString(JSON.Hero.NAME));
				game.getHeroes()[i].setUserId(currentHeroObject.optString(JSON.Hero.USER_ID));
				game.getHeroes()[i].setELO(currentHeroObject.optInt(JSON.Hero.ELO, 0));
			}
		}
		
		// Update game state
		game.setTurn(gameObject.getInt(JSON.Game.TURN));
		game.setFinished(gameObject.getBoolean(JSON.Game.FINISHED));
		
		// Update mine tiles
		for(Position m: game.getBoard().getMines()) {
			game.getBoard().setTile(m.getX(), m.getY(), JSON.Game.Board.TILE_MAP.get(tiles.substring((m.getX()*2*game.getBoard().getSize())+m.getY()*2, (m.getX()*2*game.getBoard().getSize())+m.getY()*2+2)));
		}
		
		// Update heroes
		for(int i = 0; i < heroesArray.length(); ++i ) {
			JSONObject currentHeroObject = heroesArray.getJSONObject(i);
			
			game.getHeroes()[i].setLife(currentHeroObject.getInt(JSON.Hero.LIFE));
			game.getHeroes()[i].setGold(currentHeroObject.getInt(JSON.Hero.GOLD));
			game.getHeroes()[i].setMineCount(currentHeroObject.getInt(JSON.Hero.MINE_COUNT));
			game.getHeroes()[i].setCrashed(currentHeroObject.getBoolean(JSON.Hero.CRASHED));
			
			// Remove old tile on the map
			if(game.getHeroes()[i].getPosition() != null)
				game.getBoard().setTile(game.getHeroes()[i].getPosition().getX(), game.getHeroes()[i].getPosition().getY(), Tile.AIR);
			
			JSONObject positionObject = currentHeroObject.getJSONObject(JSON.Hero.POSITION);
			game.getHeroes()[i].setPosition(new Position(positionObject.getInt(JSON.Hero.Position.X), positionObject.getInt(JSON.Hero.Position.Y)));
			
			// Add tile on the map
			game.getBoard().setTile(game.getHeroes()[i].getPosition().getX(), game.getHeroes()[i].getPosition().getY(), Tile.getHero(i));
			
			JSONObject spawnPositionObject = currentHeroObject.getJSONObject(JSON.Hero.SPAWN_POSITION);
			game.getHeroes()[i].setSpawnPosition(new Position(spawnPositionObject.getInt(JSON.Hero.Position.X), spawnPositionObject.getInt(JSON.Hero.Position.Y)));
		}
		
		return game;
	}
}
