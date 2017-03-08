package mazes.webapp

import mazes.generators.DungeonGenerator.Map
import org.scalajs.dom.Element

abstract class DungeonUI(val base:Element,val speed:Int){
  
  
  def setupUI()
  def dungeonToText(d: Map): String = {
    var r = " "
    for (j <- 0 until d.columns) {
      r+=(j % 10).toString()
    }
    r += "\n"
    for (i <- 0 until d.rows) {
      
      r+=(i % 10).toString()
      for (j <- 0 until d.columns) {
        r += d(i, j).asciiArt + ""
      }
      r += "\n"
    }
    return r

  }
}