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

class RecursiveMaze(s: Size,var minSideLength: Int) extends DungeonGenerator(s) {
  
  class DoorInfo(val avoid:Int,val isLeft:Boolean)
  
  var map: Map = Matrix(size, Dungeon.Passage)
  fillBorderWith(map, Dungeon.Wall)
  
  var stack = ListBuffer[Rectangle]();
  val initial = new Rectangle((s._1-2,s._2-2), Position(1,1)); //inside border
  stack += initial
  var orientation = true
  
  var doorStack = ListBuffer[DoorInfo]();
  minSideLength=Math.max(minSideLength,3)

  def addWall() {
    val a = stack.remove(stack.length - 1)

    println(s"$a")
    if (a.h >= minSideLength && a.w >= minSideLength) {
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

  /*
   *   ----------------
   *   -              -
   *   -              -
   *   ----------------
   *   -              -
   *   -              -
   *   ----------------
   */
  def splitVertically(stack: ListBuffer[Rectangle], area: Rectangle, map: Map) {
    val rowsWithPassage=getRowsWithWall(area,map).toList
//    println(s"rowsWithPassage $rowsWithPassage")
    val freeRows=area.h-2-rowsWithPassage.length
    if (freeRows>0){
      var splitRow = splitPoint(area.h)
      
      while (rowsWithPassage.contains(splitRow+area.x)){
        splitRow = splitPoint(area.h)
      }
      
      val (r1, r2) = area.splitVertically(splitRow)
      drawHorizontalWall(map, area, splitRow)
      
      val doorColumn = doorPoint(area.w)
      println(s"Splitting at row $splitRow - door at $doorColumn - Resulting areas: 1) $r1 2) $r2")
      
      map(splitRow+area.x, doorColumn+area.y) = Dungeon.Passage
      
      stack += r1
      stack += r2
    }
  }
  
  /*
   *   ----------------
   *   -      -       -
   *   -      -       -
   *   ----------------
   */
  def splitHorizontally(stack: ListBuffer[Rectangle], area: Rectangle, map: Map) {
    val columnsWithPassage=getColumnsWithPassage(area,map).toList
    val freeColumns=area.w-2-columnsWithPassage.length
    if (freeColumns>0){
      var splitColumn = splitPoint(area.w)
      while (columnsWithPassage.contains(splitColumn+area.y)){
        splitColumn = splitPoint(area.w)
      }
  //    println(s"columnsWithWall ${columnsWithPassage.mkString}")
      val (r1, r2) = area.splitHorizontally(splitColumn)
      
      drawVerticalWall(map, area, splitColumn)
      val doorRow = doorPoint(area.h)
      map(doorRow+area.x, splitColumn+area.y) = Dungeon.Passage
      
      println(s"Splitting at column $splitColumn - door at $doorRow - Resulting areas: 1) $r1 2) $r2")
      
      stack += r1
      stack += r2
    }
  }
  
  def getRowsWithWall(a:Rectangle,map:Map)={
//    println("row range check Passage:"+(a.x until a.x+a.h))
    val leftRowsWithPassage= (a.x until a.x+a.h) filter {row => map(row,a.y-1)==Dungeon.Passage}
    val rightRowsWithPassage= (a.x until a.x+a.h) filter {row => map(row,a.y+a.w)==Dungeon.Passage}
//    println("leftRowsWithPassage:"+leftRowsWithPassage.toList+ ", rightRowsWithPassage"+rightRowsWithPassage.toList )
    leftRowsWithPassage.toSet++rightRowsWithPassage.toSet
  }
  def getColumnsWithPassage(a:Rectangle,map:Map)={
    val leftColumnsWithPassage= (a.y until a.y+a.w) filter {column => map(a.x-1,column)==Dungeon.Passage}
    val rightColumnsWithPassage= (a.y until a.y+a.w) filter {column => map(a.x+a.h,column)==Dungeon.Passage}
    leftColumnsWithPassage.toSet++rightColumnsWithPassage.toSet
  }
  
  def splitPoint(size: Int) = {
//    val mu = (size.toFloat / 2)
//    val sd = (size.toFloat / 20)
//    var at = (r.nextGaussian() * sd + mu).toInt
//    
//    println(s"Splitting with mu=$mu sd=$sd  at $at")
//    at
      val m=1
      var at= r.nextInt(size-m-1)+m
      at = Seq(Seq(at, size - m-1).min, m).max
      at
       
  }
  def doorPoint(size: Int) = {
      val m=1
      var at= r.nextInt(size-m)+m
      at = Seq(Seq(at, size-m).min, m).max
      at
       
  }

  def drawHorizontalWall(map: Map, area: Rectangle, at: Int) {
    val fromCol = area.y;
    val toCol = fromCol + area.w;
    val correctedAt = area.x + at
    for (c <- fromCol until toCol) {
      map(correctedAt, c) = Dungeon.Wall
    }

  }
  def drawVerticalWall(map: Map, area: Rectangle, at: Int) {
    val fromRow = area.x;
    val toRow = fromRow + area.h;
    val correctedAt = area.y + at
    for (r <- fromRow until toRow) {
      map(r, correctedAt) = Dungeon.Wall
    }

  }
  
  def finished = stack.isEmpty

}