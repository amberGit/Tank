package ai;

import java.util.Random;

import tank.Tank;
import engine.GameEngine;

/**
 * 坦克人工智能
 * 
 * @author john
 * 
 */
public class EnemyTankAI extends Thread {
	public boolean isStop = false;
	public boolean isSuspend = false;
	public Tank t;

	public EnemyTankAI(Tank t) {
		this.t = t;
	}

	/**
	 * 攻击玩家坦克
	 * 
	 * @param t
	 *            地方坦克
	 */
	public void atk() {
		// 把枪口对准玩家坦克
		if (GameEngine.playerTank.x >= t.x
				&& GameEngine.playerTank.x <= t.x + t.size) {
			if (t.y < GameEngine.playerTank.y) {
				t.direction = 4;
			} else {
				t.direction = 2;

			}
			t.enemyFire = true;
		} else if (GameEngine.playerTank.y >= t.y
				&& GameEngine.playerTank.y <= t.y + t.size) {
			if (t.x < GameEngine.playerTank.x) {
				t.direction = 3;

			} else {
				t.direction = 1;

			}
			t.enemyFire = true;
		}
		t.enemyFire = false;
	}

	/**
	 * 敌方坦克漫游
	 */
	public void randWay() {
		Random rand = new Random();
		t.direction = rand.nextInt(4) + 1;
	}

	public void randFire() {
		Random rand = new Random();
		int temp = rand.nextInt(2);
		if (temp == 0) {
			t.enemyFire = false;
		} else {
			t.enemyFire = true;
		}
	}

	public void run() {
		//atk();// 打玩家坦克
		randWay();// 随机方向乱走路
		//randFire();// 乱开枪
	}
}
