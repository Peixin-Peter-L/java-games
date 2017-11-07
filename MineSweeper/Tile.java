import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Tile extends JPanel{
	private int tile_width;
	private int mine_count;
	private boolean isOpen;
	private boolean isMine;
	private boolean hasFlag;
	private int x, y;
	
	private boolean left, right;
	private MouseListener ml;
	private GameBoard main;
	
	public Tile(GameBoard board, JPanel game,int x, int y, int width){
		super(null);
		this.x = x;
		this.y = y;
		this.tile_width = width;
		this.main = board;
		reset();
		this.setBounds(x*tile_width, y*tile_width, tile_width, tile_width);
		this.repaint();
		
		listenerInit();
		game.add(this);
	}
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.GRAY);
		if (isOpen) {
			g.setColor(Color.DARK_GRAY);
			if (isMine) g.setColor(Color.RED);
		}
//		if (isMine && !isOpen) g.setColor(Color.YELLOW);
		g.fillRect(0, 0, tile_width, tile_width);
		
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, tile_width, tile_width);		
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("default",Font.BOLD, tile_width/2));
		if (isOpen && !isMine && mine_count>0)
			g.drawString(mine_count+"", tile_width/2-tile_width/8, this.getHeight()-tile_width/4);
		if (!isOpen && hasFlag)
			g.drawString("F", tile_width/2-tile_width/8, this.getHeight()-tile_width/4);
		
		
	}
	
	private void listenerInit(){
		ml = new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isOpen){
					switch (e.getButton()){
					case 1:
						main.openCheck(x,y,true);
						break;
					case 3:
						hasFlag = !hasFlag;
						break;
					}
					update();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			@Override
			public void mousePressed(MouseEvent e) {
				switch(e.getButton()){
				case 1:
					left = true;
					break;
				case 3:
					right = true;
					break;
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (left && right){
					left = false;
					right = false;
					if (isOpen){
						main.openCheck(x,y,false);
					}
				}
			}
		};
		this.addMouseListener(ml);
	}
	private void update(){
		this.repaint();
	}
	
	public void open(){
		isOpen = true;
		this.repaint();
	}

	public boolean isMine(){
		return isMine;
	}
	
	public void setMine(){
		isMine = true;
		mine_count = -1;
	}
	public void setMineNumber(int num){
		mine_count = num;
	}
	public int getMineNumber(){
		return mine_count;
	}
	public boolean isOpen(){
		return isOpen;
	}
	public boolean hasFlag(){
		return hasFlag;
	}
	public void reset(){
		mine_count = 0;
		isMine = false;
		hasFlag = false;
		isOpen = false;
	}
}
