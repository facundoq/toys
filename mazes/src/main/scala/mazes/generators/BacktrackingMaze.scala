package mazes.generators

import mazes.generators.DungeonGenerator.Size
import mazes.generators.DungeonGenerator.Map
import mazes.generators.DungeonGenerator.Move
import mazes.math.Categorical
import mazes.generators.Dungeon.Direction
import scala.collection.mutable.ListBuffer

object BacktrackingMaze {
  class asInt(b: Boolean) {
    def toInt = if (b) 1 else 0
  }
  implicit def convertBooleanToInt(b: Boolean) = new asInt(b)

}

class BacktrackingMaze(s: Size, var keepDirectionProbability: Float, var maxBacktracks: Int) extends DungeonGenerator(s) {
  import mazes.generators.Position._

  implicit def position2tuple(p: Position) = (p.x, p.y)

  var map: Map = Matrix(size, Dungeon.Empty)

  var p = Position(map.randomIndex(r))
  var d: Dungeon.Direction = Dungeon.South
  var visitedOrWallCount = 1
  var path = ListBuffer(p)
  var direction = ListBuffer(d)
  
  path += p
  direction += d
  
  var i = 0
  var backtracks=0

  def finished = path.isEmpty || backtracks > maxBacktracks

  def generate() = {
    while (!finished) { step() }
    map
  }

  def step() {
    p = path.last
    d = direction.last    
    println(i + " -> " + p)
    
    map(p.x, p.y) = Dungeon.Passage;
    updateWall(p, map)
    i += 1
    carvePassage(map, d, p) match {
      case None => {
        path.remove(path.length - 1)
        direction.remove(direction.length - 1)
        backtracks+=1
      }
      case Some((newP, newD)) => {
        if (r.nextFloat()<0.1){
          path.remove(path.length - 1)
          direction.remove(direction.length - 1)
        }
        path+=newP  
        direction+=newD
      }

    }
  }

  def possibleMoves(p: Position, m: Map): Seq[Move] = {
    p.moves filter ({ case (p, d) => m.valid(p) && m(p.x, p.y) == Dungeon.Empty })
  }

  def carvePassage(m: Map, d: Dungeon.Direction, p: Position): Option[Move] = {

    val moves = possibleMoves(p, m)
    //println(moves)
    if (moves.isEmpty) {
      // deadend, look somewhere else
      return None
    }

    val moveDistribution = new Categorical(moveProbabilities(moves, m, d).toList)
    val moveIndex = moveDistribution.draw()

    return Some(moves(moveIndex))
  }
  def moveProbabilities(moves: Seq[Move], m: Map, previousDirection: Direction): List[Float] = {
    //      val newDirectionProbability = (1 - keepDirectionProbability) / (possiblePositions.length - 1)
    //      
    //      var mantainDirectionFactor = List.fill(Util.Directions.length)(newDirectionProbability)
    //      mantainDirectionFactor.updated(previousDirectionIndex, keepDirectionProbability)
    //    

    //var positionProbabilities = (validPositionFactor, mantainDirectionFactor).zipped.map(_ * _)

    var moveProbabilities = List.fill(moves.length)(1.0f / moves.length)

    val previousDirectionIndex = moves indexWhere ({ m => m._2 == previousDirection })
    if (previousDirectionIndex != -1) {
      val newProbability = moveProbabilities(previousDirectionIndex) * keepDirectionProbability
      moveProbabilities = moveProbabilities.updated(previousDirectionIndex, newProbability)
    }
    val totalProbability = moveProbabilities.sum

    moveProbabilities = moveProbabilities.map(_ / totalProbability)
    return moveProbabilities
  }

  def updateWall(p: Position, map: Map) {
    val adyacentPositions = p.adyacent filter ({ p => map.valid(p) && map(p.x, p.y) == Dungeon.Empty })
    adyacentPositions.foreach(p => if (convertToWall(p, map)) map(p.x)(p.y) = Dungeon.Wall)
  }

  def convertToWall(p: Position, m: Map): Boolean = {
    val border = Iterator.continually(p.adyacent).flatten.take(11).toList
    val corners = List(border.slice(1, 4), border.slice(3, 6), border.slice(5, 8), border.slice(7, 10))
    val cornerForcesWall = (c: List[Position]) => c.map(p => m.valid(p) && m(p.x, p.y).passable).reduce(_ && _)

    val cornersThatForceWall = corners filter cornerForcesWall
    !cornersThatForceWall.isEmpty
  }

}

