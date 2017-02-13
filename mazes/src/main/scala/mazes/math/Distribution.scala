package mazes.math

import scala.util.Random

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
