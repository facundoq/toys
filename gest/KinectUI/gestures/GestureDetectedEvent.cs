using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace KinectUI.gestures{
  public class GestureDetectedEvent : EventArgs{
    public IGesture gesture;
    public List<JointType> joints;

    public GestureDetectedEvent(IGesture gesture, List<JointType> joints){
      this.gesture = gesture;
      this.joints = joints;
    }
  }
}