package game

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image

package object Game{

  type Position=Point2D
  type Direction=Point2D
  type Velocity=Point2D
  class Point2D(val x:Int,val y:Int){
    def this() = this(0,0)
  }

  
  abstract class Component(){
    
  }
  
  class PositionState(val p:Position,val d:Direction,val v:Velocity) extends Component{
    def this() = this(new Position(), new Direction(), new Velocity())   
    
  }
  
  abstract class Sprite extends Component(){
    def draw(gc:GraphicsContext) 
  }
  class ImageSprite(val image:Image,val p:PositionState) extends Sprite{
      def draw(gc:GraphicsContext){
        gc.drawImage(image, p.p.x,p.p.y)
      }
  }
  
  
  class Entity(val id:Int,val p:PositionState,val s:Sprite){
  
  }
  
}