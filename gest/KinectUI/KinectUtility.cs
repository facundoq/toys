using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using KinectUI.mouse;
using utilities;

namespace KinectUI{
  internal class KinectUtility{
    public static string printPosition(Joint joint){
      string format = "0.00";
      return joint.JointType.ToString() + "=(" + joint.Position.X.ToString(format) + "," +
             joint.Position.Y.ToString(format) + "," + joint.Position.Z.ToString(format) + ")";
    }

    public static Position getResolution(ColorImageFormat format){
      switch (format){
        case ColorImageFormat.RawYuvResolution640x480Fps15:{
          return new Position(640, 480);
        }
        case ColorImageFormat.RgbResolution640x480Fps30:{
          return new Position(640, 480);
        }
        case ColorImageFormat.YuvResolution640x480Fps15:{
          return new Position(640, 480);
        }
        case ColorImageFormat.RgbResolution1280x960Fps12:{
          return new Position(1280, 960);
        }
      }
      return new Position(1, 1);
    }
  }
}