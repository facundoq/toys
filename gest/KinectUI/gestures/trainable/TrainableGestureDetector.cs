using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Diagnostics;
using KinectUI.mouse.utility;
using SampleLibrary;
using mlt.classification;
using utilities;
using KinectUI.mouse;
using mlt;
using mlt.mlp;
using mlt.mlp.backpropagation;
using mlt.samples;

namespace KinectUI.gestures.trainable{
  public abstract class TrainableGestureDetector : WindowedOneJointGestureDetector{
    public List<TrainableGesture> gestures;
    public DetectionParameters p;

    public TrainableGestureDetector(JointType jointType, JointType referenceJoint, int windowSize, DetectionParameters p)
      : base(jointType, referenceJoint, windowSize){
      gestures = new List<TrainableGesture>();
      this.p = p;
    }

    public override List<IGesture> knownGestures(){
      return gestures.Select(g => (IGesture) g).ToList();
    }

    public void addGesture(TrainableGesture gesture){
      gestures.Add(gesture);
    }

    public void addGestures(List<TrainableGesture> gestures){
      this.gestures.AddAll(gestures);
    }

    public override void update(SkeletonFrameInfo frame){
      if (!gestures.empty() && !gestures[0].samples.empty()){
        base.update(frame);
      }
    }

    public void train(){
      PatternSet set = new PatternSet(Samples.librarySample(p.preprocessingParameters, 50, 50), 1, 0);
      PatternSets sets = new PatternSets(set, 0.3, 0);
      doTrain(sets);
    }

    public abstract void doTrain(PatternSets sets);
  }

  public class DetectionParameters{
    public PreprocessingParameters preprocessingParameters;
    //public int[] intervals = { 300, 600, 900, 1200, 1500, 2000 };
    public float[] intervals = {1000, 1500, 2000, 3000};

    public DetectionParameters(PreprocessingParameters preprocessingParameters){
      this.preprocessingParameters = preprocessingParameters;
    }
  }
}