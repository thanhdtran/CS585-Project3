package project3;

import project3.obj.Point;
import project3.obj.Cell;
import utils.FileUtils;

public class Prob2DataGenerator  {
	public static void writeObjs(Object[] objs, String fileout) {
		if (FileUtils.checkExist(fileout)) {
			FileUtils.openFileAppend(fileout);
		} else {
			FileUtils.openFile(fileout);
		}
		int i = 0;
		for (Object obj : objs) {
			FileUtils.writeToFile(obj.toString());
			if (i ++ % 1000 == 0) {
				FileUtils.flushToFile();
			}
		}
		FileUtils.flushToFile();
		FileUtils.closeFile();
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("Usage: Problem2.PointGenerator.jar [Point_MegaBytes] [point_directory_path] ");
			System.exit(1);
		}
		int pointsSizeMB = Integer.parseInt(args[0]);
		
		String pointFilePath = args[1].endsWith("/") ?    (args[1] + "points.txt") : 
													"/" + (args[1] + "points.txt");
		
		FileUtils.checkExistNDel(pointFilePath);
		
		int POINT_BATCH = 10000;
		//gen random points
		while (FileUtils.checkFileSizeInMegaBytes(pointFilePath) < pointsSizeMB) {
			Point[] points = new Point[POINT_BATCH];
			for (int i = 0; i < POINT_BATCH; i++) {
				points[i] = Point.genRandomPoint();
			}
			//write all points to file
			writeObjs(points, pointFilePath);
			
			//clear object
			for (int i = 0; i < POINT_BATCH; i++) {
				points[i] = null;
			}
			points = null;
		}
		System.gc();		

	}
}
