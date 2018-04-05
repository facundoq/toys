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
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Kinect;
using KinectUI;

namespace KinectUI.view.ui{
  public partial class SensorOptions : ManagerAwareControl{
    protected override void initializeControl(){
      if (kinectManager.powered() && kinectManager.state == KinectUI.KinectManager.State.PoweredTracking){
        seated.IsChecked = kinectManager.kinectSensor.SkeletonStream.TrackingMode == SkeletonTrackingMode.Seated;
        near.IsChecked = kinectManager.kinectSensor.SkeletonStream.EnableTrackingInNearRange;
        this.IsEnabled = true;
      }
      else{
        this.IsEnabled = false;
      }
    }

    protected override void managerStatusChanged(object sender, KinectStateEvent e){
      initializeControl();
    }

    public SensorOptions(){
      InitializeComponent();
      seated.Checked += new RoutedEventHandler(seated_Checked);
      near.Checked += new RoutedEventHandler(near_Checked);
    }

    private void near_Checked(object sender, RoutedEventArgs e){
      kinectManager.kinectSensor.SkeletonStream.EnableTrackingInNearRange = seated.IsChecked == true;
    }

    private void seated_Checked(object sender, RoutedEventArgs e){
      kinectManager.kinectSensor.SkeletonStream.TrackingMode = (seated.IsChecked == true)
                                                                 ? SkeletonTrackingMode.Seated
                                                                 : SkeletonTrackingMode.Default;
    }
  }
}