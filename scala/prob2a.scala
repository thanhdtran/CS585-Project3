import org.apache.spark.rdd.RDD
import scala.collection.immutable.Stream.Empty
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._
import scala.math
//get coordinate of a cellId
def coordinate(cellId: Int): Array[Int] = {
 val SIZE = 20
 val MAX_SPACE = 10000;
 val bottomleft_x = ((cellId - 1) * SIZE) % MAX_SPACE
 val bottomleft_y = MAX_SPACE - ((cellId - 1) / (MAX_SPACE / SIZE)) * 20 - 20
 val topright_x = if ((cellId * SIZE) % MAX_SPACE == 0) MAX_SPACE
 else (cellId * SIZE) % MAX_SPACE
 val topright_y = bottomleft_y + 20
 return Array(bottomleft_x, bottomleft_y, topright_x, topright_y)
}
//get all neighbor cellIds of a cellId
def neighbors(cellId: Int): ListBuffer[Int] = {
 var neighbors = new ListBuffer[Int]()
 if (cellId > 250000 || cellId < 1) return neighbors
 val coors: Array[Int] = coordinate(cellId)
 val possible_neighbor_cells: Array[Int] = Array(cellId - 500 - 1, cellId - 500, cellId - 500 + 1,
   cellId - 1, cellId + 1,
   cellId + 500 - 1, cellId + 500, cellId + 500 + 1
 )
 for (neighbor_cellId <- possible_neighbor_cells) {
   val neighbor_co: Array[Int] = coordinate(neighbor_cellId)
   var valid_cell: Boolean = true
   breakable {
     for (i <- 0 to (neighbor_co.length - 1)) {
       if (neighbor_co(i) < 0 || neighbor_co(i) > 10000) {
         valid_cell = false
         break;
}
       if (math.abs(neighbor_co(i)-coors(i)) > 40) {
         valid_cell = false
         break;
       }
     }
   }
   if (valid_cell) neighbors += neighbor_cellId
 }
 return neighbors
}
//get cellId that contains the point
def get_cell_of_point(x: Int, y: Int): Int = {
 //storing the cellId:
 var cell_arr = Array.ofDim[Int](500, 500)
 var max_size: Int = 250000
 for (i <- 0 to 499) {
   for (j <- 499 to 0 by -1) {
     cell_arr(i)(j) = max_size
     max_size = max_size - 1
   }
 }
 val i_ind = math.min(x/20, 499)
 val j_ind = math.min(y/20, 499)
 return cell_arr(i_ind)(j_ind)
}

//convert a point string to a cellId that contains the point
def pointLine2CellId(line: String) : Int = {
 val x: Int = line.split(",")(0).toInt
 val y: Int = line.split(",")(1).toInt
 return get_cell_of_point(x, y)
}



//calculating the relative density score of a cellId
def cell_rel_score(cellId: Int, count: Int, map: Map[Int, Int]): Float = {
 val neighborCells = neighbors(cellId)
 var totalCount: Int = 0
 var num_neighbors = neighborCells.size
 for (neighborCellId <- neighborCells) {
   totalCount = totalCount + map.getOrElse(neighborCellId, 0)
 }

 if (totalCount <= 0) return 0
 else{
     val neighbors_avg_count: Float = totalCount.toFloat/num_neighbors.toFloat
     return count.toFloat/neighbors_avg_count
 }
}

def cell_neighbor_rel_score(cellId: Int, cellScoreMap: Map[Int, Float]): (Float,String) = {
  val neighborCells = neighbors(cellId)
  var res: String = ""
  for (neighborCellId <- neighborCells) {
    val neighbor_rel_score = cellScoreMap.getOrElse(neighborCellId, 0)
     if (res == "") {
       res += neighborCellId.toString + ":" + neighbor_rel_score.toString 
     } else {
       res += "," + neighborCellId.toString + ":" + neighbor_rel_score.toString 
     }
  }
  return (cellScoreMap.getOrElse(cellId, 0), res)
}

val tf = sc.textFile("/home/mqp/CS585-Project3/jars/datasets/points.txt")
val cellCount = tf.map(line => pointLine2CellId(line)).map(cellId => (cellId, 1)).reduceByKey(_+_)

//cellCount.foreach(println)

//cellCount.saveAsTextFile("out")

//////////////////////////// 2B ///////////////////////////
//convert to map
val map: Map[Int, Int] = cellCount.collect.toMap
val cellScore = cellCount.map{case (k, v) => (k, cell_rel_score(k, v, map))}
//select top k=100
val cellScoreDF = cellScore.toDF("cellId", "score")
cellScoreDF.orderBy($"score".desc).limit(100).show()
val cellScoreDFtop100 = cellScoreDF.orderBy($"score".desc).limit(100)
cellScoreDFtop100.write.format("com.databricks.spark.csv").save("problem2-B.top100")
cellScoreDFtop100.show()

////////////////////// 2C ///////////////////////////////// 
//convert cellScore to Map:
val cellScoreMap: Map[Int, Float] = cellScore.collect.toMap
val cellScoreNeighbor = cellScore.map{case (k,v) => (k,cell_neighbor_rel_score(k, cellScoreMap)) }
val cellScoreNeighborDF = cellScoreNeighbor.toDF("cellId", "score")
val cellScoreDFtop100 = cellScoreNeighborDF.orderBy($"score".desc).limit(100)
cellScoreDFtop100.write.format("com.databricks.spark.csv").save("problem2-C.top100")
cellScoreDFtop100.show()



/////////2B/
//convert to map
val map: Map[Int, Int] = cellCount.collect.toMap
val cellScore = cellCount.map{case (k, v) => (k, cell_rel_score(k, v, map))}
//select top k=100
val cellScoreSorted = cellScore.takeOrdered(100)(Ordering[Float].reverse.on(_._2))
cellScoreSorted.foreach(println)

////////2C
val cellScoreMap: Map[Int, Float] = cellScore.collect.toMap
val cellScoreNeighbor = cellScore.map{case (k,v) => (k,cell_neighbor_rel_score(k, cellScoreMap)) }
val cellScoreNeighborTopK = cellScoreNeighbor.takeOrdered(100)(Ordering[Float].reverse.on(_._2._1))
for (i <- 0 to (cellScoreNeighborTopK.size - 1)) {
  println(cellScoreNeighborTopK(i)._1 + "," + cellScoreNeighborTopK(i)._2._2)
}

