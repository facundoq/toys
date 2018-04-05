using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Diagnostics;
using utilities;

namespace KinectUI.mouse{
  public class AbsoluteJointScreenPositionMapper : JointScreenPositionMapper{
    private Area activeArea { get; set; }
    private SmoothingAlgorithm smoothingAlgorithm { get; set; }

    public AbsoluteJointScreenPositionMapper(){
      activeArea = new Area(0.3f, 0.3f, 0.7f, 0.7f);
      //smoothingAlgorithm = new ExponentialSmoothingAlgorithm(10, 0.9d);
      //smoothingAlgorithm = new AverageSmoothingAlgorithm(10);
      smoothingAlgorithm = new NullSmoothingAlgorithm();
    }

    public override Position toScreen(Joint joint, KinectSensor sensor){
      Position position = smoothingAlgorithm.smooth(joint.xyPosition()).flipY().scaleToUnitSpace(this.userXYArea());
      //Console.WriteLine( "camera position: "+ position);       
      position = position.clipTo(activeArea);
      //Console.WriteLine("active area position: " + position);       
      Area r = this.getScreenResolution();
      //Console.WriteLine( " resolution:"+r);
      return position.scale(activeArea, r);
    }

    private Area userXYArea(){
      return new Area(-0.6f, -0.4f, 0.4f, 0.6f);
    }
  }
}