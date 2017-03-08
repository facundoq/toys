package mazes.webapp

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom
import org.scalajs.dom.Element

import dom.document
import scala.scalajs.js.annotation.JSExport
import mazes.generators.DungeonGenerator.Map
import scalatags.JsDom.all._
import mazes.generators.DungeonGenerator.Size
import org.scalajs.dom.html._
import scala.scalajs.js.timers._
import mazes.generators.BacktrackingMaze

class BacktrackingMazeUI(base: Element,speed:Int) extends DungeonUI(base,speed) {

  var mazeElement: Element = null
  var drawMazeButton: Button = null
  var keepDirectionInput: Input = null
  var heightInput: Input = null
  var widthInput: Input = null
  var maxBacktracksInput: Input = null

  var m: BacktrackingMaze = null
  var h:SetIntervalHandle=null
  
  val defaultSize= (20,80)
  val defaultMaxBacktracks=Seq(defaultSize._1,defaultSize._2).max*2
  def setupUI() = {
    keepDirectionInput = input(
      `type` := "text",
      value := "1").render
    heightInput = input(
      `type` := "text",
      value := defaultSize._1.toString).render
    widthInput = input(
      `type` := "text",
      value := defaultSize._2.toString ).render
    maxBacktracksInput = input(
      `type` := "text",
      value := defaultMaxBacktracks.toString).render
    drawMazeButton = button("Redraw maze").render
    drawMazeButton.onclick = (e: dom.MouseEvent) => resetMaze()

    mazeElement = pre("").render
    this.base.appendChild(
      div(
        div(label("Width:"), widthInput),
        div(label("Height:"), heightInput),
        div(label("KeepDirection:"), keepDirectionInput),
        div(label("maxBacktracks:"), maxBacktracksInput),
        div(drawMazeButton),
        div(mazeElement)).render)
    resetMaze
    
    
    
  }

  def step() {
    if (!m.finished){
      m.step()
      drawMaze
    }else{
      clearInterval(h)
    }
  }
  
  def resetMaze() {
    recreateMaze
    drawMaze
    h = setInterval(speed) {
      step()
    }
  }
  def recreateMaze() {
    val s = (heightInput.value.toInt, widthInput.value.toInt)
    val keepDirection = keepDirectionInput.value.toFloat;
    val maxBacktracks = maxBacktracksInput.value.toInt;

    m = new BacktrackingMaze(s, keepDirection, maxBacktracks)
  }
  def drawMaze() {
    mazeElement.textContent = dungeonToText(m.map)
  }

}


