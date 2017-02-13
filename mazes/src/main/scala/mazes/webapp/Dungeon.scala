package mazes.webapp

import scala.util.Random


object Position{
  //constructors
  def apply(r:Random) = new Position(r.nextInt(),r.nextInt())
  def apply(t:(Int,Int))=new Position(t._1,t._2)
  def apply(x:Int,y:Int)=new Position(x,y)
  
  
  
}
class Position(var x: Int, var y: Int) {
  def +(p:Position): Position = this+p 
  //def asTuple():(Int,Int)=(x,y)
  
  def +(delta: (Int, Int)): Position =new Position(x + delta._1, y + delta._2)
  
  def +(d:Dungeon.Direction):Position=this+d.asTuple
  
  def moves():Seq[Dungeon.Move]=Dungeon.Directions.map(d => (this+d,d) )
  def adyacent():Seq[Position]={
    val a=List((-1,-1),(-1,0),(-1,1),(0,1),(1,1),(1,0),(1,-1),(0,-1))
    a.map(d => (this+d))
  }
  override def toString: String = "(" + x + ", " + y + ")"
  implicit def position2tuple(p:Position)= (p.x,p.y)
  implicit def tuple2position(p:(Int,Int))= Position(p._1,p._2)
  
}

object Dungeon {
  type Move = (Position,Dungeon.Direction)
  
  sealed trait Tile {
    
    def asciiArt():String={
        this match {
        case Empty => "█" //"░"
        case Passage => "▒"
        case Wall => "█"
        case Room  => "▓"
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

class Dungeon {

  //  def apply(p: (Int, Int)) = array(p._1)(p._2)
  //  def apply(r: Int, c: Int) = array(r)(c)
  //  def apply(r: Int) = array(r)
  //  def update(p: (Int, Int), v: T) {
  //    array(p._1)(p._2) = v
  //  }
  //  def update(r: Int, c:Int, v: T) {
  //    array(r)(c) = v
  //  }
  //  

}