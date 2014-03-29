package item;

public class DestroyableItem {
	public int x, y;// 可破坏物品的坐标
	public int size = 8; // 大小
	public int type;// 类型
	public int status = 1;// 目前状态

	public DestroyableItem(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
}
