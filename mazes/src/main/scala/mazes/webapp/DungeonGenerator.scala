package mazes.webapp

import scala.util.Random
import mazes.webapp.Direction.DirectionVal
import scala.util.control.Breaks._

class Size(var rows: Int, var cols: Int) {

  def isValid(p: Position): Boolean = p.x > 0 && p.y > 0 && p.x < rows && p.y < cols
  def count = rows * cols
}

abstract class DungeonGenerator(var size: Size) {
  var r = new Random()
  
  type Matrix[T] = Array[Array[T]]
  
  def generate: String

  def printArray(a: Array[Array[Int]]): String = {
    var r = ""
    for (i <- a.indices) {
      for (j <- a(i).indices) {
        r += a(i)(j).toString() + " "
      }
      r += "\n"
    }
    return r
  }
  def printArray(a: Array[Array[Boolean]]): String = {
    var inta = a.map(row => row.map(b => if (b) 1 else 0))
    return printArray(inta)
  }
}

object Direction {
  sealed trait DirectionVal {
    def asTuple(): (Int, Int) = {

      this match {
        case North => return (1, 0)
        case South => return (-1, 0)
        case East  => return (0, 1)
        case West  => return (0, -1)
      }

    }

  }
  def random(): DirectionVal = random(new Random())

  def random(r: Random): DirectionVal = values(r.nextInt(values.length))

  case object North extends DirectionVal
  case object East extends DirectionVal
  case object South extends DirectionVal
  case object West extends DirectionVal
  val values = Seq(North, East, South, West)

}

class Position(var x: Int, var y: Int) {
  def +(delta: (Int, Int)): Position = {
    return new Position(x + delta._1, y + delta._2)
  }
  
    override def toString: String =
    "(" + x + ", " + y + ")"
}

// pimpmylib http://stackoverflow.com/questions/2633719/is-there-an-easy-way-to-convert-a-boolean-to-an-integer
abstract class Distribution {
  def draw(): Int
}

class Categorical(var probabilities: List[Float]) extends Distribution {

  def draw(): Int = {
    var cumulativeProbabilities = probabilities.scanLeft(0f)(_ + _)
    var p = new Random().nextFloat()
    return cumulativeProbabilities.indexWhere(_ > p)-1
  }
}



class SimpleMaze(size: Size, var keepDirectionProbability: Float) extends DungeonGenerator(size) {

  class asInt(b: Boolean) {
    def toInt = if (b) 1 else 0
  }
  implicit def convertBooleanToInt(b: Boolean) = new asInt(b)

  def possibleMoves(p:Position,visited:Array[Array[Boolean]]):(Seq[Position],Seq[DirectionVal])={
    
     val validPositionFactor = posiblePositions.map(p => (size.isValid(p) && !visited(p.x)(p.y)) toInt)
     
  }
  def generate(): String = {

    //var tiles = Array.ofDim[Int](size.rows, size.cols)
    var visited = Array.ofDim[Boolean](size.rows, size.cols)
    var isWall = Array.ofDim[Boolean](size.rows, size.cols)
    
    var p = new Position(r.nextInt(size.rows), r.nextInt(size.cols))
    var previousDirection: DirectionVal = Direction.South
    visited(p.x)(p.y) = true;
    var visitedOrWallCount = 1

    while (visitedOrWallCount < size.count) {
      println(visitedOrWallCount+ "/"+ size.count + " -> " + p)
      
      val (posiblePositions,possibleDirections) = possibleMoves(p,size)
      println(Direction.values)
      println(posiblePositions)
      val validPositionFactor = posiblePositions.map(p => (size.isValid(p) && !visited(p.x)(p.y)) toInt)

      val previousDirectionIndex = Direction.values.indexOf(previousDirection)
      val newDirectionProbability = (1 - keepDirectionProbability) / (Direction.values.length - 1)
      var mantainDirectionFactor = List.fill(Direction.values.length)(newDirectionProbability)
      mantainDirectionFactor.updated(previousDirectionIndex, keepDirectionProbability)

      var positionProbabilities = (validPositionFactor, mantainDirectionFactor).zipped.map(_ * _)
      val totalProbability = positionProbabilities.sum
      
      println(positionProbabilities)
      
      if (totalProbability > 1e-9) {
        positionProbabilities = positionProbabilities.map(_ / totalProbability)
        val positionsDistribution = new Categorical(positionProbabilities.toList)
        val newPositionIndex = positionsDistribution.draw()
        println(newPositionIndex)  
        previousDirection = Direction.values(newPositionIndex)
        p = posiblePositions(newPositionIndex)
      } else { // deadend, look somewhere else
        visitedOrWallCount =size.count
      }

      visited(p.x)(p.y) = true;
      visitedOrWallCount += 1
    }

    return printArray(visited)
  }

}