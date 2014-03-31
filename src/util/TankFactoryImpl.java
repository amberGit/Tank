package util;

import tank.ITank;
import bullet.IBullet;

public class TankFactoryImpl implements ITankFactory {
	private static TankFactoryImpl tfi = null;
	private TankFactoryImpl(){
		
	}
	public static TankFactoryImpl getInstance(){
		synchronized(TankFactoryImpl.class){
			if( null == tfi) tfi = new TankFactoryImpl();
		}
		return tfi;
	}
	@Override
	public ITank createTank(String className) {
		Object obj = null;
		try {
			obj = Class.forName(ITankFactory.tankPakage + className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(obj instanceof ITank)
		return (ITank) obj;
		return null;
	}

	@Override
	public IBullet createWeapon(String className) {
		Object obj = null;
		try {
			obj = Class.forName(ITankFactory.bulletPakage + className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(obj instanceof IBullet)return (IBullet) obj;
		return null;
	}

}
