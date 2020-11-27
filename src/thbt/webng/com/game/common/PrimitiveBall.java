package thbt.webng.com.game.common;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;

public class PrimitiveBall {

	public PrimitiveBall() {
	}

	public PrimitiveBall(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Point leftBottomPoint = new Point(getLeft()
				+ (int) (width * (1 - 1 / Math.sqrt(2)) / 2), getTop() + height
				- (int) (height * (1 - 1 / Math.sqrt(2)) / 2));

		Point middlePoint = new Point(leftBottomPoint.x
				+ (int) (width / 2 / Math.sqrt(2)), leftBottomPoint.y
				- (int) (width / 2 / Math.sqrt(2)));

		Point rightTopPoint = new Point(leftBottomPoint.x
				+ (int) (width / Math.sqrt(2)), leftBottomPoint.y
				- (int) (width / Math.sqrt(2)));

		Paint paint = new GradientPaint(leftBottomPoint, color, middlePoint,
				Color.BLACK);
		g2.setPaint(paint);
		g2.fillArc(getLeft(), getTop(), width, height, 134, 182);

		Paint paint2 = new GradientPaint(middlePoint, Color.BLACK,
				rightTopPoint, color);
		g2.setPaint(paint2);
		g2.fillArc(getLeft(), getTop(), width, height, -46, 182);
	}

	protected int left;
	protected int top;
	protected int width;
	protected int height;
	protected Color color;
}
