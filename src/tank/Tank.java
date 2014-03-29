package tank;

import java.util.Vector;

import engine.GameEngine;
import bullet.Bullet;
import UI.GameStartUI;

public class Tank extends Thread {
	public int type;// 坦克类型 0:老巢 大于0：玩家坦克 小于0：敌方坦克（电脑）
	public int x, y;// 坦克的当前位置
	public int size = 16;// 坦克的大小
	public int vup = 0;// 坦克当前向上行驶速度
	public int vdown = 0;// 坦克当前向下的速度
	public int vleft = 0;// 当前 左速度
	public int vright = 0;// 当前 右速度
	public int v = 3;// 没有阻碍物时的速度
	public int status = 1;// 坦克当前的状态 0：销毁 1:存活
	public int direction;// 坦克当前前进方向
	public boolean enemyFire = false;// 敌方坦克能否开火
	public boolean isStop;
	public boolean isSuspend;

	/**
	 * 坦克移动的方法(仅限玩家坦克)
	 */
	public void playerMove() {
		if (GameStartUI.isUp) {
			y -= vup;
		} else if (GameStartUI.isDown) {
			y += vdown;
		} else if (GameStartUI.isLeft) {
			x -= vleft;
		} else if (GameStartUI.isRight) {
			x += vright;
		}
	}

	/**
	 * 敌方坦克移动
	 */
	public void enemyMove() {
		switch (direction) {
		case 1:
			y -= vup;
			break;
		case 2:
			x -= vleft;
			break;
		case 3:
			y += vdown;
			break;
		case 4:
			x += vright;
		}
	}

	// 坦克开枪
	public void fire() {
		// 创建子弹线程
		Bullet bullet = new Bullet(type, x + 1, y + 1);
		bullet.direction = direction - 1;// 获得子弹方向
		// 子弹速度
		switch (direction) {
		case 1:
			bullet.vx = -bullet.v;
			break;
		case 2:
			bullet.vy = -bullet.v;
			break;
		case 3:
			bullet.vx = bullet.v;
			break;
		case 4:
			bullet.vy = bullet.v;
		}
		bullet.start();// 启动子弹线程
		GameEngine.bulletArray.add(bullet);// 添加子弹到子弹队列
	}

	/**
	 * 坦克触碰
	 */
	public void tankCollision() {
		// 坦克和坦克的触碰
		Vector<Tank> Array = GameEngine.tankArray;
		for (int i = 0; i < Array.size(); i++) {
			if (this != Array.get(i)) {
				if (this.x + this.size > Array.get(i).x
						&& this.x < Array.get(i).x + Array.get(i).size
						&& this.y + this.size > Array.get(i).y
						&& this.y < Array.get(i).y + Array.get(i).size) {
					switch (this.direction) {
					case 1:// 本tank方向向左，即本tank在其他坦克的右边
						this.x = Array.get(i).x + Array.get(i).size;
						this.vleft = 0;
						break;
					case 2:// 方向向上
						this.y = Array.get(i).y + Array.get(i).size;
						this.vup = 0;
						break;
					case 3:// 方向向右
						this.x = Array.get(i).x - this.size;
						this.vright = 0;
						break;
					case 4:// 方向向下
						this.y = Array.get(i).y - this.size;
						this.vdown = 0;
					}
				} else {
					if (this.x + this.size == Array.get(i).x
							&& this.y + this.size > Array.get(i).y
							&& this.y < Array.get(i).y + Array.get(i).size) {

						// 本坦克向右紧贴着其他的坦克
						this.vleft = this.v;
						this.vup = this.v;
						this.vright = 0;
						this.vdown = this.v;
					} else if (this.x == Array.get(i).x + Array.get(i).size
							&& this.y + this.size > Array.get(i).y
							&& this.y < Array.get(i).y + Array.get(i).size) {

						// 本坦克向左紧贴着其他的坦克
						this.vleft = 0;
						this.vup = this.vright = this.vdown = this.v;
					} else if (this.y + this.size == Array.get(i).y
							&& this.x + this.size > Array.get(i).x
							&& this.x < Array.get(i).x + Array.get(i).size) {

						// 本坦克向下紧贴着其他的坦克
						this.vdown = 0;
						this.vleft = this.vup = this.vright = this.v;
					} else if (this.y == Array.get(i).y + Array.get(i).size
							&& this.x + this.size > Array.get(i).x
							&& this.x < Array.get(i).x + Array.get(i).size) {

						// 本坦克向上紧贴这其他的坦克
						this.vup = 0;
						this.vleft = this.vright = this.vdown = this.v;
					} else {

						this.vleft = this.vup = this.vright = this.vdown = this.v;
					}
				}
			}
		}
		// 边缘触碰
		if (this.x <= 0) {
			this.x = 0;
			this.vleft = 0;
		} else if (this.x + this.size >= 240) {
			this.x = 240 - this.size;
			this.vright = 0;
		} else if (this.x > 0 && this.x + size < 240) {
			if (this.vleft == 0) {
				this.vleft = this.v;
			} else if (this.vright == 0) {
				this.vright = this.v;
			}
		}

		if (this.y <= 0) {
			this.y = 0;
			this.vup = 0;
		} else if (this.y + size >= 320) {
			this.y = 320 - size;
			this.vdown = 0;
		} else if (this.y > 0 && this.y + size < 320) {
			if (this.vdown == 0) {
				this.vdown = this.v;
			} else if (this.vup == 0) {
				this.vup = this.v;
			}
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

	/**
	 * 线程运行的方法
	 */
	public void run() {
		boolean iscd = true;// 发子弹是否冷却
		long fireTime = 0;// 发子弹那一刻的时间
		boolean cd = true;// 敌方坦克子弹冷却时间
		long time = 0;// 敌方坦克发子弹的那一刻时间
		while (!isStop) {
			if (!isSuspend) {
				tankCollision();
				if (type > 0) {
					playerMove();
				} else if (type < 0) {
					enemyMove();
					System.out.println("move");
				}
				if (GameStartUI.isFire && type > 0) {

					if (iscd) {// 如果冷却时间到了就可以重新发子弹了
						fire();
						iscd = false;
						fireTime = System.currentTimeMillis();// 得到当前时间戳
					} else if (System.currentTimeMillis() - fireTime > 500) {
						// 发子弹时间间隔为一秒
						iscd = true;
					}
				} else if (type < 0 && enemyFire) {
					System.out.println("fire");
					if (cd) {
						fire();
						cd = false;
						time = System.currentTimeMillis();
					} else if (System.currentTimeMillis() - time > 200) {
						cd = true;
					}
				}
			}
			try {
				Thread.sleep(60);
			} catch (Exception ef) {
				ef.printStackTrace();
			}
		}
	}
}
