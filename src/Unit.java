import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Random;

public class Unit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2458715509419927927L;
	static int currentId = 0;
	double x, y;
	double width, height;
	double targetX, targetY;
	double speed = 10;
	double health = 1;
	double damage = 0.5;
	int id;

	Unit() {
		id = currentId;
		currentId++;
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
		g.setColor(Color.BLACK);
		g.drawRect((int)x, (int)y, (int)width, (int)height);
		g.setColor(Color.BLACK);
		g.fillRect((int)x, (int)y, (int)(width*health), (int)(height/5));
	}

	public void damage(double delta, Unit unit) {
		unit.health = Math.max(0, unit.health - delta*this.damage*this.overlap(unit)/width/height);
	}

	public double overlap(Unit unit) {
		double xOverlap , yOverlap;
		if (this.x <= unit.x) {
			xOverlap = Math.min(this.x + this.width - unit.x, unit.width);
		} else {
			xOverlap = Math.min(unit.x + unit.width - this.x, this.width);
		}
		if (this.y <= unit.y) {
			yOverlap = Math.min(this.y + this.height - unit.y, unit.height);
		} else {
			yOverlap = Math.min(unit.y + unit.height - this.y, this.height);
		}
		return Math.max(xOverlap,0) * Math.max(yOverlap, 0);
	}
}
