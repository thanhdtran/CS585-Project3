package jobs;


import java.util.Arrays;
import java.util.Map;
import java.util.Vector;
import java.lang.Iterable;

import java.util.Comparator;
import java.util.List;
import java.io.Serializable;
import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;

public class Project3Problem2 {

	public static Map<Integer, Integer> cellCountMap = null;
	public static Map<Integer, Float> cellScoreMap = null;
	public static int[] coordinate(int cellId) {
		int SIZE = 20;
		int MAX_SPACE = 10000;
		int bottomleft_x = ((cellId - 1) * SIZE) % MAX_SPACE;
		int bottomleft_y = MAX_SPACE - ((cellId - 1) / (MAX_SPACE / SIZE)) * SIZE - SIZE;
		int topright_x = ((cellId * SIZE) % MAX_SPACE == 0) ? MAX_SPACE : (cellId * SIZE) % MAX_SPACE;
		int topright_y = bottomleft_y + 20;
		int[] res = new int[4];
		res[0] = bottomleft_x;
		res[1] = bottomleft_y;
		res[2] = topright_x;
		res[3] = topright_y;
		return res;
	}

	public static Vector<Integer> neighbors(int cellId) {
		Vector<Integer> lstNeighbors = new Vector<>();
		if (cellId > 250000 || cellId < 1)
			return lstNeighbors;
		int[] coors = coordinate(cellId);
		int[] possible_neighbor_cells = { cellId - 500 - 1, cellId - 500, cellId - 500 + 1, cellId - 1, cellId + 1,
				cellId + 500 - 1, cellId + 500, cellId + 500 + 1 };
		for (int neighbor_cellId : possible_neighbor_cells) {
			int[] neighbor_coordinate = coordinate(neighbor_cellId);
			boolean valid_cell = true;
			// check a valid neighbor?
			for (int i = 0; i < neighbor_coordinate.length; i++) {
				if (neighbor_coordinate[i] < 0 || neighbor_coordinate[i] > 10000) {
					valid_cell = false;
					break;
				}
				if (Math.abs(neighbor_coordinate[i] - coors[i]) > 40) {
					valid_cell = false;
					break;
				}
			}

			if (valid_cell)
				lstNeighbors.add(neighbor_cellId);
		}
		return lstNeighbors;
	}

	public static int get_cell_of_point(int x, int y) {
		// storing the cellId:
		int[][] cell_arr = new int[500][500];
		int max_size = 250000;
		for (int i = 0; i < 500; i++) {
			for (int j = 499; j >= 0; j--) {
				cell_arr[i][j] = max_size;
				max_size = max_size - 1;
			}
		}
		int i_ind = Math.min(x / 20, 499);
		int j_ind = Math.min(y / 20, 499);
		return cell_arr[i_ind][j_ind];
	}

	// convert a point string to a cellId that contains the point
	public static int pointLine2CellId(String line) {
		int x = Integer.parseInt(line.split(",")[0]);
		int y = Integer.parseInt(line.split(",")[1]);
		return get_cell_of_point(x, y);
	}

	// calculating the relative density score of a cellId
	public static float cell_rel_score(int cellId, int count) {
		Vector<Integer> neighborCells = neighbors(cellId);
		int totalCount = 0;
		int num_neighbors = neighborCells.size();
		for (int neighborCellId : neighborCells) {
			if (cellCountMap != null && cellCountMap.containsKey(neighborCellId)) {
				totalCount = totalCount + cellCountMap.get(neighborCellId);
			}
		}

		if (totalCount <= 0)
			return 0.0f;
		else {
			float neighbors_avg_count = (float) (totalCount) / (float) (num_neighbors);
			return (float) (count) / (float) (neighbors_avg_count);
		}
	}

	public static Tuple2<Float, String> cell_neighbor_rel_score(int cellId) {
		Vector<Integer> neighborCells = neighbors(cellId);
		String res = "";
		for (int neighborCellId : neighborCells) {
			float neighbor_rel_score = 0.0f;
			if (cellScoreMap != null && cellScoreMap.containsKey(neighborCellId)) {
				neighbor_rel_score = cellScoreMap.get(neighborCellId);
			}
			if (res == "") {
				res += neighborCellId + ":" + neighbor_rel_score;
			} else {
				res += "," + neighborCellId + ":" + neighbor_rel_score;
			}
		}
		float cellIdScore = 0.0f;
		if (cellScoreMap.containsKey(cellId)) {
			cellIdScore = cellScoreMap.get(cellId);
		}
		// return (cellScoreMap.getOrElse(cellId, 0), res)
		return new Tuple2<Float, String>(cellIdScore, res);
	}
	
	static class ScoreComparator implements Comparator<Tuple2<Integer, Float>> ,Serializable {
	       final static ScoreComparator INSTANCE = new ScoreComparator();
	       @Override
	       public int compare(Tuple2<Integer, Float> t1, Tuple2<Integer, Float> t2) {
	          return -t1._2.compareTo(t2._2);     // sorts RDD elements descending
	       }
	   }
	
	static class NeighborScoreComparator implements Comparator<Tuple2<Integer, Tuple2<Float, String>>> ,Serializable {
	       final static NeighborScoreComparator INSTANCE = new NeighborScoreComparator();
	       @Override
	       public int compare(Tuple2<Integer, Tuple2<Float, String>> t1, Tuple2<Integer, Tuple2<Float, String>> t2) {
	          return -t1._2._1.compareTo(t2._2._1);     // sorts RDD elements descending
	       }
	   }

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: Project3Problem2 <path to points.txt>");
			System.exit(1);
		}
		// String inputFile = "datasets/test.txt";
		// String outputFile = "datasets/out.txt";
		String inputFile = args[0];

		// SparkSession spark = SparkSession
		// .builder()Tuple2<Float, String>
		// .appName("JavaWordCount")
		// .getOrCreate();
		//
		// JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

		// Create a Java Spark Context.
		SparkConf conf = new SparkConf().setAppName("Problem 2").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		// Load our input data into RDD of String
		JavaRDD<String> input = sc.textFile(inputFile);
		// Convert into CellId
		JavaRDD<Integer> cellIds = input.flatMap(new FlatMapFunction<String, Integer>() {
			public Iterable<Integer> call(String x) {
				return Arrays.asList(pointLine2CellId(x));
			}
		});

		// build the cellCount RDD:
		JavaPairRDD<Integer, Integer> cellCount = cellIds.mapToPair(new PairFunction<Integer, Integer, Integer>() {
			public Tuple2<Integer, Integer> call(Integer cellId) {
				return new Tuple2(cellId, 1);
			}
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer x, Integer y) {
				return x + y;
			}
		});

		cellCountMap = cellCount.collectAsMap();
		// build the cellScore:
		JavaPairRDD<Integer, Float> cellScore = cellCount
				.mapToPair(new PairFunction<Tuple2<Integer, Integer>, Integer, Float>() {
					public Tuple2<Integer, Float> call(Tuple2<Integer, Integer> tuple) {
						int cellId = tuple._1();
						int count = tuple._2();
						float score = cell_rel_score(cellId, count);
						return new Tuple2<>(cellId, score);
					}
				});

		/************************* question 2b ***********************************/
		List<Tuple2<Integer, Float>> top100Result = cellScore.takeOrdered(100, ScoreComparator.INSTANCE);
		// print out result:
		for (Tuple2<Integer, Float> entry : top100Result) {
			System.out.println(entry._1 + "--" + entry._2);
		}

		/************************* question 2c ***********************************/
		// store cellScore RDD to map
		cellScoreMap = cellScore.collectAsMap();
		JavaPairRDD<Integer, Tuple2<Float, String>> cellScoreNeighbor = cellScore
				.mapToPair(new PairFunction<Tuple2<Integer, Float>, Integer, Tuple2<Float, String>>() {
					public Tuple2<Integer, Tuple2<Float, String>> call(Tuple2<Integer, Float> tuple) {
						int cellId = tuple._1();
						float cellScore = tuple._2();
						Tuple2<Float, String> res = cell_neighbor_rel_score(cellId);
						return new Tuple2<>(cellId, res);
					}
				});

		List<Tuple2<Integer, Tuple2<Float, String>>> top100ResultPart2C = cellScoreNeighbor.takeOrdered(100,
				NeighborScoreComparator.INSTANCE);
		// print out result:
		for (Tuple2<Integer, Tuple2<Float, String>> entry : top100ResultPart2C) {
			System.out.println(entry._1 + "," + entry._2._2);
		}
	}
}