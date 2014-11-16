package vindinium.client.api;

import org.json.JSONArray;
import org.json.JSONObject;

import vindinium.game.core.Board;
import vindinium.game.core.Game;
import vindinium.game.core.Hero;
import vindinium.game.core.Position;

/**
 * A parser for deserializing a JSON response from a Vindinium server and turning
 * it into a Response POJO.
 */
public class ResponseParser {
	/**
	 * Parses a JSONObject into a Java Response POJO.
	 * 
	 * @param response A JSONObject in the response data schema expected from a Vindinium server
	 * @return A Response object POJO representing the json object
	 */
	public Response parseResponseJson(JSONObject response) {
		Game parsedGame = new Game();
		Hero parsedHero = null;
		
		// Parse first level attributes
		JSONObject gameObject = response.getJSONObject(JSON.GAME);
		JSONObject heroObject = response.getJSONObject(JSON.HERO);
		String token = response.getString(JSON.TOKEN);
		String viewUrl = response.getString(JSON.VIEW_URL);
		String playUrl = response.getString(JSON.PLAY_URL);
		// End first level attributes
		
		// Parse game level attributes
		parsedGame.setId( gameObject.getString(JSON.Game.ID) );
		parsedGame.setTurn( gameObject.getInt(JSON.Game.TURN) );
		parsedGame.setMaxTurns( gameObject.getInt(JSON.Game.MAX_TURNS) );
		parsedGame.setFinished( gameObject.getBoolean(JSON.Game.FINISHED) );
		
		// Parse heroes array
		JSONArray heroesArray = gameObject.getJSONArray(JSON.Game.HEROES);
		Hero[] heroes = new Hero[heroesArray.length()];
		for( int i = 0; i < heroesArray.length(); ++i ) {
			JSONObject currentHeroObject = heroesArray.getJSONObject(i);
			heroes[i] = parseHero(currentHeroObject);
		}
		parsedGame.setHeroes(heroes);
		// End parse heroes array
		
		// Parse board
		JSONObject boardObject = gameObject.getJSONObject(JSON.Game.BOARD);
		parsedGame.setBoard( parseBoard(boardObject) );
		// End parse board
		// End game level attributes
		
		// Parse hero level attributes
		parsedHero = parseHero(heroObject);
		// End hero level attributes
		
		return new Response(token, viewUrl, playUrl, parsedGame, parsedHero);
	}
	
	/**
	 * Parse a Hero JSONObject into a Java Hero POJO.
	 * 
	 * @param heroObject A JSONObject in the hero data schema expected from a Vindinium server
	 * @return A Hero object POJO representing the json object
	 */
	public Hero parseHero(JSONObject heroObject) {
		Hero parsedHero = new Hero();
		
		parsedHero.setId( heroObject.getInt(JSON.Hero.ID) );
		parsedHero.setName( heroObject.getString(JSON.Hero.NAME) );
		parsedHero.setUserId( heroObject.optString(JSON.Hero.USER_ID) );
		parsedHero.setELO( heroObject.optInt(JSON.Hero.ELO, 0) );
		parsedHero.setLife( heroObject.getInt(JSON.Hero.LIFE) );
		parsedHero.setGold( heroObject.getInt(JSON.Hero.GOLD) );
		parsedHero.setMineCount( heroObject.getInt(JSON.Hero.MINE_COUNT) );
		parsedHero.setCrashed( heroObject.getBoolean(JSON.Hero.CRASHED) );
		
		JSONObject positionObject = heroObject.getJSONObject(JSON.Hero.POSITION);
		int positionX = positionObject.getInt(JSON.Hero.Position.X);
		int positionY = positionObject.getInt(JSON.Hero.Position.Y);
		parsedHero.setPosition(new Position(positionX, positionY));
		
		JSONObject spawnPositionObject = heroObject.getJSONObject(JSON.Hero.SPAWN_POSITION);
		int spawnPositionX = spawnPositionObject.getInt(JSON.Hero.Position.X);
		int spawnPositionY = spawnPositionObject.getInt(JSON.Hero.Position.Y);
		parsedHero.setSpawnPosition(new Position(spawnPositionX, spawnPositionY));
		
		return parsedHero;
	}
	
	/**
	 * Parse a Board JSONObject into a Java Board POJO.
	 * 
	 * @param boardObject A JSONObject in the board data schema expected from a Vindinium server
	 * @return A IBoard object POJO representing the json object
	 */
	public Board parseBoard(JSONObject boardObject) {
		Board parsedBoard = new Board();

		parsedBoard.setSize( boardObject.getInt(JSON.Game.Board.SIZE) );
		parsedBoard.setTiles( boardObject.getString(JSON.Game.Board.TILES) );
		
		return parsedBoard;
	}
}
