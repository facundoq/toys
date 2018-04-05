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
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Kinect;
using Kinect.Toolbox;
using KinectUI.mouse;
using KinectUI.observer;
using System.Windows.Controls;
using SampleLibrary;
using utilities;

namespace KinectUI.view.ui{
  /// <summary>
  /// Interaction logic for SkeletonViewer.xaml
  /// </summary>
  /// 
  public partial class SkeletonDisplay : UserControl{
    private SkeletonDrawer skeletonDrawer { get; set; }

    public SkeletonDisplay(){
      InitializeComponent();
      var options = new SkeletonDrawerOptions(Colors.Orange, JointType.HandLeft.inList());
      skeletonDrawer = new SkeletonDrawer(options, canvas);
    }

    public void draw(SkeletonFrameEvent e){
      this.draw(e.skeletons, e.frame, e.sensor);
    }

    public void draw(Skeleton[] skeletons, FrameInfo frame, KinectSensor sensor){
      skeletonDrawer.draw(skeletons, frame, sensor);
    }

    public void draw(Skeleton skeleton, FrameInfo frame, KinectSensor sensor){
      skeletonDrawer.draw(skeleton, frame, sensor);
    }
  }
}