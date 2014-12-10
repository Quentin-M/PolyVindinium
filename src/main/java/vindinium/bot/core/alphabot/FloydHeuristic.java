package vindinium.bot.core.alphabot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.game.core.Game;
import vindinium.game.core.Position;
import vindinium.game.core.Tile;
import vindinium.simulation.FloydResult;
import vindinium.simulation.ShortestPath;

public class FloydHeuristic implements HeuristicInterface {	
	final static Logger logger = LogManager.getLogger();
	public FloydResult floyd;
	
	public int evaluate(Game initialGame, Game game) {
		if(floyd == null) {
			logger.info("Initializing the heuristic using Floyd algorithm");
			floyd = ShortestPath.Floyd(game.getBoard());
		}
		
		// Targets
		int mineDistance = 0;
		if(game.getHero().getMineCount() < game.getBoard().getMines().size()) {
			mineDistance = Integer.MAX_VALUE;
			for(Position p: game.getBoard().getMines()) {
				if(Tile.getOwner(game.getBoard().getTile(p.getX(), p.getY())) == game.getHero().getId()) continue;
				
				mineDistance = Math.min(mineDistance, floyd.getDistances(game.getBoard(), game.getHero().getPosition(), p));
			}
		}
		int tavernDistance = 0;
		if(game.getHero().getLife()<=30) {
			tavernDistance = Integer.MAX_VALUE;
			for(Position p: game.getBoard().getTaverns()) {
				tavernDistance = Math.min(tavernDistance, floyd.getDistances(game.getBoard(), game.getHero().getPosition(), p));
			}
		}
		// Track the hero who have a lot of mines if he has 4 more gold mines than me
		int heroMaxMinesIndex = game.getHero().getId();
		for(int i = 0; i<4; i++) {
			if(game.getHeroes()[heroMaxMinesIndex].getMineCount() < game.getHeroes()[i].getMineCount()) heroMaxMinesIndex = i;
		}
		int heroMaxMinesDistance = floyd.getDistances(game.getBoard(), game.getHero().getPosition(), game.getHeroes()[heroMaxMinesIndex].getPosition());
		if(game.getHeroes()[heroMaxMinesIndex].getMineCount() <= game.getHero().getMineCount() + 4 || game.getHeroes()[heroMaxMinesIndex].getLife() > game.getHero().getLife() + 20) heroMaxMinesDistance = 0;
		
		return    15*game.getHero().getMineCount()*(game.getMaxTurns()-game.getTurn())
				+ 5*(int) ((game.getHero().getLife())/10.0)
				
				- 1*heroMaxMinesDistance
				- 2*mineDistance
				- 3*tavernDistance
		;
	}
}
