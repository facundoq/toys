using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.mouse;

namespace KinectUI.actions{
  public class MouseAction : Action{
    private MouseEvent mouseEvent;

    public MouseAction(MouseEvent mouseEvent){
      this.mouseEvent = mouseEvent;
    }

    public override void execute(){
      Mouse.triggerEvent(mouseEvent);
    }
  }
}