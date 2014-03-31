package tank;

import java.awt.Image;

import util.Direction;
import bullet.IBullet;

public  class TankImpl implements ITank {
	public double x,y;
	public IBullet bullet;
	public double speed;
	public double armor;
	public static Image image;
	public Direction direction ;
	
	public TankImpl(IBullet bullet){
		this.bullet = bullet;
	}
	@Override
	public void move() {
		switch(direction){
		case down: y+=speed;
			break;
		case left: x-=speed;
			break;
		case right:x+=speed;
			break;
		case up: y-=speed;
			break;
		
		}
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfExplotion() {
		// TODO Auto-generated method stub

	}

}
