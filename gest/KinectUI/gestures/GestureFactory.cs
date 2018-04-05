using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using KinectUI.gestures.algorithmic;
using KinectUI.gestures.trainable;
using mlt.samples;

namespace KinectUI.gestures{
  public class GestureFactory{
    public static List<BaseGestureDetector> create(){
      List<BaseGestureDetector> gestures = new List<BaseGestureDetector>();
      //gestures.Add(new SwipeGestureDetector(JointType.HandLeft));

      //gestures.Add(new NeuralNetworkDetector(JointType.HandLeft, JointType.HipCenter, 200,new DetectionParameters(new PreprocessingParameters(60,0))));
      gestures.Add(new RBFDetector(JointType.HandLeft, JointType.HipCenter, 200,
                                   new DetectionParameters(new PreprocessingParameters(90, 0))));
      return gestures;
    }
  }
}