using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Kinect;
using mlt.classification;

namespace KinectUI.gestures.trainable{
  public class CombinedJointGestureDetector : TrainableGestureDetector{
    public CombinedJointGestureDetector(JointType jointType, JointType referenceJoint, int windowSize,
                                        DetectionParameters p) : base(jointType, referenceJoint, windowSize, p){
    }

    public override void doUpdate(){
    }

    public override void doTrain(PatternSets sets){
    }
  }
}