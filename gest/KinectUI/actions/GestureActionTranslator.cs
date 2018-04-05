using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KinectUI.actions{
  public class GestureActionTranslator{
    private Action action;

    public GestureActionTranslator(Action action){
      this.action = action;
    }

    public void translate(gestures.GestureDetectedEvent e){
      action.execute();
    }
  }
}