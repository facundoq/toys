package mazes.math

import scala.util.Random

abstract class Distribution[T] {
  def draw(): T
}

class Categorical(var probabilities: List[Float]) extends Distribution[Int] {

  def draw()= {
    var cumulativeProbabilities = probabilities.scanLeft(0f)(_ + _)
    var p = new Random().nextFloat()
    cumulativeProbabilities.indexWhere(_ > p)-1
  }
}

class Normal(val u:Double,val std:Double) extends Distribution[Double]{
  
  def draw()= new Random().nextDouble()*std+u
  
}