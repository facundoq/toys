using System;
using System.Collections.Generic;
using mlt.classification;
using mlt.data;
using mlt.experiments;
using mlt.mlp;
using mlt.mlp.backpropagation;
using mlt.samples;
using mlt.svm;
using utilities;

namespace mlt.comparison{
  public class FFExperimentParameters : BaseParameters {
    public double n1, n2, u1, u2, error;
    public int hidden, batches;
  }

  public class FFClassificationExperiment : ClassificationExperiment<FFExperimentParameters, GestureProcessingOptions> {
    public FFClassificationExperiment(IList<FFExperimentParameters> specs, ClassificationExperimentOptions options)
      : base(specs, options){
    }

    public FFClassificationExperiment(ClassificationExperimentOptions co) : this(productionSpec(),co){
    }

    public static List<FFExperimentParameters> productionSpec() {
      return new Specs<FFExperimentParameters>()
        .add("n1", 0.1)
        .add("n2", 0.5)
        .add("u1", 0.5)
        .add("u2", 0.1)
        .add("error", 0.05)
        .add("hidden", 10,20,30,40)
        .add("batches", 30)
        .generate();
    }

    public override Classifier experiment(FFExperimentParameters ep, Dataset sample, GestureProcessingOptions options) {
      Difference difference;
      if (options.angles) {
        difference = new AngleDifference();
      } else {
        difference = new CartesianDifference();
      }
      Distance distance = new EuclideanDistance(difference);
      sample = sample.addBias();
      var inputSize = options.n - 1;
      inputSize *= (options.angles) ? 2 : 3;
      inputSize++;
      var tp = new NeuralNetworkTrainerParameters(ep.batches, ep.error, ep.n1, ep.n2, ep.u1, ep.u2,0.5d);

      var p = new NeuralNetworkParameters(inputSize, ep.hidden, sample.classes());
      var trainer = new NeuralNetworkTrainer(tp,p);

      return trainer.train(sample);
    }
  }
}