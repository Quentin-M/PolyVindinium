package vindinium.simulation;

import java.util.ArrayList;
import java.util.Collections;

import vindinium.game.core.Board;
import vindinium.game.core.NoPathException;
import vindinium.game.core.Position;

public class FloydResult {
	private int[][] distances;
	private int[][] predecessors;
	
	public FloydResult(){
		
	}
	
	public void setDistances(int [][] newDistances) {
		distances = newDistances;
	}
	
	public void setPredecessors(int [][] newPredecessors) {
		predecessors = newPredecessors;
	}
	
	public ArrayList<Position> getPath(ArrayList<Position> path, Board board, Position P1, Position P2) throws NoPathException {
		if (path == null){
			path = new ArrayList<Position>();
		}
		int i = (P1.getX() * board.getSize()) + P1.getY();
		int j = (P2.getX() * board.getSize()) + P2.getY();
		
	    if(i == j){
	    	path.add(new Position(i / board.getSize(), i % board.getSize()));
	        Collections.reverse(path);
	        return path;
	    }
	    else if(predecessors[i][j] == -1){
	    	throw new NoPathException("Path does not exist");
	    }
	    else{
	    	path.add(new Position(j / board.getSize(), j % board.getSize()));
	        return getPath(path, board, P1, new Position(predecessors[i][j] / board.getSize(), predecessors[i][j] % board.getSize()));
	    }
	}
	
	public int getDistances(Board board, Position P1, Position P2) {
		return distances[(P1.getX() * board.getSize()) + P1.getY()][(P2.getX() * board.getSize()) + P2.getY()];
	}
}