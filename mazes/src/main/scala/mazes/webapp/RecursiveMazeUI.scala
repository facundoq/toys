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

class RecursiveMazeUI(base: HTMLElement) extends DungeonUI {

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
      value := "10").render
    heightInput = input(
      `type` := "text",
      value := "50").render
    widthInput = input(
      `type` := "text",
      value := "120").render
    minSideLength = input(
      `type` := "text",
      value := "5").render
    drawMazeButton = button("Redraw maze").render
    drawMazeButton.onclick = (e: dom.MouseEvent) => drawMaze()

    mazeElement = pre("").render
    val mazeElementDiv= div(mazeElement).render
    this.base.appendChild(
      div(
        h1("Maze Generator"),
        div(label("Width:"), widthInput),
        div(label("Height:"), heightInput),
        div(label("minArea:"), minArea),
        div(label("minSideLength:"), minSideLength),
        div(drawMazeButton),
        mazeElementDiv).render)
    recreateMaze()
    
    
//    mazeElementDiv.onclick = (e:dom.MouseEvent) => step()
//    step()
    setInterval(200) {
      step()
    }
  }

  def step(){
    if (!m.stack.isEmpty) {
        m.addWall()
        mazeElement.textContent = dungeonToText(m.map)
    }
  }
  def recreateMaze() {
    val s = (heightInput.value.toInt, widthInput.value.toInt)
    val minAreaVal = minArea.value.toInt;
    val minSideLengthVal = minSideLength.value.toInt;

    m = new RecursiveMaze(s, minAreaVal, minSideLengthVal)
  }
  
  def drawMaze() {

    //org.scalajs.dom.setTimeout(() => {},100)

    //org.scalajs.dom.setInterval(() =>{ } , 100)
    recreateMaze()
  }

}