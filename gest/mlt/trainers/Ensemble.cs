using System;
using System.Collections.Generic;
using mlt.classification;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.trainers {
  
  public interface DecisionIntegrator{
    double[] classify(List<double[]> outputs);
   }

  public interface ErrorEstimation {
    bool correctlyClassified(double[] actualOutput, int expectedClass);
  }


  public class AverageDecisionIntegrator : DecisionIntegrator {
    public double[] classify(List<double[]> outputs) {
      double[] result = new double[outputs[0].Length];
      for (int klass = 0; klass < result.Length; klass++) {
        for (int classifier = 0; classifier < outputs.Count; classifier++) {
          result[klass] += outputs[classifier][klass];
        }
        result[klass] /= outputs.Count;
      }
      return result;
    }

   }

  public class MaxErrorEstimation{
    public bool correctlyClassified(double[] actualOutput, int expectedClass) {
      return actualOutput.greaterThanAll(expectedClass);
    }  
  }

  public class MaxWithThresholdErrorEstimation{
    public double threshold;

    public MaxWithThresholdErrorEstimation(double threshold){
      this.threshold = threshold;
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass) {
      return actualOutput.greaterThanAll(expectedClass) & actualOutput[expectedClass]>threshold;
    }
  }


  public class MaxDecisionIntegrator : DecisionIntegrator{
    public double[] classify(List<double[]> outputs){
      double[] result = new double[outputs[0].Length];
      for (int klass = 0; klass < result.Length; klass++){
        for (int classifier=0; classifier< outputs.Count;classifier++){
          result[klass] = Math.Max(result[klass], outputs[classifier][klass]);
        }
      }
      return result;
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass){
      return actualOutput.greaterThanAll(expectedClass);
    }
  }


  public class Ensemble<T> : Classifier where T:Classifier{
    public DecisionIntegrator integrator;
    public List<T> classifiers;
    public ErrorEstimation estimation;

    public Ensemble(DecisionIntegrator integrator, List<T> classifiers, ErrorEstimation estimation){
      this.integrator = integrator;
      this.classifiers = classifiers;
      this.estimation = estimation;
    }

    public double[] classify(double[] input){
      return integrator.classify(classifiers.Select(c => c.classify(input)).ToList());
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass){
      return estimation.correctlyClassified(actualOutput,expectedClass);
    }
  }

  public class EnsembleTrainer<TCLassifier>
    : Trainer<Ensemble<TCLassifier>> 
    where TCLassifier:Classifier{

    public List<Trainer<TCLassifier>> trainers;
    public DecisionIntegrator integrator;
    public ErrorEstimation estimation;

    public EnsembleTrainer(List<Trainer<TCLassifier>> trainers, DecisionIntegrator integrator, ErrorEstimation estimation){
      this.trainers = trainers;
      this.integrator = integrator;
      this.estimation = estimation;
    }

    public Ensemble<TCLassifier> train(Dataset set){
      List<TCLassifier> classifiers= trainers.Select(t => t.train(set)).ToList() ;
      return new Ensemble<TCLassifier>(integrator,classifiers,estimation);
    }
  }
}
