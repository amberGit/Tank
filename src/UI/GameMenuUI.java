package UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 坦克的主窗体
 * 
 * @author john
 * 
 */
public class GameMenuUI extends JFrame implements Runnable {
	// 添加按钮图片
	private ImageIcon Icons[] = new ImageIcon[5];
	public static int loc = 0;// 设置初始光标为0
	private MyPanel myPanel;
	private boolean isstop = false;

	public static void main(String args[]) {
		GameMenuUI tui = new GameMenuUI();
		tui.initUI();
		// 启动刷屏线程
		Thread thread = new Thread(tui);
		thread.start();
	}

	public void initUI() {
		// 设置窗体
		this.setTitle("坦克大战");
		this.setSize(246, 348);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setFocusable(true);// 设置JFrame 窗体可以获得焦点:键盘监听器必须要在组件获得焦点之后才能运行

		// // 添加按钮区域
		for (int i = 0; i < Icons.length; i++) {
			Icons[i] = new ImageIcon("images/tankfacemenu_start" + i + ".png");
		}

		// 添加一个JPanel(方便重绘)
		myPanel = new MyPanel();
		this.add(myPanel);
		this.setVisible(true);
		// 添加键盘监听器
		ButtonListener butnLis = new ButtonListener(this);
		this.addKeyListener(butnLis);
	}

	public void stopThread() {
		isstop = true;
	}

	public void run() {
		while (!isstop) {
			myPanel.repaint();
			try {
				Thread.sleep(100);
			} catch (Exception ef) {
				ef.printStackTrace();
			}
		}
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
			ImageIcon pic = new ImageIcon("images/tankfacing.png");
			// 游戏开始界面处理
			// 1：让界面上的图片恢复初始状态（没有一个按钮——这里其实是一张图片——被选中）
			gr.drawImage(pic.getImage(), 0, 0, null);
			// 2：在新界面上贴上按钮图片，这样视觉效果就是每次只有一个按钮被选中了
			gr.drawImage(Icons[loc].getImage(), 75, 150 + 25 * loc, null);
			// gr.drawImage(startIcon.getImage(), 75, 150, null);
			// gr.drawImage(settingIcon.getImage(), 75, 175, null);
			// gr.drawImage(achieveIcon.getImage(), 75, 200, null);
			// gr.drawImage(helpIcon.getImage(), 75, 225, null);
			// gr.drawImage(quitIcon.getImage(), 75, 250, null);

			// 3把虚拟画布画到屏幕上去
			g.drawImage(img, 0, 0, null);
		}
	}

	public class ButtonListener extends KeyAdapter {
		private GameMenuUI gameMenu;

		public ButtonListener(GameMenuUI gameMenu) {
			this.gameMenu = gameMenu;
		}

		public void keyPressed(KeyEvent e) {
			// 按下方向键
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (GameMenuUI.loc == 0) {
					GameMenuUI.loc = 4;
				} else {
					GameMenuUI.loc--;
				}

			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				GameMenuUI.loc = (GameMenuUI.loc + 1) % 5;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (GameMenuUI.loc == 0) {
					GameMenuUI.loc = 4;
				} else {
					GameMenuUI.loc--;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				GameMenuUI.loc = (GameMenuUI.loc + 1) % 5;
			}
			// 按下回车键
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				gameMenu.stopThread();// 结束线程
				switch (GameMenuUI.loc) {
				case 0:
					GameStartUI gameStart = new GameStartUI();
					gameStart.initUI();
					gameMenu.dispose();
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:

					gameMenu.dispose();// 线程没关是否有影响
				}
			}
			// 按下ESC键
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				gameMenu.stopThread();// 结束线程
				gameMenu.dispose();
			}
		}
	}
}
