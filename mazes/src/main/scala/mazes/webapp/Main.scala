package mazes.webapp

// tutorial https://www.scala-js.org/tutorial/basic/
// canvas https://github.com/vmunier/scalajs-simple-canvas-game/blob/master/src/main/scala/simplegame/SimpleCanvasGame.scala

import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom.Element

import dom.document
import scala.scalajs.js.annotation.JSExport
import mazes.webapp.DungeonGenerator.Map
import scalatags.JsDom.all._
import mazes.webapp.DungeonGenerator.Size
import org.scalajs.dom.html._

object Main extends JSApp {
  var mazeElement: Element = null
  var drawMazeButton:Button=null
  var keepDirectionInput:Input=null
  var heightInput:Input=null
  var widthInput:Input=null
  var restartsInput:Input=null
  
  
  def main(): Unit = {
    setupUI()
    drawMaze()
  }
  
  
  def setupUI()={
    keepDirectionInput=  input(
      `type`:="text",
      value:="1"
    ).render
    heightInput=  input(
      `type`:="text",
      value:="20"
    ).render
    widthInput=  input(
      `type`:="text",
      value:="80"
    ).render
    restartsInput=  input(
      `type`:="text",
      value:="20"
    ).render
    drawMazeButton=button("Redraw maze").render
    drawMazeButton.onclick = (e: dom.MouseEvent) => drawMaze()
    
    mazeElement=pre("").render
    document.body.appendChild(
      div(
        h1("Maze Generator"),
        div(label("Width:"),widthInput),
        div(label("Height:"),heightInput),
        div(label("KeepDirection:"),keepDirectionInput),
        div(label("Restarts:"),restartsInput),
        div(drawMazeButton),
        div(mazeElement)
      ).render
    )
    
  }
  
  @JSExport
  def drawMaze(): Unit = {

    val s = (heightInput.value.toInt, widthInput.value.toInt)
    val keepDirection = keepDirectionInput.value.toFloat;
    val restarts= restartsInput.value.toInt;

    var m = new SimpleMaze(s,keepDirection,restarts)
    
    mazeElement.textContent= dungeonToText(m.generate())
  }

    
  def dungeonToText(d:Map):String={
    var r = ""
    
    for (i <- 0 until d.rows) {
      for (j <- 0 until d.columns) {
        r += d(i,j).asciiArt + ""
      }
      r += "\n"
    }
    return r
    
  }

}

