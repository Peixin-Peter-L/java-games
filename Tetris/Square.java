import java.awt.Color;

public class Square {
	/*
	 * I - Cyan 	1111 	0,255,255 
	 * J - Blue 	111 	0,0,255 
	 * 			  	  1 
	 * L - Orange 	111 	255,165,0 
	 * 			  	1
	 * O - Yellow 	11 		255,255,0 
	 * 			  	11 
	 * S - Green 	11 		0,255,0 
	 * 			   11 
	 * T - Purple 	111		128,0,128 
	 * 			     1 
	 * Z - Red 		11 		255,0,0 
	 * 			   11
	 */

	private final Color[] COLORS = {new Color(0, 255, 255), new Color(0, 0, 255), new Color(255, 165, 0),
			new Color(255, 255, 0), new Color(0, 255, 0), new Color(128, 0, 128), new Color(255, 0, 0)};
	private final Color BACKGROUND_COLOR = new Color(0,0,0); 

	private Color color;

	public Square() {
		this.color = BACKGROUND_COLOR;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(int color){
		this.color = color%10==-1?BACKGROUND_COLOR:COLORS[color%10];
	}
}
