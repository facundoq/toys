package mazes.webapp

import scala.util.Random

import scala.util.control.Breaks._
import mazes.webapp.Util.North
import scala.collection.mutable.ListBuffer


class Size(var rows: Int, var cols: Int) {

  def isValid(p: Position): Boolean = p.x > 0 && p.y > 0 && p.x < rows && p.y < cols
  def count = rows * cols
}

object Util {
  type Move = (Position,Util.Direction)
  sealed trait Tile {
    
    def letter():String={
        this match {
        case Empty => "E"
        case Passage => "P"
        case Wall => "W"
        case Room  => "R"
      }
    }
    
    def passable():Boolean= this==Room || this ==Passage
    
  }  
  def random(): Tile = random(new Random())

  def random(r: Random): Tile = Tiles(r.nextInt(Tiles.length))
  
  
  
  case object Empty extends Tile
  case object Passage extends Tile
  case object Wall extends Tile
  case object Room extends Tile
  
  val Tiles = Seq(Empty,Passage,Wall,Room)

  sealed trait Direction {
    def asTuple(): (Int, Int) = {
      this match {
        case North => return (1, 0)
        case South => return (-1, 0)
        case East  => return (0, 1)
        case West  => return (0, -1)
      }
    }
  }
  
  def randomDirection(): Direction = randomDirection(new Random())

  def randomDirection(r: Random): Direction = Directions(r.nextInt(Directions.length))

  case object North extends Direction
  case object East extends Direction
  case object South extends Direction
  case object West extends Direction
  val Directions = Seq(North, East, South, West)
  
  
}

abstract class DungeonGenerator(var size: Size) {
  var r = new Random()
  
  type Matrix[T] = Array[Array[T]]
  
  def generate: String

  def printArray(a: Matrix[Int]): String = {
    var r = ""
    for (i <- a.indices) {
      for (j <- a(i).indices) {
        r += a(i)(j).toString() + " "
      }
      r += "\n"
    }
    return r
  }
  def printArray(a: Matrix[Util.Tile]): String = {
    var r = ""
    for (i <- a.indices) {
      for (j <- a(i).indices) {
        r += a(i)(j).letter + " "
      }
      r += "\n"
    }
    return r
  }
  def printArray(a: Matrix[Boolean]): String = {
    var inta = a.map(row => row.map(b => if (b) 1 else 0))
    return printArray(inta)
  }

}

class Position(var x: Int, var y: Int) {
  def +(p:Position): Position = this+(p.asTuple) 
  def asTuple():(Int,Int)=(x,y)
  def +(delta: (Int, Int)): Position =new Position(x + delta._1, y + delta._2)
  
  def +(d:Util.Direction):Position=this+d.asTuple
  
  def moves():Seq[Util.Move]=Util.Directions.map(d => (this+d,d) )
  def adyacent():Seq[Position]={
    val a=List((-1,-1),(-1,0),(-1,1),(0,1),(1,1),(1,0),(1,-1),(0,-1))
    a.map(d => (this+d))
  }
  override def toString: String = "(" + x + ", " + y + ")"
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
  type Map = Matrix[Util.Tile]
  type Move = (Position,Util.Direction)
  class asInt(b: Boolean) {
    def toInt = if (b) 1 else 0
  }
  implicit def convertBooleanToInt(b: Boolean) = new asInt(b)

  
  def possibleMoves(p:Position,m:Map,s:Size ):Seq[Move]={
     p.moves filter ( {case (p,d) => s.isValid(p) && m(p.x)(p.y)==Util.Empty })        
  }
  def carvePassage( m:Map, d:Util.Direction,p:Position):Option[Move]={
      val s=size(m)
      val moves = possibleMoves(p,m,s)
      //println(moves)

      if (moves.isEmpty) {
        // deadend, look somewhere else
        return None
      }
        
      
//      val newDirectionProbability = (1 - keepDirectionProbability) / (possiblePositions.length - 1)
//      
//      var mantainDirectionFactor = List.fill(Util.Directions.length)(newDirectionProbability)
//      mantainDirectionFactor.updated(previousDirectionIndex, keepDirectionProbability)
//    
      
      //var positionProbabilities = (validPositionFactor, mantainDirectionFactor).zipped.map(_ * _)
      var moveProbabilities= List.fill(moves.length)(1.0f/moves.length)
      val totalProbability = moveProbabilities.sum
      //println(moveProbabilities)
      moveProbabilities = moveProbabilities.map(_ / totalProbability)
      
      val moveDistribution = new Categorical(moveProbabilities.toList)
      val moveIndex = moveDistribution.draw()
      //println(moveIndex)  
      
      return Some(moves(moveIndex))
  }
  
  def size(m:Map):Size=new Size(m.length,m(0).length)
  
  def updateWall(p:Position,map:Map){
    val s=size(map)
    
    val adyacentPositions=p.adyacent filter ({ p => s.isValid(p) && map(p.x)(p.y)==Util.Empty })
    
    adyacentPositions.foreach( p => if (convertToWall(p, map)) map(p.x)(p.y)=Util.Wall)
    
  }
  
  def convertToWall(p:Position,m:Map):Boolean={
      val border=Iterator.continually(p.adyacent).flatten.take(11).toList
      //println(border.take(2))
      //println(border.take(2))
      
      
      val s=size(m)
      val corners=List(border.slice(1, 4),border.slice(3, 6),border.slice(5,8),border.slice(7,10))
      //println(corners) 
      val cornerForcesWall = (c:List[Position]) => c.map(p => s.isValid(p) && m(p.x)(p.y).passable).reduce(_&&_)  
      
      val cornersThatForceWall= corners filter cornerForcesWall 
      //println(corners(0).take(2))
      
      //println("Convert position "+p+" to wall" + !cornersThatForceWall.isEmpty)
      
      !cornersThatForceWall.isEmpty
      
      
  }
  
  
  def generate(): String = {

    //var tiles = Array.ofDim[Int](size.rows, size.cols)
    
    var map:Map=Array.fill(size.rows, size.cols)(Util.Empty)
    
    
    var p = new Position(r.nextInt(size.rows), r.nextInt(size.cols))
    var d: Util.Direction = Util.South
    map(p.x)(p.y) = Util.Passage;
    var visitedOrWallCount = 1
    var path=ListBuffer(p)
    var direction=ListBuffer(d)
    val maxRestarts=20
    var i=0
    while (i<maxRestarts) {
       println(i+ "/"+ maxRestarts+ " -> " + p)
       updateWall(p,map)
       carvePassage(map, d, p) match {
       case None=> {
          // TODO find a new place to start
          //visitedOrWallCount =size.count
          path.remove(path.length-1)
          direction.remove(path.length-1)
          if (path.isEmpty){
            i==maxRestarts
          }else{
            i+=1
            p=path.last
            d=direction.last
          }
       }
       case Some((p2,d2)) => {
          p=p2
          d=d2
          map(p.x)(p.y) = Util.Passage;
          path+=p
          direction+=d
       }
        
      }

      
    }

    return printArray(map)
  }

}
