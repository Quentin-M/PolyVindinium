package vindinium.bot.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import vindinium.game.core.Board;
import vindinium.game.core.Game;
import vindinium.game.core.NoPathException;
import vindinium.game.core.Position;
import vindinium.game.core.Tile;

public class ShortestPath {
	/**
	 * Compute shortest path from start to end
	 * @param start
	 * @param end
	 * @return an ordered arraylist of positions from start to end
	 * @throws NoPathException throwed if no path can be found
	 */
	public static ArrayList<Position> aStar(Board board, Position start, Position end) throws NoPathException {
		
		// Initialize lists
		HashSet<Position> closedList = new HashSet<Position>(); // The set of nodes already evaluated
		HashSet<Position> openedList = new HashSet<Position>(); // The set of tentative nodes to be evaluated, initially containing the start node
		openedList.add(start);
		HashMap<Position, Position> cameFrom = new HashMap<Position, Position>(); // The map of navigated nodes
		HashMap<Position, Integer> g = new HashMap<Position, Integer>();
		g.put(start, 0); // Cost from start along best known path
		HashMap<Position, Integer> f = new HashMap<Position, Integer>(); // Estimated total cost from start to goal through y.
		f.put(start, Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY()));
		
		while(!openedList.isEmpty()) {
			Position current = findBestFromOpenedList(openedList, f);
			
			// End condition
			if(current.equals(end)) {
				return reconstructPath(cameFrom, end);
			}
			
			// Switch the position from opened to closed list
			openedList.remove(current);
			closedList.add(current);
			
			// For each neighbors
			Position neighbors[] = { new Position(current.getX()+1, current.getY()), new Position(current.getX()-1, current.getY()), new Position(current.getX(), current.getY()+1), new Position(current.getX(), current.getY()-1)};
			for(int i = 0; i<4; i++) {
				if(!isAccessible(board, neighbors[i]) && !neighbors[i].equals(end)) continue;
				
				// If the neighbor is in closed list, continue
				if(closedList.contains(neighbors[i])) continue;
				
				int tentative_g_score = g.get(current) + 1;
				
				if(!openedList.contains(neighbors[i]) || tentative_g_score < g.get(neighbors[i])) {
					cameFrom.put(neighbors[i], current);
					g.put(neighbors[i], tentative_g_score);
					f.put(neighbors[i], g.get(neighbors[i]) + Math.abs(neighbors[i].getX() - end.getX()) + Math.abs(neighbors[i].getY() - end.getY()));
					
					if(!openedList.contains(neighbors[i])) openedList.add(neighbors[i]);
				}
			}
		}
		
		throw new NoPathException("There is no path between " + start + " and " + end);
	}

	private static boolean isAccessible(Board board, Position position) {
		if(position.getX() < 0 || position.getY() < 0 || position.getX() >= board.getSize() || position.getY() >= board.getSize()) return false;
		Tile t = board.getTile(position.getX(), position.getY());
		return (t == Tile.AIR);
	}

	private static ArrayList<Position> reconstructPath(HashMap<Position, Position> cameFrom, Position end) {
		ArrayList<Position> path = new ArrayList<Position>();
		
		// Add end
		Position current = end;
		path.add(current);
		
		// Follow path
		while(cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			path.add(current);
		}
	    	   
		// Reverse
		Collections.reverse(path);
		
		return path;
	}

	private static Position findBestFromOpenedList(HashSet<Position> openedList, HashMap<Position, Integer> f) {
		int min = Integer.MAX_VALUE;
		Position minPosition = null;
		for(Position p: openedList) {			
			int fp = f.get(p);
			if(fp < min) {
				min = fp;
				minPosition = p;
			}
		}
		return minPosition;
	}
}
