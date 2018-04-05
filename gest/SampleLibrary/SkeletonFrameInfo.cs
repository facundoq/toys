using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace SampleLibrary{
  [Serializable]
  public class SkeletonFrameInfo{
    public Skeleton skeleton;
    public FrameInfo frame;

    public SkeletonFrameInfo(Skeleton skeleton, FrameInfo frame){
      this.skeleton = skeleton;
      this.frame = frame;
    }

    public SkeletonFrameInfo copySkeleton(){
      return new SkeletonFrameInfo(skeleton.deepClone(), frame);
    }
  }
}