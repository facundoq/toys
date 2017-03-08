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
import mazes.generators.RecursiveMaze
import scala.scalajs.js.timers.SetTimeoutHandle
import scala.scalajs.js.timers._

class RecursiveMazeUI(base: Element,speed:Int) extends DungeonUI(base,speed) {

  var mazeElement: Element = null
  var drawMazeButton: Button = null
  var minArea: Input = null
  var heightInput: Input = null
  var widthInput: Input = null
  var minSideLength: Input = null

  var m: RecursiveMaze = null

  def setupUI() {
    minArea = input(
      `type` := "text",
      value := "9").render
    heightInput = input(
      `type` := "text",
      value := "20").render
    widthInput = input(
      `type` := "text",
      value := "80").render
    minSideLength = input(
      `type` := "text",
      value := "3").render
    drawMazeButton = button("Redraw maze").render
    drawMazeButton.onclick = (e: dom.MouseEvent) => resetMaze()

    mazeElement = pre("").render
    val mazeElementDiv= div(mazeElement).render
    this.base.appendChild(
      div(
        div(label("Width:"), widthInput),
        div(label("Height:"), heightInput),
        div(label("minArea(useless):"), minArea),
        div(label("minSideLength (min 3):"), minSideLength),
        div(drawMazeButton),
        mazeElementDiv).render)
    resetMaze()
    
    
//    mazeElementDiv.onclick = (e:dom.MouseEvent) => step()
//    step()
    setInterval(speed) {
      step()
    }
  }

  def step(){
    if (!m.finished) {
        m.addWall
        drawMaze    
    }
  }
  def recreateMaze() {
    val s = (heightInput.value.toInt, widthInput.value.toInt)
    
    val minSideLengthVal = minSideLength.value.toInt;
    
    m = new RecursiveMaze(s, minSideLengthVal)
  }
  def drawMaze(){mazeElement.textContent = dungeonToText(m.map)}
  
  def resetMaze(){
    recreateMaze
    drawMaze
  }

}