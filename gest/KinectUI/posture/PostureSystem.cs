using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using KinectUI.posture;
using KinectUI.observer;
using SampleLibrary;

namespace KinectUI{
  public class PostureSystem{
    public List<Posture> postures;
    //public event EventHandler<PostureDetectedEvent> detected;
    public PostureSystem(){
      postures = new List<Posture>();
    }

    public Posture getPosture(string id){
      return postures.First(p => p.id == id);
    }

    public void addPosture(Posture posture){
      postures.Add(posture);
    }

    public void update(SkeletonFrameEvent e){
      SkeletonFrameInfo info = e.getDefaultSkeletonFrameInfo();
      foreach (Posture posture in this.postures){
        posture.update(info.skeleton, info.frame.msSinceLastFrame);
      }
    }
  }
}