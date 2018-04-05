using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using KinectUI.observer;
using SampleLibrary;
using mlt.classification;
using mlt.experiments;
using mlt.mlp;
using mlt.gmm;
using mlt.mlp.backpropagation;
using mlt.samples;
using utilities;

namespace KinectUI.gestures.trainable{
  public class IntervalInput{
    public float interval;
    public double[] input;

    public IntervalInput(float interval, double[] input){
      this.interval = interval;
      this.input = input;
    }
  }

  public class RBFDetector : TrainableGestureDetector{
    public GMM network;
    public OutputDecider d;

    public RBFDetector(JointType jointType, JointType referenceJoint, int windowSize, DetectionParameters p)
      : base(jointType, referenceJoint, windowSize, p){
      d = new OutputDecider(0.65, 0.5, 1);
    }

    public override void doUpdate(){
      decide(new DetectorOutput(p.intervals
                                 .Select(getInputForInterval).results()
                                 .Select(
                                   intervalInput =>
                                   new IntervalOutput(intervalInput.interval,
                                                      network.classify(intervalInput.input).ToArray()))
                                 .ToList()));
    }

    private Maybe<IntervalInput> getInputForInterval(float interval){
      var pp = new SamplePreprocessor();
      var centers = w.centers.positionsForTheLast(interval);
      var positions = w.positions.sampleForTheLast(interval);
      var floorClippingPlanes = w.getLastFloorClippingPlanes(positions.Length);
      var input = pp.preprocess(p.preprocessingParameters, positions, centers, floorClippingPlanes);
      // input = input.subArray(1, input.Length);
      //if (interval>1000){
      //Console.WriteLine(positions.toString());
      //Console.WriteLine(",".@join(input.splitIntoGroups(3).Select(g=> g.toString())));
      //}
      return (positions.Length > 0)
               ? Maybe<IntervalInput>.unit(new IntervalInput(interval, input))
               : Maybe<IntervalInput>.Nothing;
    }

    public void decide(DetectorOutput output){
      Console.WriteLine("-----------------------------------------");
      foreach (var x in output.outputs){
        Console.WriteLine(x.interval.ToString("0.00") + ":" + x.output.toString());
      }
      Maybe<int> m = d.decide(output.inList());
      m.ifResult(g => detect(gestures[g]));
      m.ifResult(g => Console.WriteLine(" detected:" + gestures[g].id));
    }

    public override void doTrain(PatternSets sets){

    }
  }
}