package mazes.webapp

// tutorial https://www.scala-js.org/tutorial/basic/
// canvas https://github.com/vmunier/scalajs-simple-canvas-game/blob/master/src/main/scala/simplegame/SimpleCanvasGame.scala

import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom.Element

import dom.document
import scala.scalajs.js.annotation.JSExport
import mazes.generators.DungeonGenerator.Map
import scalatags.JsDom.all._
import mazes.generators.DungeonGenerator.Size
import org.scalajs.dom.html._
import mazes.generators.SimpleMaze

object Main extends JSApp {
  
  def main(): Unit = {
    val ui=new RecursiveMazeUI(document.body)
    ui.setupUI()
    ui.drawMaze()
    
  }
  
  


}

