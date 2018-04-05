using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using mlt.classification;
using utilities;

namespace mlt.svm{

 
  public class SVM : Classifier{
    public List<double[]> xs;
    public double[] a;
    public double b;
    public double[] y;
    public Kernel k;

    public SVM(List<double[]> xs, double[] a, double b, double[] y, Kernel k){
      this.xs = xs;
      this.a = a;
      this.b = b;
      this.y = y;
      this.k = k;
    }

    public double[] classify(double[] input){
      double result = -b + a.Select((t, i) => t*y[i]*k.at(xs[i], input)).Sum();
      return result.inArray();
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass){
      return false;
    }
  }
}