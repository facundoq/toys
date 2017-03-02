package mazes.generators

import scala.util.Random
import scala.util.control.Breaks._
import scala.collection.mutable.ListBuffer
import mazes.math.Categorical
import scala.collection.TraversableOnce.flattenTraversableOnce
import mazes.generators.DungeonGenerator.Map
import mazes.generators.DungeonGenerator.Size
import mazes.generators.DungeonGenerator.Move
import mazes.generators.Dungeon.Direction




object DungeonGenerator{
  //type Matrix[T] = Array[Array[T]]
  type Map = Matrix[Dungeon.Tile]
  type Move = (Position,Dungeon.Direction)
  type Size=(Int,Int)
}
abstract class DungeonGenerator(var size:Size) {
  var r = new Random()  
  def generate(): Map
  
  def fillBorderWith(map:Map,e:Dungeon.Tile){
    
    for (c <- 0 to map.columns-1){
      map(0,c)=e
      map(map.rows-1,c)=e
    }
    
    for (r <- 0 to map.rows-1){
      map(r,0)=e
      map(r,map.columns-1)=e
    }
    
  }
}


// pimpmylib http://stackoverflow.com/questions/2633719/is-there-an-easy-way-to-convert-a-boolean-to-an-integer

