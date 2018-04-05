using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Windows;
using KinectUI.gestures;
using KinectUI.mouse;
using KinectUI.mouse.utility;
using KinectUI.actions;
using System.Windows.Shapes;
using System.Windows.Media;
using KinectUI.view;
using KinectUI.observer;
using SampleLibrary;

namespace KinectUI{
  public partial class MainWindow{
    private PostureSystem postureSystem { get; set; }
    private GestureSystem gestureSystem { get; set; }
    private ActionSystem actionSystem;
    private MouseSystem mouseSystem { get; set; }
    //View view;
    private FrameInfo frame;

    public event EventHandler<SkeletonFrameEvent> newFrame;

    private void updateSystems(Skeleton[] skeletons, SkeletonFrame skeletonFrame){
      manager.tracking(true);
      if (frame == null){
        frame = new FrameInfo(skeletonFrame);
      }
      frame = frame.next(skeletonFrame);
      SkeletonFrameEvent e = new SkeletonFrameEvent(skeletons, frame, manager.kinectSensor);
      if (newFrame != null){
        newFrame(this, e);
      }
      if (e.trackedSkeletonsExists()){
        skeletonDisplay.draw(e);
        //view.update(skeleton);
        //postureSystem.update(e);
        positions.update(e);
        //gestureSystem.update(e);
        //mouseSystem.update(e);
      }
    }

    private void initializeSystems(){
      //view = new View(position, console, gestureLog);
      gestureSystem = db.getGestureSystem();
      gestureSystem.detected += gestureSystem_detected;
      postureSystem = new PostureSystem();
      actionSystem = new ActionSystem(postureSystem, gestureSystem);
      mouseSystem = new MouseSystem(manager.kinectSensor, new AbsoluteJointScreenPositionMapper(), JointType.HandRight);
      HardcodedActionSystemInitializer.initialize(actionSystem);
      gestures.initialize(gestureSystem);
    }

    private void gestureSystem_detected(object sender, GestureDetectedEvent e){
      var title = "Gesture " + e.gesture.id + " detected ";
      var message = DateTime.Now.ToLongTimeString();
      Console.WriteLine(title);
      trayIconView.showBalloon(title, message);
    }
  }
}