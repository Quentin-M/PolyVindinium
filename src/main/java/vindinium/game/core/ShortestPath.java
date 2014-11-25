package vindinium.game.core;

import java.util.ArrayList;
import java.lang.Math;

public class ShortestPath {
	private ArrayList<Position> chemin;

	public ArrayList<Position> getChemin(){
		return chemin;
	}
	
	public ShortestPath(){
		chemin = new ArrayList<Position>();
	}
	
	public int getDistance(){
		return chemin.size();
	}
	/**
	 * A class that helps to 
	 * 	- find the best neighbor (f = g + h with 
	 * 		g = cost to go from the starting point to this point
	 * 		h = Manhattan distance between this point and the end)
	 * 	- reconstruct the path (cameFrom)
	 */
	public class Point{
		private Position position;
		private Point cameFrom;
		private int g;
		private int f;
		
		public Point(){
			position = null;
			cameFrom = null;
			g = 0;
			f = 0;
		}
		
		public Point(Position newPosition, Point newCameFrom, int newG, int newF) {
			position = newPosition;
			cameFrom = newCameFrom;
			g = newG;
			f = newF;
		}
		
		public Position getPosition(){
			return position;
		}
		
		public Point getCameFrom(){
			return cameFrom;
		}
		
		public int getG(){
			return g;
		}
		
		public int getF(){
			return f;
		}
		
		public void setPosition(Position newPosition){
			position = newPosition;
		}
		
		public void setCameFrom(Point newCameFrom){
			cameFrom = newCameFrom;
		}
		
		public void setG(int newG){
			g = newG;
		}
		
		public void setF(int newF){
			f = newF;
		}
	}
	
	/**
	 * aStar algorithm
	 * @param end
	 * @param board
	 * @param game
	 */
	public void aStar(Position end, Board board, Game game){
		Position heroPosition = game.getHero().getPosition(), neighborPosition = null;
		Point currentPoint = new Point(heroPosition, null, 0, manhattanDistance(heroPosition, end));
		int newG = 0, currentPositionX = 0, currentPositionY = 0, rechercheCloseList = 0, rechercheOpenList = 0;
		
		ArrayList <Point> openList = new ArrayList<Point>();
		openList.add(currentPoint);
		
		ArrayList <Point> closeList = new ArrayList<Point>();
		
		while(openList.isEmpty() != true){
			//trouvons le point courant = point ayant le plus petit f
			currentPoint = openList.get(0);
			for(int i = 1; i < openList.size(); i++){
				if(openList.get(i).getF() < currentPoint.getF()){
					currentPoint = openList.get(i);
				}
			}
			
			if(currentPoint.getPosition().equals(end)){
				//reconstitution du chemin si l'on a réussi à arriver jusqu'au point d'arrivée
				Point parent = currentPoint;
				while(parent != null){
					chemin.add(0, parent.getPosition());
					parent = parent.getCameFrom();
				}
				displayPath(board);
				break;
			}
			else{
				openList.remove(currentPoint);
				closeList.add(currentPoint);
				
				//on prend en compte les 4 positions voisines
				currentPositionX = currentPoint.getPosition().getX();
				currentPositionY = currentPoint.getPosition().getY();
				Position p1 = new Position(currentPositionX+1, currentPositionY);
				Position p2 = new Position(currentPositionX-1, currentPositionY);
				Position p3 = new Position(currentPositionX, currentPositionY+1);
				Position p4 = new Position(currentPositionX, currentPositionY-1);
				
				ArrayList<Position> neighborsPosition = new ArrayList<Position>();
				neighborsPosition.add(p1);
				neighborsPosition.add(p2);
				neighborsPosition.add(p3);
				neighborsPosition.add(p4);
	            for(int j = 0; j <= 3; j++){
	            	//traitement pour chaque voisin
	            	neighborPosition = neighborsPosition.get(j);
					if(isAccessible(neighborPosition, end, board)){
						rechercheCloseList = 0;
						for(int k = 0; k < closeList.size(); k++){
							if(closeList.get(k).getPosition().equals(neighborPosition)){
								rechercheCloseList = 1;
							}
						}
						if(rechercheCloseList == 0){
							newG = currentPoint.getG() + 1;
							Point neighbour = new Point();
							rechercheOpenList = 0;
							for(int l = 0; l < openList.size(); l++){
								if(openList.get(l).getPosition().equals(neighborPosition)){
									rechercheOpenList = 1;
									neighbour = openList.get(l);
								}
							}
							if(rechercheOpenList == 0){
								//si le voisin n'est pas dans l'openListe, on l'y ajoute
								neighbour = new Point(neighborPosition, currentPoint, newG, newG + manhattanDistance(neighborPosition, end));
								openList.add(neighbour);
							}
							else if(newG < neighbour.getG() && rechercheOpenList == 1){
								//s'il se trouve dans l'openList et que le nouveau g est plus petit pour ce voisin, on l'update -> on a ainsi trouvé
								//un chemin plus court pour accéeder à ce voisin
								neighbour.setCameFrom(currentPoint);
                            	neighbour.setG(newG);
                            	neighbour.setF(newG + manhattanDistance(neighborPosition, end));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Compute the Manhattan distance between start and end
	 * @param start
	 * @param end
	 * @return the Manhattan distance between start and end
	 */
	public int manhattanDistance(Position start, Position end){
		return Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
	}
	
	/**
	 * Permit to find if the position is accessible
	 * @param current
	 * @param board
	 * @return true if the position is accessible
	 */
	public boolean isAccessible(Position current, Position end, Board board)// -> en fonction des tiles accessibles et des spawns ennemis
	{
		if(current.equals(end)){
			return true;
		}
		if(current.getX() < 0 || current.getX() >= board.getSize() || current.getY() < 0 || current.getY() >= board.getSize()){
			return false;
		}
		else{
			Tile myTile = board.getTile(current.getX(), current.getY());
			return !(myTile == Tile.WOODS
				|| myTile == Tile.TAVERN 
				|| myTile == Tile.MINE_HERO0 
				|| myTile == Tile.MINE_HERO1 
				|| myTile == Tile.MINE_HERO2 
				|| myTile == Tile.MINE_HERO3
				|| myTile == Tile.MINE_NEUTRAL
				|| myTile == Tile.HERO0
				|| myTile == Tile.HERO1
				|| myTile == Tile.HERO2
				|| myTile == Tile.HERO3);
		}
			
	}
	
	/**
	 * display the solution path
	 * @param chemin
	 * @param board
	 */
	public void displayPath(Board board){
		int boardSize = board.getSize(), trouve = 0;
		
		System.out.println("Solution path");
		System.out.println("Distance de ce chemin: " + chemin.size());
		for(int x = 0; x < boardSize; x++) {
			System.out.print("|");
			for(int y = 0; y < boardSize; y++) {
				trouve = 0;
				for(int z = 0; z < chemin.size(); z++){
					if(chemin.get(z).getX() == x && chemin.get(z).getY() == y){
						trouve = 1;
					}
				}
				if(trouve == 1){
					System.out.print("X");
				}
				else{
					System.out.print(" ");
				}
			}
			System.out.println("|");
		}
	}
}
