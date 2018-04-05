using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using utilities;

namespace KinectUI.mouse{
  public abstract class WindowedSmoothingAlgorithm : SmoothingAlgorithm{
    public readonly FixedSizeQueue<Position> values;

    public WindowedSmoothingAlgorithm(int windowSize){
      values = new FixedSizeQueue<Position>(windowSize);
    }

    public override Position smooth(Position p){
      values.Enqueue(p);
      return doSmooth();
    }

    protected abstract Position doSmooth();
  }
}