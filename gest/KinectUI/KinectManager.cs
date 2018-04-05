using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Windows;

namespace KinectUI{
  public class KinectManager{
    private MainWindow window;
    public KinectSensor kinectSensor;
    public event EventHandler<KinectStateEvent> statusChanged;

    public enum State{
      Disconnected,
      NotReady,
      Unpowered,
      PoweredTracking,
      PoweredNotTracking,
      Initializing
    }

    public State state;

    private void raiseStatusChange(){
      if (this.statusChanged != null){
        this.statusChanged(this, new KinectStateEvent(state));
      }
    }

    public KinectManager(MainWindow window){
      this.window = window;
      KinectSensor.KinectSensors.StatusChanged += kinectStatusChanged;
      state = State.Disconnected;
      this.raiseStatusChange();
    }

    public void onLoad(){
      //loop through all the Kinects attached to this PC, and start the first that is connected without an error.
      foreach (KinectSensor kinect in KinectSensor.KinectSensors){
        if (kinect.Status == KinectStatus.Connected){
          kinectSensor = kinect;
          break;
        }
      }
      if (KinectSensor.KinectSensors.Count == 0){
        state = State.Disconnected;
      }
      else if (kinectSensor == null){
        state = State.Unpowered;
      }
      else{
        state = State.PoweredNotTracking;
        initializeSensor();
      }
      this.raiseStatusChange();
    }

    private void initializeSensor(){
      kinectSensor.DepthStream.Range = DepthRange.Near;
      kinectSensor.SkeletonStream.TrackingMode = SkeletonTrackingMode.Default;
      kinectSensor.SkeletonStream.EnableTrackingInNearRange = true;
      window.Initialize();
      kinectSensor.SkeletonStream.Enable(getSmoothingParameters());
      //kinectSensor.SkeletonStream.Enable();
      kinectSensor.Start();
    }

    private static TransformSmoothParameters getSmoothingParameters(){
      TransformSmoothParameters smoothingParam = new TransformSmoothParameters();
      {
        smoothingParam.Smoothing = 0.2f;
        smoothingParam.Correction = 0.3f;
        smoothingParam.Prediction = 0.1f;
        smoothingParam.JitterRadius = 0.05f;
        smoothingParam.MaxDeviationRadius = 0.04f;
      }
      ;
      return smoothingParam;
    }

    private void clean(){
      kinectSensor.Dispose();
      kinectSensor = null;
      window.Clean();
    }

    public void tracking(bool tracking){
      State previousState = this.state;
      if (tracking){
        this.state = State.PoweredTracking;
      }
      else{
        this.state = State.PoweredNotTracking;
      }
      if (this.state != previousState){
        this.raiseStatusChange();
      }
    }

    public void kinectStatusChanged(object sender, StatusChangedEventArgs e){
      switch (e.Status){
        case KinectStatus.Connected:
          if (kinectSensor == null){
            state = State.PoweredNotTracking;
            kinectSensor = e.Sensor;
            initializeSensor();
          }

          break;
        case KinectStatus.Disconnected:
          if (kinectSensor == e.Sensor){
            state = State.Disconnected;
            clean();
          }
          break;
        case KinectStatus.NotReady:
          state = State.NotReady;
          break;
        case KinectStatus.NotPowered:
          if (kinectSensor == e.Sensor){
            state = State.Unpowered;
            clean();
          }
          break;
        case KinectStatus.Initializing:
          state = State.Initializing;
          break;
        default:
          MessageBox.Show("Unhandled Status: " + e.Status);
          break;
      }
      this.raiseStatusChange();
    }

    public bool powered(){
      return state == KinectManager.State.PoweredNotTracking || state == KinectManager.State.PoweredTracking;
    }
  }
}