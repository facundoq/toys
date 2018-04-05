using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using utilities;

namespace KinectUI.mouse{
  public abstract class SmoothingAlgorithm{
    public abstract Position smooth(Position joint);
  }
}