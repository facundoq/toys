using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using utilities;

namespace KinectUI.mouse{
  internal class ExponentialSmoothingAlgorithm : SmoothingAlgorithm{
    private int windowSize;
    private double a;
    private readonly Queue<double> xs;
    private readonly Queue<double> ys;

    public ExponentialSmoothingAlgorithm(int windowSize, double a){
      this.a = a;
      this.windowSize = windowSize;
      ys = new Queue<double>();
      xs = new Queue<double>();
    }

    public override Position smooth(Position p){
      xs.Enqueue((double) p.x);
      ys.Enqueue((double) p.y);

      if (xs.Count > windowSize){
        xs.Dequeue();
        ys.Dequeue();
      }

      float x = (float) ExponentialMovingAverage(xs.ToArray(), a);
      float y = (float) ExponentialMovingAverage(ys.ToArray(), a);

      return new Position(x, y);
    }

    private double ExponentialMovingAverage(double[] data, double baseValue){
      double numerator = 0;
      double denominator = 0;

      double average = data.Sum();
      average /= data.Length;

      for (int i = 0; i < data.Length; ++i){
        numerator += data[i]*Math.Pow(baseValue, data.Length - i - 1);
        denominator += Math.Pow(baseValue, data.Length - i - 1);
      }

      numerator += average*Math.Pow(baseValue, data.Length);
      denominator += Math.Pow(baseValue, data.Length);

      return numerator/denominator;
    }
  }
}