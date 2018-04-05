using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Diagnostics;
using KinectUI.mouse;
using KinectUI.mouse.utility;
using KinectUI.observer;
using SampleLibrary;
using utilities;
using utilities.time;

namespace KinectUI.gestures{
  public abstract class WindowedOneJointGestureDetector : BaseGestureDetector{
    protected DetectionWindow w;

    protected WindowedOneJointGestureDetector(JointType jointType, JointType center, int windowSize){
      w = new DetectionWindow(jointType, center, windowSize);
    }

    public override void update(SkeletonFrameInfo frame){
      w.add(frame);
      if (w.isFull()){
        doUpdate();
      }
    }

    public abstract void doUpdate();

    public void detect(IGesture gesture){
      this.detect(gesture, this.w.positions.joint.inList());
    }
  }
}