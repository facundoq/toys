using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Db4objects.Db4o;
using Microsoft.Kinect;
using KinectUI.mouse.utility;
using KinectUI.systems;
using KinectUI.observer;
using SampleLibrary;

namespace KinectUI.gestures{
  public class GestureSystem : KinectSystem{
    private long pauseBetweenGestures;
    private GestureStatistics gesturesStatistics;
    private List<GestureDetector> gestureDetectors;
    [Transient] private FrameInfo lastFrame;

    private const long defaultPauseBetweenGestures = 1600;

    public GestureSystem(long pauseBetweenGestures){
      this.pauseBetweenGestures = pauseBetweenGestures;
      this.gesturesStatistics = new GestureStatistics();
      gestureDetectors = new List<GestureDetector>();
      foreach (GestureDetector gesture in GestureFactory.create()){
        this.addDetector(gesture);
      }
    }

    public GestureSystem() : this(defaultPauseBetweenGestures){
    }

    public event EventHandler<GestureDetectedEvent> detected;
    public event EventHandler<GestureDetectedEvent> omitted;

    public override void update(SkeletonFrameEvent e){
      SkeletonFrameInfo info = e.getDefaultSkeletonFrameInfo();
      this.lastFrame = info.frame;
      if (this.isRunning()){
        foreach (GestureDetector gestureDetector in this.gestureDetectors){
          gestureDetector.update(info);
        }
      }
    }

    public void addDetector(GestureDetector gestureDetector){
      gestureDetector.detected += new EventHandler<GestureDetectedEvent>(gestureDetected);
      this.gestureDetectors.Add(gestureDetector);
    }

    public List<GestureDetector> getGestureDetectors(){
      return gestureDetectors;
    }

    public List<IGesture> getGestures(){
      return gestureDetectors.SelectMany(g => g.knownGestures()).ToList();
    }

    public void gestureDetected(object sender, GestureDetectedEvent e){
      if (this.paused()){
        this.gesturesStatistics.gestureOmitted(e, lastFrame);
        if (this.omitted != null){
          this.omitted(sender, e);
        }
      }
      else{
        this.gesturesStatistics.gestureRecognized(e, lastFrame);
        if (this.detected != null){
          this.detected(sender, e);
        }
      }
    }

    private bool paused(){
      long timeSinceLastGesture = this.lastFrame.msTimestamp - this.gesturesStatistics.lastGestureTimestamp;
      return timeSinceLastGesture < pauseBetweenGestures;
    }

    public void clearEvents(){
    }
  }
}