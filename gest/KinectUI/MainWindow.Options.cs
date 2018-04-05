using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Windows;

namespace KinectUI{
  public partial class MainWindow{
    private void nearMode_Checked_1(object sender, RoutedEventArgs e){
      if (manager.kinectSensor == null){
        return;
      }

      manager.kinectSensor.DepthStream.Range = DepthRange.Near;
      manager.kinectSensor.SkeletonStream.EnableTrackingInNearRange = true;
    }

    private void nearMode_Unchecked_1(object sender, RoutedEventArgs e){
      if (manager.kinectSensor == null){
        return;
      }

      manager.kinectSensor.DepthStream.Range = DepthRange.Default;
      manager.kinectSensor.SkeletonStream.EnableTrackingInNearRange = false;
    }

    private void seatedMode_Checked_1(object sender, RoutedEventArgs e){
      if (manager.kinectSensor == null){
        return;
      }

      manager.kinectSensor.SkeletonStream.TrackingMode = SkeletonTrackingMode.Seated;
    }

    private void seatedMode_Unchecked_1(object sender, RoutedEventArgs e){
      if (manager.kinectSensor == null){
        return;
      }

      manager.kinectSensor.SkeletonStream.TrackingMode = SkeletonTrackingMode.Default;
    }
  }
}