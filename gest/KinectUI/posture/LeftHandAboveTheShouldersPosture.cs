using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using KinectUI;
using KinectUI.posture;

namespace KinectUI.gestures{
  public class LeftHandAboveTheShouldersPosture : Posture{
    public LeftHandAboveTheShouldersPosture()
      : base("LeftHandAboveTheShoulders"){
    }

    public override void update(Skeleton skeleton, long elapsedTime){
      bool inPosture = skeleton.Joints[JointType.HandLeft].Position.Y >
                       skeleton.Joints[JointType.ShoulderLeft].Position.Y &&
                       skeleton.Joints[JointType.HandLeft].Position.Y > skeleton.Joints[JointType.Head].Position.Y;
      this.isOnOnlyIf(inPosture);
    }
  }
}