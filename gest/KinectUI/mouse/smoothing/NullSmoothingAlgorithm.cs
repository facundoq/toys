using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using utilities;

namespace KinectUI.mouse{
  public class NullSmoothingAlgorithm : SmoothingAlgorithm{
    public override Position smooth(Position p){
      return p;
    }
  }
}