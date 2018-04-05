using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace SampleLibrary{
  [Serializable]
  public class Sample{
    public List<SkeletonFrameInfo> frames;
    public DateTime time { get; set; }

    public long length{
      get { return frames[frames.Count - 1].frame.msSinceTheBeginning - frames[0].frame.msSinceTheBeginning; }
    }

    public Sample(List<SkeletonFrameInfo> frames, DateTime time){
      if (frames.Count == 0){
        throw new ArgumentException("cannot create a sample with no frames");
      }
      this.frames = frames;
      this.time = time;
    }

    public SkeletonFrameInfo getSkeletonFrameInfo(long ms){
      long beginning = frames[0].frame.msSinceTheBeginning;
      int index = frames.FindIndex(f => f.frame.msSinceTheBeginning - beginning >= ms);
      index = (index == -1) ? frames.Count - 1 : index;
      return frames[index];
    }
  }
}