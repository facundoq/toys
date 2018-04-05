using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Kinect;
using SampleLibrary;
using utilities;

namespace SampleLibrary{
  public class NetworkInputGenerator{
    public readonly JointType jointType;
    private readonly JointType center;

    public NetworkInputGenerator(JointType jointType, JointType center){
      this.jointType = jointType;
      this.center = center;
    }

    public Tuple<List<Position4[]>, List<Position3[]>, List<Plane3[]>> generate(Gesture gesture){
      List<Position4[]> samples = generateSamples(gesture.samples);
      List<Position3[]> centers = gesture.samples.Select(s => getCenters(s)).ToList();
      List<Plane3[]> floorClippingPlanes = gesture.samples.Select(getFloorClippingPlanes).ToList();
      return Tuple.Create(samples, centers, floorClippingPlanes);
    }

    public List<Position4[]> generateSamples(List<Sample> samples){
      return samples.Select(s => generatePositions4(s)).ToList();
    }

    public Position4[] generatePositions4(Sample s){
      var positions = new Position4[s.frames.Count];
      var startingTime = s.frames[0].frame.msTimestamp;
      var trackedFrames =
        s.frames.Where(f => f.skeleton.Joints[jointType].TrackingState != JointTrackingState.NotTracked
                            && f.skeleton.Joints[center].TrackingState != JointTrackingState.NotTracked
                            && f.frame.floorClippingPlane.d != 0
          );

      foreach (var f in CollectionExtensions.enumerate(trackedFrames)){
        positions[f.i] = new Position4(){
          p = f.e.skeleton.Joints[jointType].Position.toPosition3(),
          t = f.e.frame.msTimestamp - startingTime
        };
      }
      return positions;
    }

    public Position3[] getCenters(Sample s){
      return s.frames.Select(f => f.skeleton.Joints[center].Position.toPosition3()).ToArray();
    }

    public Plane3[] getFloorClippingPlanes(Sample s){
      return s.frames.Select(f =>
                             f.frame.floorClippingPlane).ToArray();
    }
  }
}