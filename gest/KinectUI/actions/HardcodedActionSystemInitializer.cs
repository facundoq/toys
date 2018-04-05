using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.gestures.algorithmic;
using KinectUI.mouse;

namespace KinectUI.actions{
  public class HardcodedActionSystemInitializer{
    public static void initialize(ActionSystem actionSystem){
      MouseAction rclick = new MouseAction(MouseEvent.RightClick);
      MouseAction lclick = new MouseAction(MouseEvent.LeftClick);
      actionSystem.mapGestureToAction(SwipeGestureDetector.SwipeLeft, new GestureActionTranslator(rclick));
      actionSystem.mapGestureToAction(SwipeGestureDetector.SwipeRight, new GestureActionTranslator(lclick));
    }
  }
}