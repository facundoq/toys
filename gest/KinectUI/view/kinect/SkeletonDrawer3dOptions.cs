using System.Collections.Generic;
using System.Windows.Media;
using Microsoft.Kinect;

namespace KinectUI.view.kinect{
  public class SkeletonDrawer3dOptions{
    public Color skeletonColor = Colors.Orange;
    public List<JointType> highlightedJoints;

    public SkeletonDrawer3dOptions() : this(Colors.Orange, new List<JointType>()){
    }

    public SkeletonDrawer3dOptions(Color skeletonColor, List<JointType> highlightedJoints){
      this.skeletonColor = skeletonColor;
      this.highlightedJoints = highlightedJoints;
    }

    public Color highlightedColor{
      get { return Colors.Green; }
    }
  }
}