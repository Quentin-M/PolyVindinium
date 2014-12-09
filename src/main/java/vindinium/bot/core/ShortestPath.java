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
	
	private static boolean isAccessible(Board board, Position position) {
		if(position.getX() < 0 || position.getY() < 0 || position.getX() >= board.getSize() || position.getY() >= board.getSize()) return false;
		Tile t = board.getTile(position.getX(), position.getY());
		return (t == Tile.AIR);
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

	public static FloydResult Floyd(Board board){
		int i, j, k, x, y, d[][], p[][], tableSize = board.getSize() * board.getSize(), boardSize = board.getSize();
		d = new int [tableSize][tableSize];
		p = new int [tableSize][tableSize];
		Tile currentTile;
		FloydResult res = new FloydResult();
		
		// Calculating the initial cost matrix -> the cost from a point to of his neighbours is always 1
		
		for (i = 0; i < tableSize; i++) {
	        for (j = 0; j < tableSize; j++) {
	            d[i][j] = Integer.MAX_VALUE;
	        }
	    }
		
		for(i = 0; i < tableSize; i++) {
			currentTile = board.getTile(i);
			System.out.print(currentTile);
			if(currentTile == Tile.AIR || currentTile == Tile.HERO0 || currentTile == Tile.HERO1 || currentTile == Tile.HERO2 || currentTile == Tile.HERO3) {
				for(j = 0; j < tableSize; j++) {
					if(i == j) {
						d[i][j] = 0;
					}
					else {
						initNeighbour(board, i, i/boardSize, i%boardSize - 1, d, boardSize);
						initNeighbour(board, i, i/boardSize, i%boardSize + 1, d, boardSize);
						initNeighbour(board, i, i/boardSize - 1, i%boardSize, d, boardSize);
						initNeighbour(board, i, i/boardSize + 1, i%boardSize, d, boardSize);
					}
				}
			}
		}
		
		/*// Display Test
		System.out.println("");
	    for (i = 0; i < tableSize; i++) {
	        for (j = 0; j < tableSize; j++) {
	            System.out.print(d[i][j] + " ");
	        }
	        System.out.println();
	    }
	    System.out.println();
	    System.out.println("**********************************************************************************************************************");
	    System.out.println();*/
		
		// Calculating the initial predecessor matrix
		for (i = 0; i < tableSize; i++) {
	        for (j = 0; j < tableSize; j++) {
	            if (d[i][j] != 0 && d[i][j] != Integer.MAX_VALUE) {
	                p[i][j] = i;
	            } else {
	                p[i][j] = -1;
	            }
	        }
	    }
		
		// Calculating the distance matrix d
	    for (k = 0; k < tableSize; k++) {
	    	currentTile = currentTile = board.getTile(k);
	    	if(currentTile != Tile.WOODS) {
		        for (i = 0; i < tableSize; i++) {
		        	currentTile = currentTile = board.getTile(i);
		        	if(currentTile != Tile.WOODS) {
			            for (j = 0; j < tableSize; j++) {
			                if (d[i][k] == Integer.MAX_VALUE || d[k][j] == Integer.MAX_VALUE || i == j) {
			                    continue;         
			                }
			                
			                if (d[i][j] > d[i][k] + d[k][j]) {
			                    d[i][j] = d[i][k] + d[k][j];
			                    p[i][j] = p[k][j];
			                }
			            }
		        	}
		        }
	    	}
	    }
	    
	    // Display Test d
	    for (i = 0; i < tableSize; i++) {
	        for (j = 0; j < tableSize; j++) {
	            System.out.print(d[i][j] + " ");
	        }
	        System.out.println();
	    }
	    System.out.println();
	    System.out.println("**********************************************************************************************************************");
	    System.out.println();
	    // Display Test p
	    for (i = 0; i < tableSize; i++) {
	        for (j = 0; j < tableSize; j++) {
	            System.out.print(p[i][j] + " ");
	        }
	        System.out.println();
	    }
	    
	    res.setDistances(d);
	    res.setPredecessors(p);
	    return res;
	}

	private static void initNeighbour(Board board, int i, int x, int y, int[][] d, int boardSize) {
		Tile currentTile;
		if(x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
			currentTile = board.getTile(x, y);
			if(currentTile != Tile.WOODS) {
				d[i][(x * boardSize) + y] = 1;
			}
			else {
				d[i][(x * boardSize) + y] = Integer.MAX_VALUE;
			}
		}
	}
}
