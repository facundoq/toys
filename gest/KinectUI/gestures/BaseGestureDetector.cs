using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Db4objects.Db4o;
using Microsoft.Kinect;
using System.Diagnostics;
using KinectUI.mouse.utility;
using KinectUI.observer;
using SampleLibrary;

namespace KinectUI.gestures{
  public interface GestureDetector{
    event EventHandler<GestureDetectedEvent> detected;
    void update(SkeletonFrameInfo frame);

    List<IGesture> knownGestures();
  }

  public abstract class BaseGestureDetector : GestureDetector{
    public BaseGestureDetector(){
    }

    public event EventHandler<GestureDetectedEvent> detected;

    public abstract void update(SkeletonFrameInfo frame);

    public abstract List<IGesture> knownGestures();

    protected void detect(IGesture gesture, List<JointType> joints){
      if (this.detected != null){
        this.detected(this, new GestureDetectedEvent(gesture, joints));
      }
    }

    public override string ToString(){
      return this.GetType().Name;
    }
  }
}