using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Windows.Forms;
using System.Drawing;
using utilities;

namespace KinectUI.mouse{
  public abstract class JointScreenPositionMapper{
    public JointScreenPositionMapper(){
    }

    public abstract Position toScreen(Joint joint, KinectSensor sensor);

    public Area getScreenResolution(){
      System.Drawing.Rectangle workingRectangle =
        Screen.PrimaryScreen.Bounds;
      float w = (float) System.Windows.SystemParameters.PrimaryScreenWidth;
      float h = (float) System.Windows.SystemParameters.PrimaryScreenHeight;
      //1366f,766f
      return new Area(0f, 0f, workingRectangle.Width, workingRectangle.Height);
    }

    public Position toCameraPosition(SkeletonPoint position, KinectSensor sensor){
      float width = 0;
      float height = 0;
      float x = 0;
      float y = 0;

      if (sensor.ColorStream.IsEnabled){
        var colorPoint = sensor.MapSkeletonPointToColor(position, sensor.ColorStream.Format);
        x = colorPoint.X;
        y = colorPoint.Y;

        switch (sensor.ColorStream.Format){
          case ColorImageFormat.RawYuvResolution640x480Fps15:
          case ColorImageFormat.RgbResolution640x480Fps30:
          case ColorImageFormat.YuvResolution640x480Fps15:
            width = 640;
            height = 480;
            break;
          case ColorImageFormat.RgbResolution1280x960Fps12:
            width = 1280;
            height = 960;
            break;
        }
      }
      else if (sensor.DepthStream.IsEnabled){
        var depthPoint = sensor.MapSkeletonPointToDepth(position, sensor.DepthStream.Format);
        x = depthPoint.X;
        y = depthPoint.Y;

        switch (sensor.DepthStream.Format){
          case DepthImageFormat.Resolution80x60Fps30:
            width = 80;
            height = 60;
            break;
          case DepthImageFormat.Resolution320x240Fps30:
            width = 320;
            height = 240;
            break;
          case DepthImageFormat.Resolution640x480Fps30:
            width = 640;
            height = 480;
            break;
        }
      }
      else{
        width = 1;
        height = 1;
      }

      return new Position(x/width, y/height);
    }
  }
}