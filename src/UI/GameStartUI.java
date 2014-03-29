package UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.GameEngine;
import bullet.Bullet;
import tank.Tank;

/**
 * 玩游戏的界面
 * 
 * @author john
 * 
 */
public class GameStartUI extends JFrame implements Runnable {
	public static boolean isUp = false;
	public static boolean isDown = false;
	public static boolean isLeft = false;
	public static boolean isRight = false;
	public static boolean isFire = false;
	public static int pressed = 2;// 用来记录上次按下的方向键(默认方向向上)
	public boolean isStop = false;
	public boolean isSuspend = false;
	public MyPanel myPanel;

	public void initUI() {
		// 设置窗体
		this.setTitle("坦克大战");
		this.setSize(246, 348);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setFocusable(true);// 设置JFrame 窗体可以获得焦点:键盘监听器必须要在组件获得焦点之后才能运行
		// 创建一个方便重绘的PANEL
		myPanel = new MyPanel();
		this.add(myPanel);
		GameEngine engine = new GameEngine();
		engine.gameStart();// 启动游戏
		engine.start();// 启动游戏引擎线程，开局和判断输赢
		this.setVisible(true);
		// 创建键盘监听器
		ButtonListener butnLis = new ButtonListener();
		this.addKeyListener(butnLis);
		Thread t = new Thread(this);// 启动刷屏线程
		t.start();
	}

	class MyPanel extends JPanel {

		public MyPanel() {
			this.setPreferredSize(new Dimension(240, 320));
		}

		// override 重写JPanel的paint方法，方便画游戏界面
		public void paint(Graphics g) {
			super.paint(g);
			// 绘制游戏界面
			// 双缓冲
			// 1创建虚拟画布
			Image img = this.createImage(this.getPreferredSize().width,
					this.getPreferredSize().height);
			Graphics gr = img.getGraphics();
			// 2将要绘制的图像绘制到虚拟画布上去
			ImageIcon pic = new ImageIcon("images/background.png");
			ImageIcon condition = new ImageIcon("images/infshow.png");
			// 贴地图
			gr.drawImage(pic.getImage(), 0, 0, null);
			// gr.drawImage(condition.getImage(), 0, 234, null);

			// 贴坦克和子弹以及可破坏性物体上去
			Vector<Tank> ta = GameEngine.tankArray;
			Vector<Bullet> ba = GameEngine.bulletArray;
			for (int i = 0; i < ta.size(); i++) {
				// 判断死活
				if (ta.get(i).status == 0) {
					// 坦克已死
					ta.get(i).stopThread();// 结束线程
					ImageIcon movies = new ImageIcon(
							"images/explode_tankmovie_frame01.png");
					gr.drawImage(movies.getImage(), ta.get(i).x, ta.get(i).y,
							null);// 把坦克爆炸的图片贴上去
					ta.remove(i);// 将已经爆炸的坦克从队列中移除
				} else {
					// 选择图片
					ImageIcon tankIcon[] = new ImageIcon[4];// 坦克4个方向的图片
					if (ta.get(i).type > 0) {
						// 大于0 代表是玩家坦克
						for (int j = 0; j < tankIcon.length; j++) {
							tankIcon[j] = new ImageIcon("images/playertank"
									+ ta.get(i).type + (j + 1) + ".png");
						}// 获得上次按的是哪个方向键，根据方向键贴上相印的图片
						gr.drawImage(tankIcon[pressed - 1].getImage(),
								ta.get(i).x, ta.get(i).y, null);
					} else if (ta.get(i).type < 0) {
						// 小于0的是敌方坦克
						for (int j = 0; j < tankIcon.length; j++) {
							tankIcon[j] = new ImageIcon("images/enemytank"
									+ (-(ta.get(i).type)) + (j + 1) + ".png");
						}// 获得上次按的是哪个方向键，根据方向键贴上相印的图片
						gr.drawImage(
								tankIcon[ta.get(i).direction - 1].getImage(),
								ta.get(i).x, ta.get(i).y, null);
					} else {

						// 等于0 是老巢
						ImageIcon image = new ImageIcon(
								"images/headquarters00.png");
						gr.drawImage(image.getImage(), ta.get(i).x,
								ta.get(i).y, null);
					}
				}

			}
			// 贴子弹图片
			for (int i = 0; i < ba.size(); i++) {
				// 判断子弹是否销毁
				if (ba.get(i).status == 0) {
					// 结束子弹线程
					ba.get(i).stopThread();
					// 创建爆炸图片
					ImageIcon explode = new ImageIcon(
							"images/explode_bulletmovie_frame01.png");
					// 将爆炸图片贴到画布上
					gr.drawImage(explode.getImage(), ba.get(i).x, ba.get(i).y,
							null);
					ba.remove(i);// 将销毁的子弹从子弹队列中移除
				} else {
					// 创建子弹方向图片数组
					ImageIcon bimg[] = new ImageIcon[4];
					for (int j = 0; j < 4; j++) {
						bimg[j] = new ImageIcon("images/bullet3" + j + ".png");
					}
					gr.drawImage(bimg[ba.get(i).direction].getImage(),
							ba.get(i).x, ba.get(i).y, null);
				}

			}
			// 游戏暂停，结束的图片
			if (GameEngine.status == -1) {
				// 游戏结束
				// 创建游戏结束的图片
				ImageIcon failImg = new ImageIcon("images/tankgameover.png");
				gr.drawImage(failImg.getImage(), 0, 0, null);// 画到屏幕上去

			} else if (GameEngine.status == 1) {
				// 游戏胜利
				ImageIcon winImg = new ImageIcon("images/");
				// gr.drawImage(winImg.getImage(), 0, 0, null);
			}
			// 3把虚拟画布画到屏幕上去
			g.drawImage(img, 0, 0, null);
		}
	}

	public void stopThread() {
		isStop = true;
	}

	public void suspendThread() {
		isSuspend = true;
	}

	public void run() {
		while (!isStop) {
			if (!isSuspend) {
				myPanel.repaint();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 键盘监听器
	 * 
	 * @author john
	 * 
	 */
	class ButtonListener extends KeyAdapter {

		Tank mt = GameEngine.playerTank;// 玩家控制的坦克

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				// 向上移动
				pressed = 2;
				isUp = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				// 向下移动
				pressed = 4;
				isDown = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				// 向左移动
				pressed = 1;
				isLeft = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				// 向右移动
				pressed = 3;
				isRight = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				isFire = true;// 发子弹
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				// 退出游戏

			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// 暂停游戏
			}
			mt.direction = pressed;// 坦克此时的速度方向
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				// 向上移动
				isUp = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				// 向下移动
				isDown = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				// 向左移动
				isLeft = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				// 向右移动
				isRight = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				isFire = false;
			}
		}
	}
}
