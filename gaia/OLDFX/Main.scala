import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Pos, VPos}
import scalafx.scene.Scene

import scalafx.scene.input.MouseEvent
import scalafx.scene.control.{Accordion, Label, ScrollPane, TitledPane}
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.text.{Font, Text}
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.Stop
import scalafx.scene.paint.CycleMethod
import scalafx.stage.Stage
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.RadialGradient
import scalafx.scene.image.Image
import scalafx.event.EventHandler
import scalafx.scene.input.KeyEvent
import scalafx.scene.layout.VBox
import game._
import scalafx.scene.image.ImageView
import scalafx.event._


class GUI(val canvas:Canvas
  ,val stage: Stage
  ,val gc: GraphicsContext){}

object Main extends JFXApp {
  
  var gui=setupGraphics()
  var e = setupGameObjects()
  e.s.draw(gui.gc)
 
  
  def setupGraphics():GUI={
    val canvas = new Canvas(400, 400)
    canvas.translateX=0
    canvas.translateY=0
    val gc = canvas.graphicsContext2D
    val container= new VBox()
    container.delegate.getChildren.add(canvas)
    var a=  (k: KeyEvent) => {
      
    }
    canvas.handleEvent(KeyEvent.KeyPressed){
      e: KeyEvent => {
        println("text: " + e.text + "  " + "code name: " + e.code.name)
       
      }      
    }
   
    stage = new PrimaryStage {
        title = "Distopia"
        height = 600
        width = 800
        scene=new Scene{content=container}
     }
   
    return new GUI(canvas,stage,gc)
  }
  
  def setupGameObjects():Game.Entity ={
//var image = new Image(this.getClass.getResourceAsStream("/scalafx/ensemble/images/icon-48x48.png"))
  var is=this.getClass.getResourceAsStream("/sprites/player/player.png")
  var image = new Image(is,64,64,false,true)
  //var image= new Image("http://www.scala-lang.org/resources/img/scala-logo.png")
  var p=new Game.PositionState()
  var s=new Game.ImageSprite(image,p)
  return new Game.Entity(1,p,s)
  }

}
