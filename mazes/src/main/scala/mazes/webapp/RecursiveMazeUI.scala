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

class RecursiveMazeUI(base: HTMLElement) extends DungeonUI {

  var mazeElement: Element = null
  var drawMazeButton: Button = null
  var minArea: Input = null
  var heightInput: Input = null
  var widthInput: Input = null
  var minSideLength: Input = null

  def setupUI() {
    minArea = input(
      `type` := "text",
      value := "10").render
    heightInput = input(
      `type` := "text",
      value := "40").render
    widthInput = input(
      `type` := "text",
      value := "80").render
    minSideLength = input(
      `type` := "text",
      value := "3").render
    drawMazeButton = button("Redraw maze").render
    drawMazeButton.onclick = (e: dom.MouseEvent) => drawMaze()

    mazeElement = pre("").render
    this.base.appendChild(
      div(
        h1("Maze Generator"),
        div(label("Width:"), widthInput),
        div(label("Height:"), heightInput),
        div(label("minArea:"), minArea),
        div(label("minSideLength:"), minSideLength),
        div(drawMazeButton),
        div(mazeElement)).render)
  }

  
  def drawMaze(){

    val s = (heightInput.value.toInt, widthInput.value.toInt)
    val minAreaVal = minArea.value.toInt;
    val minSideLengthVal = minSideLength.value.toInt;

    var m = new RecursiveMaze(s,minAreaVal,minSideLengthVal)

    mazeElement.textContent = dungeonToText(m.generate())
  }

}