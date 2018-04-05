namespace mlt.mlp{
  public class NeuralNetworkParameters{
    public NeuralNetworkParameters(int inputSize, int hiddenNodes, int outputs){
      this.inputSize = inputSize;
      this.hiddenNodes = hiddenNodes;
      this.outputs = outputs;
    }

    public readonly int inputSize;
    public readonly int hiddenNodes;
    public readonly int outputs;
  }
}