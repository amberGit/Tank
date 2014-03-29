package engine;

import java.awt.Point;
import java.util.Vector;

import tank.Tank;
import ai.EnemyTankAI;
import bullet.Bullet;

public class GameEngine extends Thread {
	public static Vector<Tank> tankArray = new Vector<Tank>();
	public static Vector<Bullet> bulletArray = new Vector<Bullet>();
	public static boolean isStop = false;
	public static boolean isSuspend = false;
	public static int status;// 游戏当前状态 0:正在进行1:赢了 -1：输了
	public static Tank playerTank;// 玩家控制的坦克
	public static Tank hdQuarter;// 老巢
	public static Point etp[] = new Point[6];

	/**
	 * 游戏开始
	 */
	public void gameStart() {
		status = 0;
		// 创建玩家坦克
		playerTank = new Tank();
		playerTank.type = 1;
		playerTank.x = 123;
		playerTank.y = 160;
		playerTank.start();
		tankArray.add(playerTank);
		// 建老巢
		hdQuarter = new Tank();
		hdQuarter.type = 0;
		// 老巢的起始坐标
		hdQuarter.x = 240 / 2 - 16 / 2;
		hdQuarter.y = 320 - 16;
		hdQuarter.start();// 启动老巢线程
		tankArray.add(hdQuarter);
		// 创建敌方坦克出生点
		for (int i = 0; i < etp.length; i++) {
			etp[i] = new Point(i * 44 + 3, 0);
		}
		// 创建地方坦克
		creatETank();

	}

	/**
	 * 地方坦克的动作
	 */
	public void action() {
		// 敌方坦克的动作
		for (int i = 0; i < tankArray.size(); i++) {
			if (tankArray.get(i).type < 0) {
				EnemyTankAI ai = new EnemyTankAI(tankArray.get(i));
				ai.start();
			}
		}

	}

	/**
	 * 根据敌方坦克出生点创建敌方坦克
	 */
	public void creatETank() {

		// 根据出生点创建敌方坦克
		for (int i = -1; i > -7; i--) {
			boolean find = false;// 记录类型为i 的敌方坦克是否被找到
			for (int j = 0; j < tankArray.size(); j++) {
				if (tankArray.get(j).type == i) {
					find = true;
					break;
				}
			}
			if (find == false) {
				Tank t = new Tank();
				t.direction = 3;
				t.type = i;
				t.x = etp[-(i + 1)].x;
				t.y = etp[-(i + 1)].y;
				// 若已经设置AI则应该在此启动线程，用AI选项控制坦克的行为
				t.start();
				tankArray.add(t);
			}
		}
	}

	/**
	 * 游戏结束
	 */
	public void gameOver() {
		if (hdQuarter.status == 0) {
			// 老巢被爆了
			status = -1;
		}
		int count = 0;
		for (int i = 0; i < tankArray.size(); i++) {
			if (tankArray.get(i).type > 0) {
				count++;
				continue;
			}
		}
		if (count == 0) {
			status = -1;
			stopThread();
			tankArray.clear();
		}
	}

	/**
	 * 过关
	 */
	public void win() {
		int count = 0;// 记录敌方坦克的数量
		for (int i = 0; i < tankArray.size(); i++) {
			if (tankArray.get(i).type < 0) {
				count++;
				continue;
			}
		}
		if (count == 0) {
			// 游戏胜利
			status = 1;
			// stopThread();
			// tankArray.clear();
		}
	}

	public void stopThread() {
		isStop = true;
	}

	public void suspendThread() {
		isSuspend = true;
	}

	public void resumeThread() {
		isSuspend = false;
	}

	public void run() {
		boolean respawn = true;// 出坦克
		long t = 0;// 记录坦克复活时的时间
		while (!isStop) {
			if (!isSuspend) {
				gameOver();
				win();
				action();
				if (respawn) {
					creatETank();// 当敌方坦克有被击毙的，马上从该类型坦克的基地重新生产出一辆来
					respawn = false;
					t = System.currentTimeMillis();
				} else if (System.currentTimeMillis() - t > 5000) {
					// 地方坦克的复活时间为5秒
					respawn = true;
				}

			}
			try {
				Thread.sleep(500);

			} catch (Exception ef) {
				ef.printStackTrace();
			}
		}
	}
}
