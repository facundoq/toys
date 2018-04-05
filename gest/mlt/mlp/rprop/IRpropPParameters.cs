namespace mlt.mlp.rprop {
  public class IRpropPParameters{
    public int maxEpochs;
    public double e, nm, np;
    public double delta0, deltaMax, deltaMin;
    public double validationPercent;

    public IRpropPParameters(int maxEpochs, double e, double nm, double np, double delta0, double deltaMin, double deltaMax, double validationPercent){
      this.maxEpochs = maxEpochs;
      this.e = e;
      this.nm = nm;
      this.np = np;
      this.delta0 = delta0;
      this.deltaMax = deltaMax;
      this.deltaMin = deltaMin;
      this.validationPercent = validationPercent;
    }

    
  }
}