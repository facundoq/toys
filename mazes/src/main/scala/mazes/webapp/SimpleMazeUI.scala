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

import mazes.generators.SimpleMaze

class SimpleMazeUI(base: HTMLElement) extends DungeonUI {

  var mazeElement: Element = null
  var drawMazeButton: Button = null
  var keepDirectionInput: Input = null
  var heightInput: Input = null
  var widthInput: Input = null
  var restartsInput: Input = null

  def setupUI() = {
    keepDirectionInput = input(
      `type` := "text",
      value := "1").render
    heightInput = input(
      `type` := "text",
      value := "20").render
    widthInput = input(
      `type` := "text",
      value := "80").render
    restartsInput = input(
      `type` := "text",
      value := "20").render
    drawMazeButton = button("Redraw maze").render
    drawMazeButton.onclick = (e: dom.MouseEvent) => drawMaze()

    mazeElement = pre("").render
    this.base.appendChild(
      div(
        h1("Maze Generator"),
        div(label("Width:"), widthInput),
        div(label("Height:"), heightInput),
        div(label("KeepDirection:"), keepDirectionInput),
        div(label("Restarts:"), restartsInput),
        div(drawMazeButton),
        div(mazeElement)).render)
  }

  
  def drawMaze(): Unit = {

    val s = (heightInput.value.toInt, widthInput.value.toInt)
    val keepDirection = keepDirectionInput.value.toFloat;
    val restarts = restartsInput.value.toInt;

    var m = new SimpleMaze(s, keepDirection, restarts)

    mazeElement.textContent = dungeonToText(m.generate())
  }

}


