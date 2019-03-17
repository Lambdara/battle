import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Unit {

	double x, y;
	double width, height;
	double targetX, targetY;
	double speed = 10;

	Unit() {
		Random generator = new Random(System.nanoTime());
		x = generator.nextDouble() * 600 + 100;
		y = generator.nextDouble() * 400 + 100;
		targetX = generator.nextDouble() * 600 + 100;
		targetY = generator.nextDouble() * 400 + 100;
		width = 50;
		height = 20;
	}
	
	public void update(double delta) {
		// Direction
		double diffX = targetX - x;
		double diffY = targetY - y;
		// Measure distance, return if not moving
		double total = Math.sqrt(diffX * diffX + diffY * diffY);
		diffX /= total;
		diffY /= total;
		System.out.println();
		if (total == 0)
			return;
		
		// Move
		double distanceThisTurn = speed * delta;
		if (distanceThisTurn > total) {
			x = targetX;
			y = targetY;
		} else {
			// TODO This is not exactly right but will do for now
			x += diffX * distanceThisTurn;
			y += diffY * distanceThisTurn;
		}
	}
	
	public void paint(Graphics g, Color c) {
		g.setColor(c);
		g.fillRect((int)x, (int)y, (int)width, (int)height);
	}
}
