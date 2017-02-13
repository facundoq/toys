package mazes.webapp

import scala.util.Random

import scala.util.control.Breaks._
import scala.collection.mutable.ListBuffer
import mazes.webapp.DungeonGenerator.Map
import mazes.webapp.DungeonGenerator.Move
import mazes.math.Categorical



object DungeonGenerator{
  //type Matrix[T] = Array[Array[T]]
  type Map = Matrix[Dungeon.Tile]
  type Move = (Position,Dungeon.Direction)
}
abstract class DungeonGenerator() {
  var r = new Random()  
  def generate(size:(Int,Int)): Map
}


// pimpmylib http://stackoverflow.com/questions/2633719/is-there-an-easy-way-to-convert-a-boolean-to-an-integer


object SimpleMaze{
  class asInt(b: Boolean) {
    def toInt = if (b) 1 else 0
  }
  implicit def convertBooleanToInt(b: Boolean) = new asInt(b)
  
}
class SimpleMaze(var keepDirectionProbability: Float) extends DungeonGenerator() {
  import mazes.webapp.Position._
  
  implicit def position2tuple(p:Position)= (p.x,p.y)
  
  def possibleMoves(p:Position,m:Map):Seq[Move]={
     p.moves filter ( {case (p,d) => m.valid(p) && m(p.x,p.y)==Dungeon.Empty })        
  }
  
  def carvePassage( m:Map, d:Dungeon.Direction,p:Position):Option[Move]={

      val moves = possibleMoves(p,m)

      if (moves.isEmpty) {
        // deadend, look somewhere else
        return None
      }
      
      val moveDistribution = new Categorical(moveProbabilities(moves,m).toList)
      val moveIndex = moveDistribution.draw()  
      
      return Some(moves(moveIndex))
  }
  def moveProbabilities(moves:Seq[Move],m:Map):List[Float]={
          //      val newDirectionProbability = (1 - keepDirectionProbability) / (possiblePositions.length - 1)
      //      
      //      var mantainDirectionFactor = List.fill(Util.Directions.length)(newDirectionProbability)
      //      mantainDirectionFactor.updated(previousDirectionIndex, keepDirectionProbability)
      //    
      
      //var positionProbabilities = (validPositionFactor, mantainDirectionFactor).zipped.map(_ * _)
      var moveProbabilities= List.fill(moves.length)(1.0f/moves.length)
      val totalProbability = moveProbabilities.sum

      moveProbabilities = moveProbabilities.map(_ / totalProbability)
      return moveProbabilities 
  }
  
  def updateWall(p:Position,map:Map){
    val adyacentPositions=p.adyacent filter ({ p => map.valid(p) && map(p.x,p.y)==Dungeon.Empty })
    adyacentPositions.foreach( p => if (convertToWall(p, map)) map(p.x)(p.y)=Dungeon.Wall)    
  }
  
  def convertToWall(p:Position,m:Map):Boolean={
      val border=Iterator.continually(p.adyacent).flatten.take(11).toList
      val corners=List(border.slice(1, 4),border.slice(3, 6),border.slice(5,8),border.slice(7,10))
      val cornerForcesWall = (c:List[Position]) => c.map(p => m.valid(p) && m(p.x,p.y).passable).reduce(_&&_)  
      
      val cornersThatForceWall= corners filter cornerForcesWall       
      !cornersThatForceWall.isEmpty
  }
  
  
  def generate(size:(Int,Int)): Map = {
    var map:Map=Matrix(size,Dungeon.Empty)

    var p = Position(map.randomIndex(r))
    var d: Dungeon.Direction = Dungeon.South
    map(p.x,p.y) = Dungeon.Passage;
    var visitedOrWallCount = 1
    var path=ListBuffer(p)
    var direction=ListBuffer(d)
    var i=0
    val maxRestarts=20
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
       case Some((newP,newD)) => {
          p=newP
          d=newD
          map(p.x,p.y) = Dungeon.Passage;
          path+=p
          direction+=d
       }
        
      }

      
    }

    return map
  }

}
