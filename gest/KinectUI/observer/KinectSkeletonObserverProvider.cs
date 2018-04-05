using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.gestures;
using Microsoft.Kinect;
using SampleLibrary;

namespace KinectUI.observer{
  public class SkeletonFrameEvent : EventArgs{
    public Skeleton[] skeletons;
    public FrameInfo frame;
    public KinectSensor sensor;

    public SkeletonFrameEvent(Skeleton[] skeletons, FrameInfo frame, KinectSensor sensor){
      this.skeletons = skeletons;
      this.frame = frame;
      this.sensor = sensor;
    }

    public SkeletonFrameInfo getDefaultSkeletonFrameInfo(){
      return new SkeletonFrameInfo(getDefaultSkeleton(), frame);
    }

    public Boolean trackedSkeletonsExists(){
      return getDefaultSkeleton() != null;
    }

    private Skeleton getDefaultSkeleton(){
      Skeleton defaultSkeleton = null;
      foreach (Skeleton skeleton in skeletons){
        if (skeleton.TrackingState == SkeletonTrackingState.Tracked){
          defaultSkeleton = skeleton;
          break;
        }
      }
      return defaultSkeleton;
    }
  }

  public interface IKinectSkeletonFrameProvider{
    event EventHandler<SkeletonFrameEvent> newFrame;
  }
}