        
        object Test {
        
          import scalafx.Includes._
        import scalafx.application.JFXApp
        import scalafx.application.JFXApp.PrimaryStage
        import scalafx.stage.Stage
        
        import scalafx.scene.Scene
        import scalafx.scene.canvas.Canvas
        import scalafx.scene.canvas.GraphicsContext
        
        import scalafx.scene.input.MouseEvent
        import scalafx.event.EventHandler
        
        
        
        class GUI(val canvas:Canvas
          ,val stage: Stage
          ,val gc: GraphicsContext){}
        
        object Main extends JFXApp {
          
          var gui=setupGraphics()
          var canvas= gui.canvas
          
          def setupGraphics():GUI={
            val canvas = new Canvas(400, 400)
            canvas.translateX=0
            canvas.translateY=0
            val gc = canvas.graphicsContext2D
            
           canvas.handleEvent(MouseEvent.MouseClicked){
              a: MouseEvent => {
                println("Mouse pos:" + a.sceneX+ "  " + a.sceneY)
              }      
          }
         
            stage = new PrimaryStage {
                title = "asd"
                height = 600
                width = 800
                scene=new Scene{content=canvas}
             }
           
            return new GUI(canvas,stage,gc)
          }
        
        }
        
        }