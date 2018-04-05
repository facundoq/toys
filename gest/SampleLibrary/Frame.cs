using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using utilities.time;

namespace SampleLibrary{
  [Serializable]
  public class FrameInfo{
    public long number;
    public long nsSinceLastFrame;

    public long msSinceLastFrame{
      get { return nsSinceLastFrame/10000; }
    }

    public DateTime time;

    public long msTimestamp{
      get { return time.Ticks/10000; }
    }

    public long msSinceTheBeginning{
      get { return (time.Ticks - beginning.Ticks)/10000; }
    }

    public readonly Plane3 floorClippingPlane = new Plane3();
    public DateTime beginning;
    public float fps;

    public FrameInfo(SkeletonFrame skeletonFrame, DateTime beginning, float fps, long number, long nsSinceLastFrame,
                     DateTime time){
      this.floorClippingPlane = new Plane3(skeletonFrame.FloorClipPlane);
      this.beginning = beginning;
      this.fps = fps;
      this.number = number;
      this.time = time;
      this.nsSinceLastFrame = nsSinceLastFrame;
    }

    public FrameInfo(SkeletonFrame skeletonFrame) : this(skeletonFrame, DateTime.Now, 0, 0, 0, DateTime.Now){
    }

    public FrameInfo next(SkeletonFrame skeletonFrame){
      return this.next(skeletonFrame, DateTime.Now);
    }

    private FrameInfo next(SkeletonFrame skeletonFrame, DateTime newTime){
      long msBetweenFrames = (newTime.Ticks - this.time.Ticks)/10000;
      long newTimestamp = msTimestamp + msBetweenFrames;
      float fps = number/((msSinceTheBeginning + 1)/1000 + 1);
      return new FrameInfo(skeletonFrame, beginning, fps, this.number + 1, msBetweenFrames, newTime);
    }

    public TimeInfo getTimeInfo(){
      return new TimeInfo(this.msSinceLastFrame, this.msTimestamp);
    }
  }
}