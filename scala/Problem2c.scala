import org.apache.spark.rdd.RDD
import scala.collection.immutable.Stream.Empty
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._
import java.io.File
import java.io.PrintWriter
import scala.io.Source

import scala.math
//import scala.math._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListMap
//import scala.collection.mutable.SortedMap
import java.io.PrintWriter//write
import scala.io.Source//read




//Find cellID that the given point belong to
def findCellID(line: String) : Int = {
	val x : Int = line.split(",")(0).toInt
	val y : Int = line.split(",")(1).toInt
	var xline : Int = 0
	var yline : Int = 0
	//var id : Int = 0
	if(x%20 != 0){
		xline = x/20 + 1
	}
	else{
		xline = x/20
	}
	if(y%20 !=0){
		yline = y/20 + 1
	}
	else{
		yline = y/20
	}
	
	var id = 500*(math.max(yline-1,0)) + xline
	return id

}

//Find score of a given cellID and density
def findRelativeScore(k: Int,count: Int,map:Map[Int,Int]):Float = {
	var cnt : Int = 0
	var sum : Int = 0
	var up = false
	var bot = false
	var left = false
	var right = false

	if(k/500 != 499){
		//add bot
		sum += map.getOrElse(k+500,0)
		cnt += 1
		bot = true		
	}
	if(k/501 >= 1){
		sum += map.getOrElse(k-500,0)
		cnt += 1
		up = true
	}
	if(k%500 != 1){
		sum += map.getOrElse(k-1,0)
		cnt += 1
		left = true
	}
	if(k%500 != 0){
		sum += map.getOrElse(k+1,0)
		cnt += 1
		right = true
	}
	if(left==true && up==true){
		sum += map.getOrElse(k-501,0)
		cnt += 1
	}
	if(left==true && bot==true){
		sum += map.getOrElse(k+499,0)
		cnt += 1
	}
	if(right==true && up==true){
		sum += map(k-499)
		cnt += 1
	}
	if(right==true && bot==true){
		sum += map.getOrElse(k+501,0)
		cnt += 1
	}
	var avg : Float = sum.toFloat/cnt
	var index : Float = count.toFloat/avg
	if(index.isNaN==false){
		return index
				
	}
	else{
		return 0
	}
		
}


//Find neighbors of a given cellID
def getneighbors(k:Int): ListBuffer[Int] = {
	var neighbors = new ListBuffer[Int]()
	var up = false
	var bot = false
	var left = false
	var right = false
	if(k/500 != 499){
		//add bot
		neighbors += k+500
		bot = true		
	}
	if(k/501 >= 1){
		neighbors += k-500
		up = true
	}
	if(k%500 != 1){
		neighbors += k-1
		left = true
	}
	if(k%500 != 0){
		neighbors += k+1
		right = true
	}
	if(left==true && up==true){
		neighbors += k-501
	}
	if(left==true && bot==true){
		neighbors += k+499
	}
	if(right==true && up==true){
		neighbors += k-499
	}
	if(right==true && bot==true){
		neighbors += k+501
	}

	return neighbors

}

//Find top 100 cells' neighbors and scores
def getNeighborScore(top100MP:Array[(Int,Float)],cellCount:Map[Int,Int]): String ={
	
	var output : String = ""
	var keys = top100MP.map(_._1).toList
	for (key<-keys){
		//var key : Int = line.map(._2)
		output += "Center cell:"+key.toString+"\n"
		for (neighbor<-getneighbors(key)){
			output += "neighbor:"+neighbor.toString+",score:"+findRelativeScore(neighbor,cellCount.getOrElse(neighbor,0),cellCount).toString + " "																																																																																																																
		}
		output+="\n"
		
	}
	
	return output
}



		
//B		
val data = sc.textFile("/home/mqp/spark-2.1.0-bin-hadoop2.7/bin/points.txt")
val cellCount = data.map(line => findCellID(line)).map(cellId => (cellId, 1)).reduceByKey(_+_)
val map: Map[Int, Int] = cellCount.collect.toMap
val cellScore = cellCount.map{case (k, v) => (k, findRelativeScore(k, v, map))}
val cellScoreSorted = cellScore.takeOrdered(100)(Ordering[Float].reverse.on(_._2))


//C using the top 100 cells with highest relative density score
var cell_neighbor_info = getNeighborScore(cellScoreSorted,map)

val writer = new PrintWriter(new File("output.txt"))

writer.write(cell_neighbor_info)

writer.close()


		
		

