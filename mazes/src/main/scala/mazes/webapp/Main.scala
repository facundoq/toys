package mazes.webapp

// tutorial https://www.scala-js.org/tutorial/basic/
// canvas https://github.com/vmunier/scalajs-simple-canvas-game/blob/master/src/main/scala/simplegame/SimpleCanvasGame.scala

import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom.Element
import dom.document
import scala.scalajs.js.annotation.JSExport


object Main extends JSApp {
  var mazeElement: Element = null

  def main(): Unit = {
    drawMaze()
  }
  
  def generate():String ={
    var s = new Size(20, 20)
    var randomness = 0.2f;
    var m = new SimpleMaze(s, randomness)
    return m.generate()
  }
  
  @JSExport
  def drawMaze(): Unit = {
    
    if (mazeElement != null) {
      document.body.removeChild(mazeElement)
    }
    mazeElement = appendPar(document.body, generate())

  }
    def appendPar(targetNode: dom.Node, text: String): Element = {
    val parNode = document.createElement("pre")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
    return parNode
  }

}

