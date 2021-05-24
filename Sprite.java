import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle 
{
	boolean dead = false;
	final String type;
	
	Sprite(int x, int y, int w, int h, String type, Color color)
	{
		super(w, h, color);
		this.type = type;
		setTranslateX(x);
		setTranslateY(y);
	}
		
	void moveLeft() 
	{
		setTranslateX(getTranslateX() - 5);
	}
	
	void moveRight() 
	{
		setTranslateX(getTranslateX() + 5);
	}
	
	void moveUp() 
	{
		setTranslateX(getTranslateY() - 5);
	}
	
	void moveDown() 
	{
		setTranslateX(getTranslateY() + 5);
	}
}