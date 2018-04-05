using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace SampleLibrary{
  [Serializable]
  public class Gesture{
    public List<Sample> samples { get; set; }
    public string id;

    public Gesture(string id){
      this.id = id;
      samples = new List<Sample>();
    }

    public void addSample(Sample sample){
      this.samples.Add(sample);
    }

    public void removeSample(Sample sample){
      samples.Remove(sample);
    }
  }
}