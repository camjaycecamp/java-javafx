/*
 * cameron campbell
 * advanced java
 * occc spring 2021
 * bonus: java fx
 */

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.*;

/*
 * to test the waters of making a game in java fx, i decided to start
 * off with something simple: space invaders. i followed a few guides
 * online to get an idea of how java fx differs from swing, so this
 * program should establish the foundation i'll use to develop my 
 * final project.
 */
public class JavaFXGame extends Application
{
	// private fields for the pane, time, and player sprite
	private Pane p = new Pane();
	private double t = 0;
	private Sprite player = new Sprite(300, 750, 40, 40, "player", Color.BLUE);
	
	/*
	 * method that sets up the fundamental parameters and procedures
	 * of the application before returning the pane as an acting parent
	 */
	private Parent createContent() 
	{
		p.setPrefSize(600, 800);
		
		p.getChildren().add(player);
		
		AnimationTimer t = new AnimationTimer() 
		{
			@Override
			public void handle(long now) 
			{
				update();
			}
		};
		
		t.start();
		
		nextLevel();
		
		return p;
	}
	
	// main method
	public static void main(String[] args) 
	{
		/*
		 *  method from Application allowing us to go into Application
		 *  and set up our application by calling the start method
		 */
		launch(args);
	}
	
	/*
	 * moves onto the next level if all enemies have been defeated, respawning
	 * five more in their place
	 */
	private void nextLevel() 
	{
		for (int i = 0; i < 5; i++) 
		{
			Sprite s = new Sprite(90 + i*100, 150, 30, 30, "enemy", Color.RED);
			
			p.getChildren().add(s);
		}
	}
	
	/*
	 * method that collects all sprites and stores them into a list. is useful for
	 * case checks in the update() method
	 */
	private List<Sprite> sprites() 
	{
		return p.getChildren().stream().map(n -> (Sprite)n).collect(Collectors.toList());
	}
	
	/*
	 * general update method for all time-sensitive actions and procedures in-game
	 */
	private void update() 
	{
		// as the game progresses, increment the time with updates
		t += 0.016;
		
		/*
		 * update cases for enemy bullets, player bullets, and the enemies themselves
		 */
		sprites().forEach(s -> 
		{
			switch (s.type) 
			{
				case "enemybullet":
					s.moveDown();
					
					// if enemy bullet collides with player
					if(s.getBoundsInParent().intersects(player.getBoundsInParent())) 
					{
						player.dead = true;
						s.dead = true;
					}
					break;
					
				case "playerbullet":
					s.moveUp();
					
					// if player bullet collides with enemy
					sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> 
					{
						if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) 
						{
							enemy.dead = true;
							s.dead = true;
						}
					});
					break;
					
				case "enemy":
					// if at least 2 seconds have passed and the enemy passes a 30 percent chance
					if(t > 2) 
					{
						if (Math.random() < 0.3) 
						{
							shoot(s);
						}
					}
					break;
			};
		});
		
		// clean up the dead by removing them from the screen
		p.getChildren().removeIf(n -> 
		{
			Sprite s = (Sprite) n;
			return s.dead;
		});
		
		// reset time counter once two seconds have passed
		if(t > 2) 
		{
			t = 0;
		}
	}
	
	// method for creating a sprite representing a bullet fired by the passed sprite
	private void shoot(Sprite who) 
	{
		Sprite s = new Sprite((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 5, 20, who.type + "bullet", Color.BLACK);
		
		p.getChildren().add(s);
	}
	
	// implemented Applicaton method
	@Override
	public void start(Stage arg0)
	{
		Scene sc = new Scene(createContent());
		
		// various switch cases for player controls
		sc.setOnKeyPressed(e -> {
			switch (e.getCode()) 
			{
			case A:
				player.moveLeft();
				break;
			case D:
				player.moveRight();
				break;
			case SPACE:
				shoot(player);
				break;
			default:
			}
		});
		
		// sets up the stage and scene
		arg0.setScene(sc);
		arg0.show();
	}
}
