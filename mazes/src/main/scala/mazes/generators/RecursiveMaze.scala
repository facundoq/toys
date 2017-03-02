package mazes.generators

import mazes.generators.DungeonGenerator.Map
import mazes.generators.DungeonGenerator.Size
import scala.collection.mutable.ListBuffer

class Rectangle(val s: Size, val p: Position) {
  def splitHorizontally(at: Int) = {
    val (s1, s2) = splitSizeHorizontally(s, at)
    val left = new Rectangle(s1, p)
    val right = new Rectangle(s2, p + (0, s1._2 + 1))
    (left, right)
  }
  def splitSizeHorizontally(s: Size, at: Int) = {
    val (h, w) = s
    val left = (h, at)
    //leave a space for the wall in the middle 
    val right = (h, w - (at + 1))
    (left, right)
  }
  def splitSizeVertically(s: Size, at: Int) = {
    val (h, w) = s
    val top = (at, w)
    //leave a space for the wall in the middle 
    val bottom = (h - (at+1), w)
    (top, bottom)
  }
  def splitVertically(at: Int) = {
    val (s1, s2) = splitSizeVertically(s, at)
    val top = new Rectangle(s1, p)
    val bottom = new Rectangle(s2, p + ( s1._1 + 1,0))
    (top, bottom)
  }
  def tileCount() = s._1 * s._2

  override def toString() = {
    s"Rectangle, size ${this.s._1} x ${this.s._2}, at ${this.p}"
  }
}

class RecursiveMaze(s: Size, minArea: Int, minSideLength: Int) extends DungeonGenerator(s) {

  def generate(): Map = {
    var map: Map = Matrix(size, Dungeon.Passage)
    val splitHorizontallyProbability = s._2.toFloat / (s._1 + s._2)
    println(splitHorizontallyProbability)
    fillBorderWith(map, Dungeon.Wall)
    var stack = ListBuffer[Rectangle]();
    val initial = new Rectangle(s, Position());
    stack += initial
    var orientation=true
    while (!stack.isEmpty) {
      val area = stack.remove(stack.length - 1)
      println(s"$area")
      if (area.tileCount > minArea && area.s._1 > minSideLength && area.s._2 > minSideLength) {
        //if (splitHorizontallyProbability < r.nextFloat()) {
        if (orientation) {
          splitHorizontally(stack, area, map)
        } else {
          splitVertically(stack, area, map)
        }
        orientation= !orientation
      }
    }

    return map
  }

  def splitVertically(stack: ListBuffer[Rectangle], area: Rectangle, map: Map) {
    val at=splitPoint(area.s._1)
    val (r1, r2) = area.splitVertically(at)
    println(s"Resulting areas: 1) $r1 2) $r2")
    drawHorizontalWall(map, area, at)
    stack += r1
    stack += r2
  }
  def splitHorizontally(stack: ListBuffer[Rectangle], area: Rectangle, map: Map) {
    val at=splitPoint(area.s._2)
    val (r1, r2) = area.splitHorizontally(at)
    println(s"Resulting areas: 1) $r1 2) $r2")
    drawVerticalWall(map, area, at)
    stack += r1
    stack += r2
  }
  def splitPoint(size:Int)={
    val mu = (size.toFloat / 2)
    val sd = (size.toFloat / 2)
    var at = (r.nextGaussian() * sd + mu).toInt
    at = Seq(Seq(at,size - 2).min, 2).max
    println(s"Splitting with mu=$mu sd=$sd  at $at")
    at
  }
  
  def drawHorizontalWall(map: Map, area: Rectangle, at: Int) {
    val fromCol = area.p.y;
    val toCol = fromCol + area.s._2 - 1;
    val correctedAt = area.p.x + at
    for (c <- fromCol to toCol) {
      map(correctedAt,c) = Dungeon.Wall
    }

  }  
  def drawVerticalWall(map: Map, area: Rectangle, at: Int) {
    val fromRow = area.p.x ;
    val toRow = fromRow + area.s._1 - 1;
    val correctedAt = area.p.y + at
    for (r <- fromRow to toRow) {
      map(r, correctedAt) = Dungeon.Wall
    }

  }

}