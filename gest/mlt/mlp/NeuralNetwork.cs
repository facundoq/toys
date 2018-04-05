using MathNet.Numerics.LinearAlgebra.Double;
using mlt.classification;
using mlt.utility;
using utilities;
using System.Collections.Generic;
using System.Linq;


namespace mlt.mlp{

  public class NetworkOutput {
    public DenseVector r1, r2, o0, o1, o2;
  }

  public class NeuralNetwork : Classifier{
    public NeuralNetworkParameters p;
    public DenseMatrix w1, w2;

    public NeuralNetwork(NeuralNetworkParameters p){
      this.p = p;
      initializeWeights(p);
    }

    public DenseVector run(double[] input){
      return run(new DenseVector(input));
    }

    public NetworkOutput runForTraining(double[] input){
      return runForTraining(new DenseVector(input));
    }

    public NetworkOutput runForTraining(DenseVector input){
      var o = new NetworkOutput();
      o.o0 = input;
      o.r1 = w1*input;
      o.o1 = new DenseVector(o.r1);
      Sigmoid.a(o.o1);
      o.r2 = w2*o.o1;
      o.o2 = new DenseVector(o.r2);
      Sigmoid.a(o.o2);
      return o;
    }

    public DenseVector runWithBias(DenseVector input){
      var o1 = w1 * input;
      Sigmoid.a(o1);
      var o2 = w2 * o1;
      Sigmoid.a(o2);
      //Console.WriteLine(o2);
      return o2;
    }

    public DenseVector run(DenseVector input){
      input=input.insert(1d, 0);
      return runWithBias(input);
    }

    private void initializeWeights(NeuralNetworkParameters p){
      w1 = new DenseMatrix(p.hiddenNodes, p.inputSize);
      w2 = new DenseMatrix(p.outputs, p.hiddenNodes);
    }

    public double[] classify(double[] input){
      return run(input).ToArray();
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass){
      return actualOutput[expectedClass] > actualOutput.Where((v,i) => i!=expectedClass ).Max();
    }
  }
}