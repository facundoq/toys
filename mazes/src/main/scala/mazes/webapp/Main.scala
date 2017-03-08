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
import mazes.generators.BacktrackingMaze
import mazes.generators.RecursiveMaze

object Main extends JSApp {
  var container:Element=null
  var selectAlgorithm:Select=null
  var speedInput: Input = null
  
  def main(): Unit = {
    container=div().render
    selectAlgorithm=select().render
    selectAlgorithm.add(option("Recursive",value := classOf[RecursiveMaze].getName).render)
    selectAlgorithm.add(option("Backtracking",value := classOf[BacktrackingMaze].getName).render)
    
    speedInput=input(`type` := "text",value := "10").render
    document.body.appendChild(
        
        div(h1("Maze Generator"),
            label("Algorithm: "),
            selectAlgorithm,
            label("Speed (ms/step):"),
            speedInput,
            hr()
            ).render
        )
    document.body.appendChild(container)
    
    selectAlgorithm.onchange = (e: dom.Event) => {setupAlgorithm()}
    setupAlgorithm()
    
  }
  def clearContainer(){
    while (container.childElementCount>0){ container.removeChild(container.children.item(0))}
  }
  def setupAlgorithm(){
    val speed=speedInput.value.toInt
    clearContainer()
    var ui:DungeonUI=null
    if (selectAlgorithm.value== classOf[RecursiveMaze].getName ){
      ui=new RecursiveMazeUI(container,speed)  
    }else{
      ui=new BacktrackingMazeUI(container,speed)
    }
    ui.setupUI()
  }
  
  


}

