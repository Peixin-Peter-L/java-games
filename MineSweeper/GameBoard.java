import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Random Place mine
 * count mines
 * play
 */
/*
 * left click
 * if isMine then lose
 * if no mine and >0 mines, show itself
 * if no mine around, open area
 * if opened number  = total - mine, win 
 */
public class GameBoard extends JFrame{
	// Game information
	// 8x8 10
	// 16x16 40
	// 24x24 90
	private final int TILE_WIDTH = 30;
	
	private int grid_width = 16,
				grid_height = 16;
	
	private Tile[][] grid;
	
	private JPanel game;
	
	private int max_mine = 40; 
	private int display_mine = 0;
	private boolean gameEnd = false;
	private int opened_Tile = 0;
	
	public GameBoard(){
		super("MineSweeper");
		gameInit();
		graphicInit();
	}
	private void gameInit(){
		game = new JPanel(null);
		grid = new Tile[grid_width][grid_height];		
		for (int x=0;x<grid_width;x++){
			for (int y=0;y<grid_height;y++)
				grid[x][y] = new Tile(this, game, x, y, TILE_WIDTH);
		}
		game.setSize(grid_width*TILE_WIDTH+1, grid_height*TILE_WIDTH+1);
		startNewGame();
	}
	
	private int countMine(int x, int y){
		int counter=0;
		if (x>0)
			counter+=grid[x-1][y].isMine()?1:0;
		if (x<grid_width-1)
			counter+=grid[x+1][y].isMine()?1:0;
		if (y>0)
			counter+=grid[x][y-1].isMine()?1:0;
		if (y<grid_height-1)
			counter+=grid[x][y+1].isMine()?1:0;
		if (x>0 && y>0)
			counter+=grid[x-1][y-1].isMine()?1:0;
		if (x>0 && y<grid_height-1)
			counter+=grid[x-1][y+1].isMine()?1:0;
		if (x<grid_width-1 && y>0)
			counter+=grid[x+1][y-1].isMine()?1:0;
		if (x<grid_width-1 && y<grid_height-1)
			counter+=grid[x+1][y+1].isMine()?1:0;
		return counter;
	}
	
	
	private void graphicInit(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.add(game);
		Insets insets = this.getInsets();
		this.setSize(insets.left + insets.right + game.getWidth(),
					 insets.top + insets.bottom + game.getHeight());

	}
	private void startNewGame(){
		display_mine = 0;
		opened_Tile = 0;
		for (int x=0;x<grid_width;x++){
			for (int y=0;y<grid_height;y++)
				grid[x][y].reset();
		}
		int randX,randY;
		while(display_mine<max_mine){
			randX = (int)(Math.random()*grid_width);
			randY = (int)(Math.random()*grid_height);
			if (!grid[randX][randY].isMine()){
				grid[randX][randY].setMine();
				display_mine++;
			}
		}
		for (int x=0;x<grid_width;x++){
			for (int y=0;y<grid_height;y++){
				if (!grid[x][y].isMine()){
					grid[x][y].setMineNumber(countMine(x,y));
				}
			}
		}
	}
	
	private void gameOver(){
		game.repaint();
		System.out.println("Game Over");
		gameEnd = true;
		try{
			Thread.sleep(2000);
		}catch(Exception e){
			
		}
		System.out.println("Start New Game");
		startNewGame();
		game.repaint();
		gameEnd = false;
	}
	// true - left, false - together
	public void openCheck(int x, int y, boolean type){
		if (gameEnd) return;
		if (type){
			openCount(x,y);
			openAroundRecursion(x, y);
		}else{
			openAround(x,y);
		}
	}
	private int countFlag(int x, int y){
		int counter=0;
		if (x>0)
			counter+=grid[x-1][y].hasFlag()?1:0;
		if (x<grid_width-1)
			counter+=grid[x+1][y].hasFlag()?1:0;
		if (y>0)
			counter+=grid[x][y-1].hasFlag()?1:0;
		if (y<grid_height-1)
			counter+=grid[x][y+1].hasFlag()?1:0;
		if (x>0 && y>0)
			counter+=grid[x-1][y-1].hasFlag()?1:0;
		if (x>0 && y<grid_height-1)
			counter+=grid[x-1][y+1].hasFlag()?1:0;
		if (x<grid_width-1 && y>0)
			counter+=grid[x+1][y-1].hasFlag()?1:0;
		if (x<grid_width-1 && y<grid_height-1)
			counter+=grid[x+1][y+1].hasFlag()?1:0;
		return counter;
	}
	private void openAround(int x, int y){
		if (countFlag(x,y)==grid[x][y].getMineNumber()){
			if (x>0){
				openCount(x-1,y);
				openAroundRecursion(x-1,y);
			}
			if (x<grid_width-1){
				openCount(x+1,y);
				openAroundRecursion(x+1,y);
			}
			if (y>0){
				openCount(x,y-1);
				openAroundRecursion(x,y-1);
			}
			if (y<grid_height-1){
				openCount(x,y+1);
				openAroundRecursion(x,y+1);
			}
			if (x>0 && y>0){
				openCount(x-1,y-1);
				openAroundRecursion(x-1,y-1);
			}
			if (x>0 && y<grid_height-1){
				openCount(x-1,y+1);
				openAroundRecursion(x-1,y+1);
			}
			if (x<grid_width-1 && y>0){
				openCount(x+1,y-1);
				openAroundRecursion(x+1,y-1);
			}
			if (x<grid_width-1 && y<grid_height-1){
				openCount(x+1,y+1);
				openAroundRecursion(x+1,y+1);
			}
		}
	}
	private void openAroundRecursion(int x, int y){
		if (x>=0 && x<grid_width && y>=0 && y<grid_height && grid[x][y].getMineNumber()==0){
			openCount(x,y);
			if (x>0){
				// Left x-1
				if (grid[x-1][y].getMineNumber()==0 && !grid[x-1][y].isOpen())
					openAroundRecursion(x-1,y);
				openCount(x-1,y);
				// Top Left x-1 y-1
				if (y>0){ 
					if (grid[x-1][y-1].getMineNumber()==0 && !grid[x-1][y-1].isOpen())
						openAroundRecursion(x-1,y-1);
					openCount(x-1,y-1);
				}
				// Bottom Left x-1 y+1
				if(y<grid_height-1){
					if (grid[x-1][y+1].getMineNumber()==0 && !grid[x-1][y+1].isOpen())
						openAroundRecursion(x-1,y+1);
					openCount(x-1,y+1);
				}
			}
			if (x<grid_width-1){
				// Right - x+1
				if (grid[x+1][y].getMineNumber()==0 && !grid[x+1][y].isOpen())
					openAroundRecursion(x+1,y);
				openCount(x+1,y);
				// Top y-1
				if (y>0){ 
					if (grid[x+1][y-1].getMineNumber()==0 && !grid[x+1][y-1].isOpen())
						openAroundRecursion(x+1,y-1);
					openCount(x+1,y-1);
				}
				// Bottom y+1
				if(y<grid_height-1){
					if (grid[x+1][y+1].getMineNumber()==0 && !grid[x+1][y+1].isOpen())
						openAroundRecursion(x+1,y+1);
					openCount(x+1,y+1);
				}
			}
			// Top y-1
			if (y>0){ 
				if (grid[x][y-1].getMineNumber()==0 && !grid[x][y-1].isOpen())
					openAroundRecursion(x,y-1);
				openCount(x,y-1);
			}
			// Bottom y+1
			if(y<grid_height-1){
				if (grid[x][y+1].getMineNumber()==0 && !grid[x][y+1].isOpen())
					openAroundRecursion(x,y+1);
				openCount(x,y+1);
			}
		}
	}
	
	private void openCount(int x, int y){
		if (grid[x][y].isOpen() || grid[x][y].hasFlag()) return;
		grid[x][y].open();
		if (grid[x][y].isMine()){
			gameOver();
		}else{
			opened_Tile++;
			if (opened_Tile == grid_width*grid_height - max_mine){
				System.out.println("You Win");
				gameEnd = true;
			}
		}
	}
	
	public static void main(String[] args){
		GameBoard gb = new GameBoard();
	}
}
