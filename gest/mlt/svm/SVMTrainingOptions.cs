using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.svm{
  public class SVMTrainingOptions{
    public double c;
    public readonly double eps;
    public readonly double tolerance;
    public int maxIterations;
    

    public SVMTrainingOptions(double c,double eps,double tolerance, int maxIterations){
      this.c = c;
      this.eps = eps;
      this.tolerance = tolerance;
      this.maxIterations = maxIterations;
    }
  }
}