package mazes.generators

import mazes.generators.DungeonGenerator.Map
import mazes.generators.DungeonGenerator.Size
import scala.collection.mutable.ListBuffer

class Rectangle(val s: Size, val p: Position) {
  
  def splitHorizontally(at: Int) = {
    val (s1, s2) = splitSizeHorizontally( at)
    val left = new Rectangle(s1, p)
    val right = new Rectangle(s2, p + (0, left.w+ 1))
    (left, right)
  }
  def splitSizeHorizontally( at: Int) = {
    val left = (h, at)
    //leave a space for the wall in the middle 
    val right = (h, w - (at + 1))
    (left, right)
  }
  def splitSizeVertically(at: Int) = {
    val top = (at, w)
    //leave a space for the wall in the middle 
    val bottom = (h - (at + 1), w)
    (top, bottom)
  }
  def splitVertically(at: Int) = {
    val (s1, s2) = splitSizeVertically(at)
    val top = new Rectangle(s1, p)
    val bottom = new Rectangle(s2, p + (top.h + 1, 0))
    (top, bottom)
  }
  def tileCount() = h*w

  override def toString() = {
    s"Rectangle, size ${this.h} x ${this.w}, at ${this.p}"
  }
  
  def h = s._1
  def w = s._2
  def x = p.x
  def y = p.y
  
  def topLeft = p
  val bottomRight=new Position(x+h,y+w)


  
}

class RecursiveMaze(s: Size, minArea: Int,var minSideLength: Int) extends DungeonGenerator(s) {
  
  class DoorInfo(val avoid:Int,val isLeft:Boolean)
  
  var map: Map = Matrix(size, Dungeon.Passage)
  fillBorderWith(map, Dungeon.Wall)
  
  var stack = ListBuffer[Rectangle]();
  val initial = new Rectangle(s, Position());
  stack += initial
  var orientation = true
  
  var doorStack = ListBuffer[DoorInfo]();
  minSideLength=Math.max(minSideLength,6)
  
  def addWall() {
    val a = stack.remove(stack.length - 1)

    println(s"$a")
    if (a.tileCount > minArea && a.h > minSideLength && a.w > minSideLength) {
      if (a.h<a.w){
        splitHorizontally(stack, a, map)
      } else {
        splitVertically(stack, a, map)
      }
      orientation = !orientation
    }
  }

  def generate(): Map = {
    while (!stack.isEmpty) {
      addWall()
    }
    return map
  }

  def splitVertically(stack: ListBuffer[Rectangle], area: Rectangle, map: Map) {
    val splitRow = splitPoint(area.h)
    val (r1, r2) = area.splitVertically(splitRow)
    drawHorizontalWall(map, area, splitRow)
    
    val doorColumn = splitPoint(area.w)
    println(s"Splitting at row $splitRow - door at $doorColumn - Resulting areas: 1) $r1 2) $r2")
    
    map(splitRow+area.x, doorColumn+area.y) = Dungeon.Passage
    
    stack += r1
    stack += r2
  }
  def splitHorizontally(stack: ListBuffer[Rectangle], area: Rectangle, map: Map) {
    
    val splitColumn = splitPoint(area.w)
    val (r1, r2) = area.splitHorizontally(splitColumn)
    
    drawVerticalWall(map, area, splitColumn)
    val doorRow = splitPoint(area.h)
    map(doorRow+area.x, splitColumn+area.y) = Dungeon.Passage
    
    println(s"Splitting at column $splitColumn - door at $doorRow - Resulting areas: 1) $r1 2) $r2")
    
    stack += r1
    stack += r2
  }
  def addDoorHorizontal(area:Rectangle,map:Map,column:Int){
    
    
  }
  
  def splitPoint(size: Int) = {
//    val mu = (size.toFloat / 2)
//    val sd = (size.toFloat / 20)
//    var at = (r.nextGaussian() * sd + mu).toInt
//    
//    println(s"Splitting with mu=$mu sd=$sd  at $at")
//    at
      val m=2
      var at= r.nextInt(size-m)+m
      at = Seq(Seq(at, size - m).min, m).max
      at
       
  }

  def drawHorizontalWall(map: Map, area: Rectangle, at: Int) {
    val fromCol = area.y;
    val toCol = fromCol + area.w - 1;
    val correctedAt = area.x + at
    for (c <- fromCol to toCol) {
      map(correctedAt, c) = Dungeon.Wall
    }

  }
  def drawVerticalWall(map: Map, area: Rectangle, at: Int) {
    val fromRow = area.x;
    val toRow = fromRow + area.h - 1;
    val correctedAt = area.y + at
    for (r <- fromRow to toRow) {
      map(r, correctedAt) = Dungeon.Wall
    }

  }

}