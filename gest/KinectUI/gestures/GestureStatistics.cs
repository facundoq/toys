using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.mouse.utility;
using KinectUI.observer;
using SampleLibrary;

namespace KinectUI.gestures{
  public class GestureStatistics{
    public long lastGestureTimestamp { get; set; }
    public long gesturesRecognized { get; set; }
    public long gesturesOmitted { get; set; }

    public GestureStatistics(){
      this.lastGestureTimestamp = 0;
      this.gesturesRecognized = 0;
      this.gesturesOmitted = 0;
    }

    public void gestureRecognized(GestureDetectedEvent e, FrameInfo frame){
      this.lastGestureTimestamp = frame.msTimestamp;
      this.gesturesRecognized++;
    }

    public void gestureOmitted(GestureDetectedEvent e, FrameInfo frame){
      this.gesturesOmitted++;
    }
  }
}