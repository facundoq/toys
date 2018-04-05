using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.gestures;
using Microsoft.Kinect;
using KinectUI.observer;

namespace KinectUI.systems{
  public abstract class KinectSystem{
    private bool running;

    public KinectSystem(){
      running = true;
    }

    public virtual bool isRunning(){
      return running;
    }

    public virtual void pauseOrResume(){
      if (running){
        this.pause();
      }
      else{
        this.resume();
      }
    }

    public virtual void pause(){
      this.running = false;
    }

    public virtual void resume(){
      this.running = true;
    }

    public abstract void update(SkeletonFrameEvent e);
  }
}