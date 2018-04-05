using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Media;
using KinectUI.gestures.trainable;
using KinectUI.observer;
using Microsoft.Kinect;
using System.Windows.Threading;
using SampleLibrary;
using utilities;

namespace KinectUI.view.ui{
  internal class SamplePlayer{
    private Sample sample;
    private SkeletonDrawer skeletonDrawer;
    private KinectSensor sensor;
    private DispatcherTimer timer;
    private DateTime start;

    public SamplePlayer(Sample sample, SkeletonDrawer skeletonDrawer, KinectSensor sensor){
      this.sample = sample;
      this.skeletonDrawer = skeletonDrawer;
      this.sensor = sensor;
      timer = new DispatcherTimer();
      timer.Tick += new EventHandler(drawFrame);
      timer.Interval = new TimeSpan(0, 0, 0, 0, 40);
    }

    public void play(){
      skeletonDrawer.trace.Clear();
      start = DateTime.Now;
      timer.Start();
    }

    private void drawFrame(object sender, EventArgs e){
      long ms = elapsedTime();
      drawFrame(ms);
    }

    private long elapsedTime(){
      return (DateTime.Now.Ticks - start.Ticks)/10000;
    }

    private void drawFrame(long ms){
      if (sample.length <= ms){
        timer.Stop();
        if (finishedPlaying != null){
          finishedPlaying(this, EventArgs.Empty);
        }
      }
      else{
        SkeletonFrameInfo info = sample.getSkeletonFrameInfo(ms);
        skeletonDrawer.draw(info.skeleton, info.frame, sensor, new SkeletonDrawerOptions(
                                                                 Colors.Green, JointType.HandLeft.inList()));
      }
    }

    public event EventHandler finishedPlaying;
  }
}