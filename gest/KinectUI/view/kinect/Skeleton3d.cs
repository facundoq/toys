using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Media.Media3D;
using System.Windows.Navigation;
using System.Windows.Shapes;
using HelixToolkit.Wpf;
using Microsoft.Kinect;
using Kinect.Toolbox;
using KinectUI.mouse;
using KinectUI.observer;
using SampleLibrary;
using utilities;

namespace KinectUI.view.kinect{
  public class Skeleton3d : ModelVisual3D{
    public Dictionary<JointType, Joint3D> joint3Ds;
    public SkeletonDrawer3dOptions o;
    public KinectSensor sensor { get; set; }

    public Skeleton3d(SkeletonDrawer3dOptions o){
      this.o = o;
      joint3Ds = new Dictionary<JointType, Joint3D>();
      foreach (var x in (JointType[]) Enum.GetValues(typeof (JointType))){
        var j = new Joint3D(x);
        joint3Ds[x] = j;
        Children.Add(j);
      }
    }

    public void draw(Skeleton skeleton, FrameInfo frame, KinectSensor sensor, SkeletonDrawer3dOptions options){
      Skeleton[] skeletons = new Skeleton[]{skeleton};
      //this.draw(skeletons, seated, sensor, options);
    }

    public void draw(Skeleton skeleton, FrameInfo frame, KinectSensor sensor){
      this.draw(skeleton, frame, sensor, o);
    }

    public void draw(Skeleton[] skeletons, FrameInfo frame, KinectSensor sensor){
      this.sensor = sensor;
      this.draw(skeletons, sensor.SkeletonStream.TrackingMode == SkeletonTrackingMode.Seated, frame.fps, o);
    }

    public void draw(Skeleton[] skeletons, FrameInfo frame, KinectSensor sensor, SkeletonDrawer3dOptions options){
      this.sensor = sensor;
      this.draw(skeletons, sensor.SkeletonStream.TrackingMode == SkeletonTrackingMode.Seated, frame.fps, options);
    }

    private void draw(Skeleton[] skeletons, bool seated, float fps, SkeletonDrawer3dOptions options){
      foreach (var x in skeletons){
        draw(x, seated, fps, options);
      }
    }

    private void draw(Skeleton skeleton, bool seated, float fps, SkeletonDrawer3dOptions options){
      IEnumerable<Joint> joints = skeleton.Joints;
      foreach (var x in joints){
        var vector = x.Position;
        joint3Ds[x.JointType].update(new Vector3D(vector.X, vector.Y, vector.Z));
      }
    }

    //Trace(options,JointType.HandLeft, JointType.WristLeft, skeleton.Joints);
    //Plot(options,JointType.WristLeft, skeleton.Joints);
    //Trace(options,JointType.WristLeft, JointType.ElbowLeft, skeleton.Joints);
    //Plot(options,JointType.ElbowLeft, skeleton.Joints);
    //Trace(options,JointType.ElbowLeft, JointType.ShoulderLeft, skeleton.Joints);
    //Plot(options,JointType.ShoulderLeft, skeleton.Joints);
    //Trace(options,JointType.ShoulderLeft, JointType.ShoulderCenter, skeleton.Joints);
    //Plot(options,JointType.ShoulderCenter, skeleton.Joints);

    //Trace(options,JointType.ShoulderCenter, JointType.Head, skeleton.Joints);

    //Plot(options,JointType.Head, JointType.ShoulderCenter, skeleton.Joints);

    //Trace(options,JointType.ShoulderCenter, JointType.ShoulderRight, skeleton.Joints);
    //Plot(options,JointType.ShoulderRight, skeleton.Joints);
    //Trace(options,JointType.ShoulderRight, JointType.ElbowRight, skeleton.Joints);
    //Plot(options,JointType.ElbowRight, skeleton.Joints);
    //Trace(options,JointType.ElbowRight, JointType.WristRight, skeleton.Joints);
    //Plot(options,JointType.WristRight, skeleton.Joints);
    //Trace(options,JointType.WristRight, JointType.HandRight, skeleton.Joints);
    //Plot(options,JointType.HandRight, skeleton.Joints);

    //if (!seated) {
    //    Trace(options,JointType.ShoulderCenter, JointType.Spine, skeleton.Joints);
    //    Plot(options,JointType.Spine, skeleton.Joints);
    //    Trace(options,JointType.Spine, JointType.HipCenter, skeleton.Joints);
    //    Plot(options,JointType.HipCenter, skeleton.Joints);

    //    Trace(options,JointType.HipCenter, JointType.HipLeft, skeleton.Joints);
    //    Plot(options,JointType.HipLeft, skeleton.Joints);
    //    Trace(options,JointType.HipLeft, JointType.KneeLeft, skeleton.Joints);
    //    Plot(options,JointType.KneeLeft, skeleton.Joints);
    //    Trace(options,JointType.KneeLeft, JointType.AnkleLeft, skeleton.Joints);
    //    Plot(options,JointType.AnkleLeft, skeleton.Joints);
    //    Trace(options,JointType.AnkleLeft, JointType.FootLeft, skeleton.Joints);
    //    Plot(options,JointType.FootLeft, skeleton.Joints);

    //    Trace(options,JointType.HipCenter, JointType.HipRight, skeleton.Joints);
    //    Plot(options,JointType.HipRight, skeleton.Joints);
    //    Trace(options,JointType.HipRight, JointType.KneeRight, skeleton.Joints);
    //    Plot(options,JointType.KneeRight, skeleton.Joints);
    //    Trace(options,JointType.KneeRight, JointType.AnkleRight, skeleton.Joints);
    //    Plot(options,JointType.AnkleRight, skeleton.Joints);
    //    Trace(options,JointType.AnkleRight, JointType.FootRight, skeleton.Joints);
    //    Plot(options,JointType.FootRight, skeleton.Joints);
  }
}