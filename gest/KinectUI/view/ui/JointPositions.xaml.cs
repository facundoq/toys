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
using KinectUI.mouse;
using KinectUI.observer;
using Microsoft.Kinect;
using SampleLibrary;

namespace KinectUI.view.ui{
  /// <summary>
  /// Interaction logic for JointPositions.xaml
  /// </summary>
  public partial class JointPositions : UserControl{
    public JointPositions(){
      InitializeComponent();
    }

    public void update(SkeletonFrameEvent e){
      if (e.trackedSkeletonsExists()){
        var skeleton = e.getDefaultSkeletonFrameInfo().skeleton;
        string positions = "";
        //positions+= skeleton.Joints[JointType.HandLeft].toString();
        //positions += "\n" + skeleton.Joints[JointType.HipCenter].toString();
        //positions += "\n" + skeleton.Joints[JointType.HandRight].toString();
        var lefthand = skeleton.Joints[JointType.HandLeft].position3() -
                       skeleton.Joints[JointType.HipCenter].position3();
        lefthand.y += e.frame.floorClippingPlane.d;
        positions += "\n normalized position with floor:" + lefthand;
        positions += "\n floor :" + e.frame.floorClippingPlane.ToString();

        this.positions.Content = positions;
      }
    }
  }
}