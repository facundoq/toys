using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SampleLibrary;
using utilities;

namespace KinectUI.gestures{
  public class SkeletonAddedToWindowEvent : EventArgs{
    public SkeletonFrameInfo frame;

    public SkeletonAddedToWindowEvent(SkeletonFrameInfo frame){
      this.frame = frame;
    }
  }

  public class SkeletonWindow{
    public List<SkeletonFrameInfo> frames;
    public int size;
    public event EventHandler<SkeletonAddedToWindowEvent> added;

    public SkeletonWindow(int size){
      this.size = size;
      frames = new List<SkeletonFrameInfo>();
    }

    public void add(SkeletonFrameInfo info){
      frames.Add(info);
      if (isFull()){
        frames.RemoveAt(0);
      }
      if (this.added != null){
        this.added(this, new SkeletonAddedToWindowEvent(info));
      }
    }

    public bool isFull(){
      return frames.Count == size;
    }

    public Sample sampleForTheLast(int ms){
      var result = new List<SkeletonFrameInfo>();
      var beginning = frames.Last().frame.msTimestamp - ms;
      var i = frames.Count - 1;
      while (i >= 0 && frames.ElementAt(i).frame.msTimestamp > beginning){
        result.add(frames.ElementAt(i));
      }
      return new Sample(result, DateTime.Now);
    }
  }
}