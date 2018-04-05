using System;

namespace utilities{
  public class MeanAndVariance{
    private readonly double d;
    public double u;
    public double sd;

    public MeanAndVariance(double u, double sd){
      this.u = u;
      this.sd = sd;
    }
    public override String ToString(){
      return u.ToString("0.00") + " (" + sd.ToString("0.0000") + ")";
    }
  }
}