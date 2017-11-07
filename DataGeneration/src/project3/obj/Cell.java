package project3.obj;

import java.util.Vector;

import utils.Generator;

public class Cell {
	private static int ID = 1;
	

	private int cellId;
	private Point bottomLeftPoint;
	private Point topRightPoint;
	private static final int SIZE = 20;
	private static final int MAX_SPACE = 10000;
	public Cell(int cellId) {
		this.cellId = cellId;
		int bottomleft_x = ((cellId-1)*SIZE) % MAX_SPACE;
		int bottomleft_y = MAX_SPACE - ((cellId - 1) / (MAX_SPACE/SIZE)) * 20 -20;
		int topright_x = (cellId * SIZE) % 10000 == 0 ? 10000 : (cellId * SIZE) % 10000;
		
		int topright_y = bottomleft_y + 20;
		this.bottomLeftPoint = new Point(bottomleft_x, bottomleft_y);
		this.topRightPoint = new Point(topright_x, topright_y);
	}
	
	public Cell(int cellId, Point bottomLeftPoint, Point topRightPoint) {
		super();
		this.cellId = cellId;
		this.bottomLeftPoint = bottomLeftPoint;
		this.topRightPoint = topRightPoint;
	}

	
	@Override
	public String toString() {
		return this.cellId + "," + this.bottomLeftPoint.toStringInt() + ","
				+ this.topRightPoint.toStringInt();
	}

	public String toStringWithoutID() {
		return this.bottomLeftPoint.toString() + ","
				+ this.topRightPoint.toString();
	}

	public boolean containPoints(Point point) {
		if (((point.getX() >= this.bottomLeftPoint.getX()) && (point.getX() <= this.topRightPoint
				.getX()))
				&& ((point.getY() >= this.bottomLeftPoint.getY()) && (point
						.getY() <= this.topRightPoint.getY()))) {
			return true;
		}
		return false;
	}

	
	public int getcellId() {
		return cellId;
	}

	public void setcellId(int cellId) {
		this.cellId = cellId;
	}

	public Point getBottomLeftPoint() {
		return bottomLeftPoint;
	}

	public void setBottomLeftPoint(Point bottomLeftPoint) {
		this.bottomLeftPoint = bottomLeftPoint;
	}

	public Point getTopRightPoint() {
		return topRightPoint;
	}

	public void setTopRightPoint(Point topRightPoint) {
		this.topRightPoint = topRightPoint;
	}

	public static Cell parseFromString(String s) {
		String newstr = s.replaceAll("\\(", "");
		newstr = newstr.replaceAll("\\)", "");
		newstr = newstr.replaceAll("<", "");
		newstr = newstr.replaceAll(">", "");
		newstr = newstr.replaceAll("\n", "");
		newstr = newstr.replaceAll("\\s", "");
		String tokens[] = newstr.split(",");
		if (tokens.length != 5) {
			System.err.println("Error parsing to Rectangle from string:" + s);
			System.exit(1);

		}
		int cellId = Integer.parseInt(tokens[0]);
		int xbl = (int) Integer.parseInt(tokens[1]);
		int ybl = (int) Integer.parseInt(tokens[2]);
		int xtr = (int) Integer.parseInt(tokens[3]);
		int ytr = (int) Integer.parseInt(tokens[4]);
		xbl = xbl > Point.MAX_SCALE ? Point.MAX_SCALE : xbl;
		ybl = ybl > Point.MAX_SCALE ? Point.MAX_SCALE : ybl;
		xtr = xtr > Point.MAX_SCALE ? Point.MAX_SCALE : xtr;
		ytr = ytr > Point.MAX_SCALE ? Point.MAX_SCALE : ytr;
		return new Cell(cellId, new Point((int) xbl, (int) ybl),
				new Point((int) xtr, (int) ytr));
	}

	public static void main(String[] args) {
		for (int i = 1; i <= 250000; i++) {
			Cell cell = new Cell(i);
			System.out.println(cell);
		}
	}

}
