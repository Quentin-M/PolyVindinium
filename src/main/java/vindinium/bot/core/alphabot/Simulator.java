package vindinium.bot.core.alphabot;

import vindinium.game.core.Action;
import vindinium.game.core.Game;
import vindinium.game.core.Hero;
import vindinium.game.core.Position;
import vindinium.game.core.Tile;

//TODO If crashed, only one possible move! STAY
/**
 * A toolbox to simulate a move on a game state
 */
public class Simulator {	
	/**
	 * Simulate a game turn
	 * Will return null if the game is ended or if the requested movement is different than STAY but we can't move (tree, hero, map limit)
	 * @param game
	 * @param action
	 * @param currentPlayer
	 * @return
	 */
	public static Game simulate(final Game game, Action action, final Hero currentPlayer) {
		if(game.isFinished()) return null;
		
		Game newGame = new Game(game);
		int heroIndex = currentPlayer.getId();
		
		// Turn increase / Finished state
		newGame.setTurn(game.getTurn()+1);
		if(newGame.getTurn()==1200) newGame.setFinished(true);
		
		// Hero move
		if(action != Action.STAY) {
			Position nextPosition = getPositionAfterAction(game, action, heroIndex);
			
			// If we try to move outside the map, return null. Similar to STAY
			if(nextPosition.getX() < 0 || nextPosition.getX() >= newGame.getBoard().getSize() || nextPosition.getY() < 0 || nextPosition.getY() >= newGame.getBoard().getSize())
				return null;
			
			// Next position tile
			Tile nextPositionTile = newGame.getBoard().getTile(nextPosition.getX(), nextPosition.getY());
			
			// If we try to move on woods or on hero, return null. Similar to STAY
			if(nextPositionTile == Tile.WOODS || Tile.isHero(nextPositionTile))
				return null;
					
			// - Steps into a gold mine, he stays in place, and:
			if(Tile.isMine(nextPositionTile)) {
				// - - If the mine is neutral, or belongs to another hero, a fight happens against the goblin guarding the mine. The hero loses 20 life points. If he survives, the mine is his.
				if(
					(nextPositionTile == Tile.MINE_NEUTRAL) ||
					(heroIndex == 0 && nextPositionTile != Tile.MINE_HERO0) ||
					(heroIndex == 1 && nextPositionTile != Tile.MINE_HERO1) ||
					(heroIndex == 2 && nextPositionTile != Tile.MINE_HERO2) ||
					(heroIndex == 3 && nextPositionTile != Tile.MINE_HERO3)
				) {
					if(currentPlayer.getLife()>20) {
						newGame.transferMine(nextPosition, heroIndex);
					} else {
						manageDeath(newGame, heroIndex, -1);
					}
				} else {
					// It's useless to walk on a mine we already have
					return null;
				}
			}
			
			// - Steps into a tavern, he stays in place and orders a beer. The hero pays 2 gold and receive 50HP. Note than HP can never exceed 100.
			else if(nextPositionTile == Tile.TAVERN) {
				if(newGame.getHeroes()[heroIndex].getGold() >= 2) {
					newGame.getHeroes()[heroIndex].setGold(newGame.getHeroes()[heroIndex].getGold()-2);
					newGame.getHeroes()[heroIndex].setLife(Math.min(100, newGame.getHeroes()[heroIndex].getLife()+50));
				} else {
					// It is useless to consider buying a beer if we don't have enough gold
					return null;
				}
			}
			
			// - Move the hero (not a tavern, an hero, a mine and not outside the map)
			else {
				newGame.getBoard().setTile(currentPlayer.getPosition().getX(), currentPlayer.getPosition().getY(), Tile.AIR);
				newGame.getHeroes()[heroIndex].setPosition(nextPosition);
				newGame.getBoard().setTile(nextPosition.getX(), nextPosition.getY(), Tile.getHero(heroIndex));
			}
		}
		
		// Fights
		for(Hero h : newGame.getHeroes()) {
			// If the hero is at distance 1 of ours, it means that he's attackable now !
			if(newGame.getHeroes()[heroIndex].getDistanceTo(h.getPosition()) == 1) {
				if(h.getLife()>20) h.setLife(h.getLife()-20);
				else {
					manageDeath(newGame, h.getId(), heroIndex);
				}
			}
		}
		
		// Gain 1 gold per mine
		newGame.getHeroes()[heroIndex].setGold(newGame.getHeroes()[heroIndex].getGold()+newGame.getHeroes()[heroIndex].getMineCount());
		
		// Loose 1 HP
		newGame.getHeroes()[heroIndex].setLife(Math.max(1, newGame.getHeroes()[heroIndex].getLife()-1));
		
		return newGame;
	}
	
	/**
	 * Manage the death of an hero
	 * @param game
	 * @param heroIndex index of the hero who's dying. Must be [0..3]
	 * @param killerIndex index of the source hero (-1 if neutral). Must be [-1..3]
	 */
	private static void manageDeath(Game game, int heroIndex, int killerIndex) {
		if(heroIndex<0 || heroIndex > 3) throw new IllegalArgumentException("Dying hero is invalid");
		if(killerIndex<-1 || killerIndex > 3) throw new IllegalArgumentException("Killer hero is invalid");

		// Transfer mines to killer		
		for(Position p: game.getBoard().getMines()) {
			if(Tile.getOwner(game.getBoard().getTile(p.getX(), p.getY())) == heroIndex) 
				game.transferMine(p, killerIndex, false);
		}
				
		// Reset life to 100 HP
		game.getHeroes()[heroIndex].setLife(100);
		
		// Respawn
		game.getBoard().setTile(game.getHeroes()[heroIndex].getPosition().getX(), game.getHeroes()[heroIndex].getPosition().getY(), Tile.AIR);
		
		if(Tile.isHero(game.getBoard().getTile(game.getHeroes()[heroIndex].getSpawnPosition().getX(), game.getHeroes()[heroIndex].getSpawnPosition().getY()))) {
			// A hero is at my spawn point, kill him and respawn him
			//TODO Killed by neutral or by respawing player ?
			manageDeath(game, Tile.getOwner(game.getBoard().getTile(game.getHeroes()[heroIndex].getSpawnPosition().getX(), game.getHeroes()[heroIndex].getSpawnPosition().getY())), -1);
		}
		
		game.getHeroes()[heroIndex].setPosition(game.getHeroes()[heroIndex].getSpawnPosition());
		game.getBoard().setTile(game.getHeroes()[heroIndex].getSpawnPosition().getX(), game.getHeroes()[heroIndex].getSpawnPosition().getY(), Tile.getHero(heroIndex));
	}

	private static Position getPositionAfterAction(Game game, Action action, int heroIndex) {
		Position pos = game.getHeroes()[heroIndex].getPosition();
		
		switch(action) {
			case STAY:
				return new Position(pos);
			case EAST:
				return new Position(pos.getX(), pos.getY()+1);
			case WEST:
				return new Position(pos.getX(), pos.getY()-1);
			case NORTH:
				return new Position(pos.getX()-1, pos.getY());
			case SOUTH:
				return new Position(pos.getX()+1, pos.getY());
			default:
				throw new IllegalArgumentException("Non-managed enum case");
		}
	}
}
