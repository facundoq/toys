using System;
using System.Collections.Generic;
using utilities;

namespace mlt.svm{
  public abstract class Kernel{
    public double[,] matrix(List<double[]> xs){
      var patterns = xs.Count;
      var result = new double[patterns,patterns];
      for (var i = 0; i < patterns; i++){
        for (var j = 0; j < patterns; j++){
          result[i, j] = at(xs[i], xs[j]);
        }
      }
      return result;
    }

    public abstract double at(double[] a, double[] b);
  }

  public class LinearKernel : Kernel{
    public override double at(double[] a, double[] b){
      return a.dot(b);
    }
  }

  public class GaussianDistanceKernel : Kernel {
    private double sd;
    private readonly Distance distance;

    public GaussianDistanceKernel(double sd,Distance distance){
      this.sd = sd;
      this.distance = distance;
    }

    public override double at(double[] a, double[] b) {
      return Math.Exp(-distance.between(a,b) / sd);
    }
  }

  public class GaussianKernel : Kernel{
    private double sd;
    public GaussianKernel(double sd){
      this.sd = sd;
    }
    public override double at(double[] a, double[] b){
      return Math.Exp(-a.distanceSquaredTo(b)/sd);
    }
  }
}