namespace utilities{
  public class MeanAndVarianceCalculator{
    public int n;
    public double sumSquared;
    public double sum;
    public MeanAndVarianceCalculator(){
      n = 0;
      sumSquared = 0;
      sum = 0;
    }
    public void update(double value){
      sum += value;
      sumSquared += value * value;
      n++;
    }
    public MeanAndVariance calculate(){
      double u = (n > 0) ? sum/n : 0;
      double uSquares = sumSquared/n;
      double sd = (n>1)? (uSquares-u*u)/(n-1) :0;
      return new MeanAndVariance(u,sd);
    }
  }
}