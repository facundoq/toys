using System;
using System.Collections.Generic;
using MathNet.Numerics.LinearAlgebra.Double;
using MathNet.Numerics.LinearAlgebra.Generic;
using mlt.classification;
using mlt.experiments;
using mlt.utility;
using utilities;
using System.Linq;

namespace mlt.mlp.backpropagation{
  public class NeuralNetworkTrainer : Trainer<NeuralNetwork>{
    private readonly NeuralNetworkTrainerParameters tp;
    private readonly NeuralNetworkParameters p;

    public NeuralNetworkTrainer(NeuralNetworkTrainerParameters tp,NeuralNetworkParameters p){
      this.tp = tp;
      this.p = p;
    }

    public NeuralNetwork train(Dataset dataset){
      NeuralNetwork network= new NeuralNetwork(p);
      train(dataset,network);
      return network;
    }

    public void train(Dataset dataset,  NeuralNetwork network) {
      network.w1.randomize(-1, 1);
      network.w2.randomize(-1, 1);
      
      var sets = dataset.split(tp.validationPercent);
      var trainingY = outputs(sets.Item1,network.p.outputs);
      var validationY = outputs(sets.Item2, network.p.outputs);
      train(sets.Item1, trainingY,sets.Item2,validationY, network);
    }

    private double[] makeOutput(int klass,int classes){
      var output = 0d.inArray(classes);
      output[klass] = 1;
      return output;
    }
    private double[][] outputs(Dataset set,int classes){
      return set.patterns.Select(p => makeOutput(p.y, classes)).ToArray();
    }

    private void train(Dataset training, double[][] trainingY, Dataset validation, double[][] validationY, NeuralNetwork network) {
      var xs = training.patterns.clone();
      var r = new TrainingState(xs.Count, tp.maxEpochs, tp.e);

      while (!r.finished()){
        xs.shuffle();
        double trainingError = trainBatch(xs,trainingY, network, r);
        double validationError = patternSetError(network, validation,validationY);
        r.update(validationError, trainingError);
        if (r.batches%(tp.maxEpochs/4) == 0){
          //Console.WriteLine("\n" + r);
          //Console.WriteLine(network.w1);
          // Console.WriteLine(network.w2);
        }
      }
    }

    public double patternSetError(NeuralNetwork network, Dataset validation, double[][] validationY){
      var xs = validation.patterns;
      double error = 0;
      for (int i = 0; i < xs.Count; i++){
        var o = network.runWithBias(xs[i].x);
        var y = validationY[i];
        error += (o - new DenseVector(y)).SumMagnitudes();
      }
      return error/(xs.Count);
    }

    private double trainBatch(List<Pattern> xs, double[][] trainingY, NeuralNetwork network, TrainingState r){
      double error = 0;
      for (int i = 0; i < xs.Count; i++){
        var o = network.runForTraining(xs[i].x);
        var y = trainingY[i];
        error += (o.o2 - new DenseVector(y)).SumMagnitudes();
        updateWeights(o, y, network.w1, network.w2, r);
      }
      return error/(xs.Count);
    }

    private void updateWeights(NetworkOutput o, DenseVector y, DenseMatrix w1, DenseMatrix w2,
                               TrainingState r){
      var ds2 = o.r2;
      Sigmoid.da(ds2);
      var d2 = new DenseVector((y - o.o2).PointwiseMultiply(ds2));
      var ds1 = o.r1;
      Sigmoid.da(ds1);

      var lastdw1 = w1.Clone();
      lastdw1.Clear();
      var lastdw2 = w2.Clone();
      lastdw2.Clear();
      for (int k = 0; k < w2.RowCount; k++){
        updateW1(o, y, w1, d2[k], ds1, w2, k, tp.n1, tp.u1, lastdw1);
      }
      updateW2(o, y, w2, d2, tp.n2, tp.u2, lastdw2);
    }

    private void updateW1(NetworkOutput o, DenseVector y, DenseMatrix w1, double d, DenseVector ds1,
                          DenseMatrix w2, int k, double n1, double u1, Matrix<double> lastdw1){
      for (int j = 0; j < w1.RowCount; j++){
        for (int i = 0; i < w1.ColumnCount; i++){
          var lastdw1ji = lastdw1[j, i];
          lastdw1[j, i] = d*w2[k, j]*ds1[j]*o.o0[i];
          w1[j, i] += n1*lastdw1[j, i] + lastdw1ji*u1;
        }
      }
    }

    private void updateW2(NetworkOutput o, DenseVector y, DenseMatrix w2, DenseVector d2, double n2,
                          double u2, Matrix<double> lastdw2){
      for (int k = 0; k < w2.RowCount; k++){
        for (int j = 0; j < w2.ColumnCount; j++){
          var lastdw2kj = lastdw2[k, j];
          lastdw2[k, j] = d2[k]*o.o1[j];
          w2[k, j] += n2*lastdw2[k, j] + lastdw2kj*u2;
        }
      }
    }
  }
}