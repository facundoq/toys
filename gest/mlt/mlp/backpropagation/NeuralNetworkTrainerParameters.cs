namespace mlt.mlp.backpropagation{
  public class NeuralNetworkTrainerParameters{
    public int maxEpochs;
    public double e, n1, n2, u1, u2;
    public double validationPercent;

    public NeuralNetworkTrainerParameters(int maxEpochs, double e, double n1, double n2, double u1, double u2,double validationPercent){
      this.maxEpochs = maxEpochs;
      this.e = e;
      this.n1 = n1;
      this.n2 = n2;
      this.u1 = u1;
      this.u2 = u2;
      this.validationPercent = validationPercent;
    }
  }
}