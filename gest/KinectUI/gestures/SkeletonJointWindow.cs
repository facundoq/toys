using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Kinect;
using SampleLibrary;
using utilities;

namespace KinectUI.gestures{
  public class DetectionWindow{
    public SkeletonJointWindow positions, centers;
    public List<Plane3> floorClippingPlanes;
    public readonly int size;

    public DetectionWindow(JointType positions, JointType centers, int size){
      this.size = size;
      this.positions = new SkeletonJointWindow(positions, size);
      this.centers = new SkeletonJointWindow(centers, size);
      floorClippingPlanes = new List<Plane3>();
    }

    public void add(SkeletonFrameInfo frame){
      positions.add(frame);
      centers.add(frame);
      floorClippingPlanes.Add(frame.frame.floorClippingPlane);
      if (floorClippingPlanes.Count > size){
        floorClippingPlanes.RemoveAt(0);
      }
    }

    public bool isFull(){
      return floorClippingPlanes.Count == size;
    }

    public Plane3[] getLastFloorClippingPlanes(int count){
      return floorClippingPlanes.GetRange(positions.frames.Count - count, count).ToArray();
    }
  }

  public class SkeletonJointWindow{
    public JointType joint;
    private readonly int size;
    public List<Position4> frames;

    public SkeletonJointWindow(JointType joint, int size){
      this.joint = joint;
      this.size = size;
      frames = new List<Position4>(size);
    }

    protected void addElementToWindow(SkeletonFrameInfo f){
      frames.Add(new Position4(
                   f.skeleton.Joints[joint].Position.toPosition3(),
                   f.frame.msSinceTheBeginning
                   ));
    }

    public void add(SkeletonFrameInfo frame){
      addElementToWindow(frame);
      if (frames.Count > size){
        frames.RemoveAt(0);
      }
    }

    public bool isFull(){
      return frames.Count == size;
    }

    public Position3[] positionsForTheLast(float ms){
      return sampleForTheLast(ms).Select(p => p.p).ToArray();
    }

    public Position4[] sampleForTheLast(float ms){
      var result = new List<Position4>();
      var beginning = frames.Last().t - ms;
      var i = frames.Count - 1;
      while (i >= 0 && frames[i].t > beginning){
        result.Insert(0, frames[i]);
        i--;
      }
      return result.ToArray();
    }
  }
}