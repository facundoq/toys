using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.mouse.utility;
using Microsoft.Kinect;
using System.Diagnostics;
using KinectUI.observer;
using KinectUI.mouse;
using SampleLibrary;
using utilities;
using utilities.time;

namespace KinectUI.gestures.algorithmic{
  public class SwipeGesture : BaseGesture, IGesture{
    public SwipeGesture(string id)
      : base(id){
    }

    public override string getInfo{
      get { return ""; }
    }
  }

  public class SwipeGestureDetector : WindowedOneJointGestureDetector{
    public const string SwipeRight = "SwipeRight";
    public const string SwipeLeft = "SwipeLeft";
    public SwipeGesture swipeRight;
    public SwipeGesture swipeLeft;
    public float SwipeMinimalLength { get; set; }
    public float SwipeMaximalHeight { get; set; }
    public int SwipeMininalDuration { get; set; }
    public int SwipeMaximalDuration { get; set; }

    public SwipeGestureDetector(JointType jointType)
      : base(jointType, JointType.HipCenter, 100){
      swipeRight = new SwipeGesture(SwipeRight);
      swipeLeft = new SwipeGesture(SwipeLeft);
      SwipeMinimalLength = 0.15f;
      SwipeMaximalHeight = 0.1f;
      SwipeMininalDuration = 150;
      SwipeMaximalDuration = 1000;
    }

    public override void doUpdate(){
      if (!w.isFull()){
        return;
      }
      if (ScanPositions((p1, p2) => Math.Abs(p2.y - p1.y) < SwipeMaximalHeight, // Height
                        (p1, p2) => p2.x - p1.x > 0.04f, // Progression to right
                        (p1, p2) => Math.Abs(p2.x - p1.x) > SwipeMinimalLength, // Length
                        SwipeMininalDuration, SwipeMaximalDuration)) // Duration
      {
        //Console.WriteLine(SwipeRight);
        this.detect(swipeRight);
      }
      if (ScanPositions((p1, p2) => Math.Abs(p2.y - p1.y) < SwipeMaximalHeight, // Height
                        (p1, p2) => p2.x - p1.x < -0.04f, // Progression to left 
                        (p1, p2) => Math.Abs(p2.x - p1.x) > SwipeMinimalLength, // Length
                        SwipeMininalDuration, SwipeMaximalDuration)) // Duration
      {
        //Console.WriteLine(SwipeLeft);
        this.detect(swipeLeft);
      }
    }

    public override List<IGesture> knownGestures(){
      return Utility<IGesture>.with(swipeRight, swipeLeft);
    }

    protected bool ScanPositions(Func<Position3, Position3, bool> heightFunction,
                                 Func<Position3, Position3, bool> directionFunction,
                                 Func<Position3, Position3, bool> lengthFunction, int minTime, int maxTime){
      int start = 0;

      for (int index = 1; index < w.positions.frames.Count - 1; index++){
        if (!heightFunction(w.positions.frames[start].p, w.positions.frames[index].p) ||
            !directionFunction(w.positions.frames[index].p, w.positions.frames[index + 1].p)){
          start = index;
        }

        if (lengthFunction(w.positions.frames[index].p, w.positions.frames[start].p)){
          double totalMilliseconds = (w.positions.frames[index].t - w.positions.frames[start].t);
          if (totalMilliseconds >= minTime && totalMilliseconds <= maxTime){
            return true;
          }
        }
      }

      return false;
    }
  }
}