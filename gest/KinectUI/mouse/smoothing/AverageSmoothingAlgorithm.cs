using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using utilities;

namespace KinectUI.mouse{
  public class AverageSmoothingAlgorithm : WindowedSmoothingAlgorithm{
    public AverageSmoothingAlgorithm(int windowSize) : base(windowSize){
    }

    protected override Position doSmooth(){
      Position result = new Position(0, 0);
      foreach (Position p in this.values){
        result.x += p.x;
        result.y += p.y;
      }
      result.x /= this.values.Count;
      result.y /= this.values.Count;
      return result;
    }
  }
}