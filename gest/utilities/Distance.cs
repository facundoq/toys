using System;

namespace utilities{
  public interface Distance{
    double between(double[] a, double[] b);
  }
  public class EuclideanDistance:Distance{
    private readonly Difference difference;

    public EuclideanDistance(Difference difference){
      this.difference = difference;
    }

    public double between(double[] a, double[] b){
      var d = difference.between(a, b);
      var result = 0d;
      for (int i = 0; i < d.Length; i++){
        result += d[i]*d[i];
      }
      return Math.Sqrt(result);
    }
  }

  public class EuclideanSquaredDistance : Distance {
    private readonly Difference difference;

    public EuclideanSquaredDistance(Difference difference){
      this.difference = difference;
    }

    public double between(double[] a, double[] b){
      var d = difference.between(a, b);
      var result = 0d;
      for (int i = 0; i < d.Length; i++){
        result += d[i]*d[i];
      }
      return result;
    }
  }

  public class ManhattanDistance : Distance {
    public double between(double[] a, double[] b) {
      return a.minus(b).SumMagnitudes();
    }
  }
  public abstract class Difference{
    public abstract double between(double a, double b);
    public double[] between(double[] a, double[] b) {
      double[] result=new double[a.Length];
      for (int i = 0; i < result.Length; i++){
        result[i] = between(a[i], b[i]);
      }
      return result;
    }
  }

  public  class AngleDifference : Difference {
    // angle values in (-1,1) range
    public override  double between(double a, double b){
      var d = (a - b).abs();
      return Math.Min( d,2-d );
    }
  }
  public class CartesianDifference: Difference{
    public override double between(double a, double b) {
      return a - b;
    }
  }
}