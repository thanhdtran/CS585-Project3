package utils;
import project3.obj.*;
import java.util.*;
public class Utils {
	public static double calDistance(Point p1, Point p2) {
		double res_x = Math.abs(p1.getX() - p2.getX());
		double res_y = Math.abs(p1.getY() - p2.getY());
		return Math.sqrt(res_x*res_x + res_y*res_y);
	}
	public static Point getClosestPoint(Point p1, Vector<Point> centers) {
		
		if (centers == null || centers.size() <= 0) return null;
		if (centers.size() == 1) return centers.get(0);
		double min_distance = calDistance(p1, centers.get(0));
		int idx = 0;
		for (int i = 1; i < centers.size(); i++) {
			double new_dis = calDistance(p1, centers.get(i));
			if (min_distance > new_dis) {
				min_distance = new_dis;
				idx = i;
			}
		}
		return centers.get(idx);
	}
public static int getClosestPointIdx(Point p1, Vector<Point> centers) {
		
		if (centers == null || centers.size() <= 0) return -1;
		if (centers.size() == 1) return 0;
		double min_distance = calDistance(p1, centers.get(0));
		int idx = 0;
		for (int i = 1; i < centers.size(); i++) {
			double new_dis = calDistance(p1, centers.get(i));
			if (min_distance > new_dis) {
				min_distance = new_dis;
				idx = i;
			}
		}
		return (idx);
	}
}
