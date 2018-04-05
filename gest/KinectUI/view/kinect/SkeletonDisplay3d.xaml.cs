using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Media.Media3D;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Kinect;
using Kinect.Toolbox;
using KinectUI.mouse;
using KinectUI.observer;
using System.Windows.Controls;
using SampleLibrary;
using utilities;

namespace KinectUI.view.kinect{
  public partial class SkeletonDisplay3d : UserControl{
    private Skeleton3d skeleton { get; set; }

    public SkeletonDisplay3d(){
      InitializeComponent();
      var options = new SkeletonDrawer3dOptions(Colors.Orange, JointType.HandLeft.inList());
      skeleton = new Skeleton3d(options);
      viewport.Camera.Position = new Point3D(0, 0, 5);
      viewport.Camera.LookDirection = new Vector3D(0, 0, -10);
      viewport.Camera.UpDirection = new Vector3D(0, 1, 0);
      viewport.Camera.NearPlaneDistance = 0;
      viewport.Camera.FarPlaneDistance = 100;
      this.DataContext = skeleton;
      viewport.Children.Add(skeleton);
    }

    public void draw(SkeletonFrameEvent e){
      this.draw(e.skeletons, e.frame, e.sensor);
    }

    public void draw(Skeleton[] skeletons, FrameInfo frame, KinectSensor sensor){
      skeleton.draw(skeletons, frame, sensor);
    }

    public void draw(Skeleton skeleton, FrameInfo frame, KinectSensor sensor){
      this.skeleton.draw(skeleton, frame, sensor);
    }
  }
}