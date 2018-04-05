using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.systems;
using Microsoft.Kinect;
using KinectUI.gestures;
using KinectUI.observer;
using SampleLibrary;
using utilities;

namespace KinectUI.mouse{
  internal class MouseSystem : KinectSystem{
    private JointScreenPositionMapper mapper;
    private KinectSensor sensor;
    private JointType jointType;

    public MouseSystem(KinectSensor kinectSensor, AbsoluteJointScreenPositionMapper absoluteJointScreenPositionMapper,
                       JointType jointType)
      : base(){
      this.sensor = kinectSensor;
      this.mapper = absoluteJointScreenPositionMapper;
      this.jointType = jointType;
    }

    public override void update(SkeletonFrameEvent e){
      SkeletonFrameInfo info = e.getDefaultSkeletonFrameInfo();
      Skeleton skeleton = info.skeleton;
      if (isRunning()){
        Joint joint = skeleton.Joints[jointType];
        if (joint.TrackingState == JointTrackingState.Tracked){
          Position mousePosition = mapper.toScreen(joint, sensor);
          Mouse.setCursorPosition((int) mousePosition.x, (int) mousePosition.y);
        }
      }
    }
  }
}