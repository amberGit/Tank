package util;

import tank.ITank;
import bullet.IBullet;

public interface ITankFactory {
	public String tankPakage = "tank.";
	public String bulletPakage = "bullet.";
	public  ITank createTank(String className);
	public 	IBullet createWeapon(String className);
}
