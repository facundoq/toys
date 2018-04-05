using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using SampleLibrary;

namespace KinectUI.gestures.trainable{
  public class TrainableGesture : IGesture{
    public Gesture gesture;

    public TrainableGesture(Gesture gesture){
      this.gesture = gesture;
    }

    public string getInfo{
      get{
        return
          "Samples: " + gesture.samples.Count;
      }
    }

    public string id{
      get { return gesture.id; }
      set { gesture.id = value; }
    }

    public List<Sample> samples{
      get { return gesture.samples; }
    }

    public void removeSample(Sample sample){
      gesture.removeSample(sample);
    }

    public void addSample(Sample sample){
      gesture.addSample(sample);
    }
  }
}