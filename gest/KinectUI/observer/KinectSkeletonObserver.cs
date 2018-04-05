using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.gestures;
using Microsoft.Kinect;
using SampleLibrary;

namespace KinectUI.observer{
  internal interface KinectSkeletonObserver{
    void update(SkeletonFrameInfo frame, KinectSensor sensor);
  }
}