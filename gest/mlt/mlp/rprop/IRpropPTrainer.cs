using System;
using System.Collections.Generic;
using MathNet.Numerics.LinearAlgebra.Double;
using MathNet.Numerics.LinearAlgebra.Generic;
using mlt.classification;
using mlt.experiments;
using mlt.mlp.backpropagation;
using mlt.utility;
using utilities;
using System.Linq;

namespace mlt.mlp.rprop{
  public class IRpropPTrainer : Trainer<NeuralNetwork>{
    private readonly IRpropPParameters tp;
    private readonly NeuralNetworkParameters p;
    private Matrix<double> deltaw1;
    private Matrix<double> deltaw2;
    private Matrix<double> dw1;
    private Matrix<double> dw2;

    public IRpropPTrainer(IRpropPParameters tp,NeuralNetworkParameters p){
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
      dw1=new DenseMatrix(p.hiddenNodes,p.inputSize);
      dw2=new DenseMatrix(p.outputs,p.hiddenNodes);
      deltaw1 = new DenseMatrix(p.hiddenNodes, p.inputSize,tp.delta0);
      deltaw2 = new DenseMatrix(p.outputs, p.hiddenNodes, tp.delta0);

      var previousError = 0d;
      while (!r.finished()){
        double trainingError=trainBatch(xs,trainingY, network, r,previousError);
        double validationError = patternSetError(network, validation,validationY);
        previousError = trainingError;
        r.update(validationError, trainingError);
        if (r.batches % (tp.maxEpochs / tp.maxEpochs) == 0) {
          Console.WriteLine("\n" + r);
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

    private double trainBatch(List<Pattern> xs, double[][] trainingY, NeuralNetwork network, TrainingState r, double previousError){
      double error = 0;
      for (int i = 0; i < xs.Count; i++){
        var o = network.runForTraining(xs[i].x);
        var y = trainingY[i];
        error += (o.o2 - new DenseVector(y)).SumMagnitudes();
        updateWeights(o, y, network.w1, network.w2, r,previousError);
      }
      return error/(xs.Count);
    }

    private void updateWeights(NetworkOutput o, DenseVector y, DenseMatrix w1, DenseMatrix w2, TrainingState r, double previousError){
      var ds2 = o.r2;
      Sigmoid.da(ds2);
      var d2 = new DenseVector((y - o.o2).PointwiseMultiply(ds2));
      var ds1 = o.r1;
      Sigmoid.da(ds1);
      for (int k = 0; k < w2.RowCount; k++){
        updateW1(o, y, w1, d2[k], ds1, w2, k, tp, dw1,previousError);
      }
      updateW2(o, y, w2, d2, tp, dw2, previousError);
    }

    private void updateW1(NetworkOutput o, DenseVector y, DenseMatrix w1,
      double d, DenseVector ds1, DenseMatrix w2, int k, IRpropPParameters tp, Matrix<double> dw1, double previousError){
      for (int j = 0; j < w1.RowCount; j++){
        for (int i = 0; i < w1.ColumnCount; i++){
          var lastdw1ji = dw1[j, i];
          var dw = d*w2[k, j]*ds1[j]*o.o0[i];
          dw1[j, i] = dw;
          var sign = lastdw1ji*dw1[j, i];
          var deltaw = 0d;
          if (sign > 0){
            //Console.Out.Write("1+");
            deltaw1[j, i] = Math.Min(deltaw1[j, i]*tp.np, tp.deltaMax);
            deltaw = -Math.Sign(dw) * deltaw1[j, i];
          }
          else if (sign < 0){
            //Console.Out.Write("1-");
            deltaw = -deltaw1[j, i]; // reverse last iteration changes
            deltaw1[j, i] = Math.Max(deltaw1[j, i]*tp.nm, tp.deltaMin);
            deltaw = -Math.Sign(dw) * deltaw1[j, i];
          
            dw1[j, i] = 0;

          }else{
            //Console.Out.Write("1=");
            deltaw = -Math.Sign(dw) * deltaw1[j, i];
          }
          w1[j, i] += deltaw;
        }
      }
    }

    private void updateW2(NetworkOutput o, DenseVector y, DenseMatrix w2,
      DenseVector d2, IRpropPParameters tp, Matrix<double> dw2, double previousError) {
      for (int k = 0; k < w2.RowCount; k++){
        for (int j = 0; j < w2.ColumnCount; j++){
          var lastdw2kj = dw2[k, j];
          var dw = d2[k]*o.o1[j];
          dw2[k, j] = dw;
          var sign = lastdw2kj * dw;
          double deltaw;
          if (sign > 0) {
            //Console.Out.Write("2+");
            deltaw2[k, j] = Math.Min(deltaw2[k, j] * tp.np, tp.deltaMax);
            deltaw = -Math.Sign(dw) * deltaw2[k, j] ;
          } else if (sign < 0) {
            //Console.Out.Write("2-");
            deltaw = -deltaw2[k, j]; // reverse last iteration changes
            deltaw2[k, j] = Math.Max(deltaw2[k, j] * tp.nm, tp.deltaMin);
            dw2[k, j] = 0;
          } else {
            //Console.Out.Write("2=");
            deltaw = -Math.Sign(dw) * deltaw2[k,j];
          }
          w2[k, j] += deltaw;
        }
      }
    }
  }
}