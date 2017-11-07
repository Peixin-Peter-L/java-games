import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

public class TetrisBoard extends JFrame{
	/*
	 * I - Cyan 	1111 	0,255,255	0
	 * J - Blue 	111 	0,0,255		1
	 * 			  	  1 
	 * L - Orange 	111 	255,165,0 	2
	 * 			  	1
	 * O - Yellow 	11 		255,255,0 	3
	 * 			  	11 
	 * S - Green 	11 		0,255,0 	4
	 * 			   11 
	 * T - Purple 	111		128,0,128 	5
	 * 			     1 
	 * Z - Red 		11 		255,0,0 	6
	 * 			   11
	 */
	
	// Constants
	private final Color[] COLORS = {new Color(0, 255, 255), new Color(0, 0, 255), new Color(255, 165, 0),
			new Color(255, 255, 0), new Color(0, 255, 0), new Color(128, 0, 128), new Color(255, 0, 0)};
	private final Color BACKGROUND_COLOR = new Color(0,0,0); 
	private int INTERVAL = 100;
	private int width = 40;
	
	// Graphic Variables
	private int panel_width = width*10, panel_height = width*20;
	// Game Variables
	private int[][] grid = new int[22][10];
	
	private int row, col;
	private int bWidth, type;
	
	private boolean pressRight = false, pressLeft = false, 
					pressUp = false, pressDown = false;
	private int wait=0;
	private int score=0;
	
	// Graphic Instances
	private JPanel panel;
	private Timer timer;
	private KeyListener keyListener;
	private JLabel scoreL;
	
	public TetrisBoard(){
		// Graphic Setup
		super("Tetris");
		graphicInit();
		listenerInit();
		//
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[i].length; j++){
				grid[i][j] = -1;
			}
		}
//		grid[19][4] = 11;
		createBlock();
		timerInit();
	}
	
	private void createBlock(){
		row = 0;
		col = 4;
		type = (int)(Math.random()*7);
//		Scanner sc=new Scanner(System.in);
//		System.out.print("Input a type of block: ");
//		type = sc.nextInt();
		switch(type){
		case 0:
			bWidth = 4;
			for(int j=col;j<col+bWidth;j++){
				grid[row+1][j] = type;
			}
			break;
		case 1:
			bWidth = 3;
			grid[row][col] = type;
			for(int j=col;j<col+bWidth;j++){
				grid[row+1][j] = type;
			}
			break;
		case 2:
			bWidth = 3;			
			grid[row][col+bWidth-1] = type;
			for(int j=col;j<col+bWidth;j++){
				grid[row+1][j] = type;
			}
			break;
		case 3:
			bWidth = 2;
			for(int i=row;i<row+bWidth;i++){
				for(int j=col;j<col+bWidth;j++){
					grid[i][j] = type;
				}
			}
			break;
		case 4:
			bWidth = 3;
			for(int i=1;i<3;i++){
				grid[row][col+i] = type;
				grid[row+1][col+i-1] = type;
			}
			break;
		case 5:
			bWidth = 3;			
			grid[row][col+1] = type;
			for(int j=col;j<col+bWidth;j++){
				grid[row+1][j] = type;
			}
			break;
		case 6:
			bWidth = 3;			
			for(int i=1;i<3;i++){
				grid[row][col+i-1] = type;
				grid[row+1][col+i] = type;
			}
			break;
		}
	}
	private void moveDown(){
		if (canMoveDown()){
			for (int i=Math.min(grid.length, row+bWidth);i>=Math.max(0, row);i--){
				for (int j=Math.max(0, col);j<Math.min(grid[0].length, col+bWidth);j++){
					if (i==0) 
						grid[i][j] = -1;
					else if (i<grid.length && grid[i][j]<10 && grid[i-1][j]<10){
						grid[i][j] = grid[i-1][j];
					}
				}
			}
			row++;
		}else{
			for (int i=Math.max(0, row);i<Math.min(grid.length, row+bWidth);i++){
				for (int j=Math.max(0, col);j<Math.min(grid[0].length, col+bWidth);j++){
					if (grid[i][j]!=-1&&grid[i][j]<10) grid[i][j]+=10;
				}
			}
			for (int i=Math.max(0, row);i<Math.min(grid.length, row+bWidth);i++){
				if (isComplete(i)){
					clearRow(i);
				}
			}
			createBlock();
		}
	}
	private boolean canMoveDown(){
		for (int i=Math.max(0, row);i<Math.min(grid.length, row+bWidth);i++){
			for (int j=Math.max(0, col);j<Math.min(grid[0].length, col+bWidth);j++){
				if (grid[i][j]!=-1&&grid[i][j]<10){
//					print();
					if (i==21) return false;
					if (grid[i+1][j]>=10) return false;
				}
			}
		}
		return true;
	}
	private boolean canMoveLeft(){
		for (int i=Math.max(0, row);i<Math.min(grid.length, row+bWidth);i++){
			for (int j=Math.max(0, col);j<Math.min(grid[0].length, col+bWidth);j++){
				if (grid[i][j]!=-1&&grid[i][j]<10){
					if(j==0) return false;
					if (grid[i][j-1]>=10) return false;
				}
			}
		}
		return true;
	}
	private void moveLeft(){
		if (canMoveLeft()){
			for (int i=Math.max(0, row);i<Math.min(grid.length, row+bWidth);i++){
				for (int j=Math.max(0, col)-1;j<Math.min(grid[0].length, col+bWidth);j++){
					if (j==grid[0].length-1){
						grid[i][j] = -1;
					}else if (j>=0&& grid[i][j]<10 && grid[i][j+1]<10){
						grid[i][j] = grid[i][j+1];
					}
				}
    		}
			col--;
    	}
	}
	private boolean canMoveRight(){
		for (int i=Math.max(0, row);i<Math.min(grid.length, row+bWidth);i++){
			for (int j=Math.max(0, col);j<Math.min(grid[0].length, col+bWidth);j++){
				if (grid[i][j]!=-1&&grid[i][j]<10){
					if (j==grid[0].length-1) return false;
					if (grid[i][j+1]>=10) return false;
				}
			}
		}
		return true;
	}
	private void moveRight(){	
    	if (canMoveRight()){
			for (int i=Math.max(0, row);i<Math.min(grid.length, row+bWidth);i++){
				for (int j=Math.min(grid[0].length, col+bWidth);j>=Math.max(0, col);j--){
					if (j==0){
						grid[i][j] = -1;
					}else if (j<grid[0].length && grid[i][j]<10 &&grid[i][j-1]<10)
						grid[i][j] = grid[i][j-1];
	    		}
	    	}
			col++;
    	}
	}
	private void rotate(){
		if (col<0) {
			for(int i=0;i<=0-col;i++){
				moveRight();
			}
		}else if (col+bWidth>grid[0].length) {
			for(int i=0;i<=col+bWidth-grid[0].length;i++){
				moveLeft();
			}
		}
		int[][] temp = new int[bWidth][bWidth];
		for(int i=row;i<row+bWidth;i++){
			for(int j=col;j<col+bWidth;j++){
				temp[i-row][j-col] = grid[row+bWidth-1-(j-col)][col+i-row];
			}
		}
		for(int i=row;i<row+bWidth;i++){
			for(int j=col;j<col+bWidth;j++){
				if (temp[i-row][j-col]<10&&grid[i][j]>=10) return;
			}
		}
		for(int i=row;i<row+bWidth;i++){
			for(int j=col;j<col+bWidth;j++){
				grid[i][j] = temp[i-row][j-col];
			}
		}
		
	}

	private void clearRow(int endI){
		for(int i=endI;i>1;i--){
			for(int j=0;j<grid[0].length;j++){
				if (grid[i][j]==-1||grid[i][j]>=10)
					grid[i][j] = grid[i-1][j];
			}
		}
		score+=grid[0].length*10;
		scoreL.setText("Your Score: "+score);
	}
	private boolean isComplete(int i){
		for(int j=0;j<grid[0].length;j++){
			if (grid[i][j]==-1) return false;
		}
		return true;
	}
	private void listenerInit(){
		keyListener = new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT ) {
					pressRight = true;
			    } else if (e.getKeyCode() == KeyEvent.VK_LEFT ) {					
			    	pressLeft = true;
			    } else if (e.getKeyCode() == KeyEvent.VK_UP ) {
			    	pressUp = true;
			    } else if (e.getKeyCode() == KeyEvent.VK_DOWN ) {
			    	pressDown = true;
			    }
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT ) {
					pressRight = false;
			    } else if (e.getKeyCode() == KeyEvent.VK_LEFT ) {					
			    	pressLeft = false;
			    } else if (e.getKeyCode() == KeyEvent.VK_UP ) {
			    	pressUp = false;
			    } else if (e.getKeyCode() == KeyEvent.VK_DOWN ) {
			    	pressDown = false;
			    }
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		};
		this.addKeyListener(keyListener);
	}
	
	private void timerInit(){
		timer = new Timer(INTERVAL, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver()){
					timer.stop();
					JOptionPane.showMessageDialog(null,"You Lost");
				}
				wait += INTERVAL;
				if (pressLeft) moveLeft();
				if (pressRight) moveRight();
				if (pressUp) rotate();
				if (pressDown) moveDown();
				if (wait >= 300){
					wait = 0;
					moveDown();
				}
				panel.repaint();
			}
		});
		timer.start();
	}
	private boolean gameOver(){
		for(int j=0;j<grid[1].length;j++){
			if(grid[1][j]>10){
				return true;
			}
		}
		return false;
	}
	private void graphicInit(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0,0,panel_width+150,panel_height+width);
		panel = new JPanel(null){
			public void paint(Graphics g){
				super.paint(g);
				for (int i = 2; i < grid.length; i++){
					for (int j = 0; j < grid[i].length; j++){
						if(grid[i][j]!=-1){
							g.setColor(COLORS[grid[i][j]%10]);
						}else{
							g.setColor(BACKGROUND_COLOR);
						}
						g.fillRect(j*width, (i-2)*width, width, width);
						g.setColor(Color.WHITE);
						g.drawRect(j*width, (i-2)*width, width, width);
					}
				}
			}
		};
		panel.setBounds(0, 0, panel_width, panel_height);
		panel.repaint();
		panel.setBackground(Color.WHITE);
		
		scoreL = new JLabel("Your Score: "+score);
		scoreL.setBackground(Color.orange);
		scoreL.setBounds(panel_width+10,0,100,100);
		panel.add(scoreL);
		
		this.add(panel);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		TetrisBoard tb = new TetrisBoard();
	}

}
