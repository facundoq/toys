using System;
using System.Collections.Generic;
using utilities;

namespace mlt.mlp.backpropagation{
  public class TrainingState{
    public class ImprovementMonitor{
      public int maximumBatchesWithoutImprovement;
      public readonly double improvementThreshold;
      public List<double> errors;

      public ImprovementMonitor(int maximumBatchesWithoutImprovement, double improvementThreshold){
        this.maximumBatchesWithoutImprovement = maximumBatchesWithoutImprovement;
        this.improvementThreshold = improvementThreshold;
        errors = new List<double>();
      }

      public void update(double error){
        errors.Add(error);
      }

      public Boolean tooLittleImprovement(){
        return errors.Count > maximumBatchesWithoutImprovement &&
               improvement(maximumBatchesWithoutImprovement) < improvementThreshold;
      }

      public override string ToString(){
        return " Last Error:  " + lastError().ToString("0.0000000") +
               " - Improvement in the last " + maximumBatchesWithoutImprovement
               + " batches :" + improvement(maximumBatchesWithoutImprovement);
      }

      public double improvement(int batches){
        var beginning = Math.Max(errors.Count - batches, 0);
        return (errors.hasElements()) ? errors[beginning] - errors.last() : 0;
      }

      public double lastError(){
        return (errors.hasElements()) ? errors.last() : Double.MaxValue;
      }
    }

    public int batches;
    public int samplesPerBatch;
    public int maximumBatches;
    private readonly double errorThreshold;
    public ImprovementMonitor validation;
    public ImprovementMonitor training;

    public TrainingState(int samplesPerBatch, int maximumBatches, double errorThreshold){
      this.samplesPerBatch = samplesPerBatch;
      this.maximumBatches = maximumBatches;
      this.errorThreshold = errorThreshold;
      validation = new ImprovementMonitor(1000, 0.01);
      training = new ImprovementMonitor(1000, 0.001);
    }

    private double totalSamples(){
      return (batches*samplesPerBatch);
    }

    public override string ToString(){
      return " Epochs: " + batches + "/" + maximumBatches +
             " - Validation: " + validation;
    }

    public void update(double validationError, double trainingError){
      batches++;
      validation.update(validationError);
      training.update(trainingError);
    }

    public bool finished(){
      return batches >= maximumBatches
             || validation.lastError() < errorThreshold
             || validation.tooLittleImprovement();
    }
  }
}