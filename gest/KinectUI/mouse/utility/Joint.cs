using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Windows;
using utilities;

namespace KinectUI.mouse{
  public static class JointExtension{
    public static Position3 position3(this Joint joint){
      return new Position3(joint.Position.X, joint.Position.Y, joint.Position.Z);
    }

    public static Position xyPosition(this Joint joint){
      return new Position(joint.Position.X, joint.Position.Y);
    }
  }
}