using System;
using System.Collections.Generic;
using mlt.classification;
using mlt.data;
using mlt.experiments;
using mlt.mlp;
using mlt.mlp.rprop;
using mlt.samples;
using mlt.svm;
using utilities;

namespace mlt.comparison{
  public class IRpropPExperimentParameters : BaseParameters {
    public double np, nm, delta0, deltaMax,deltaMin, error;
    public int hidden, batches;
  }

  public class IRpropPClassificationExperiment : ClassificationExperiment<IRpropPExperimentParameters, GestureProcessingOptions> {
    public IRpropPClassificationExperiment(IList<IRpropPExperimentParameters> specs, ClassificationExperimentOptions options)
      : base(specs, options){
    }

    public IRpropPClassificationExperiment(ClassificationExperimentOptions co) : this(productionSpec(),co){
    }

    public static List<IRpropPExperimentParameters> productionSpec() {
      return new Specs<IRpropPExperimentParameters>()
        .add("np", 1.05)
        .add("nm", 0.9)
        .add("delta0", 0.5)
        .add("deltaMax", 2)
        .add("deltaMin", 0.01)
        .add("error", 0.05)
        .add("hidden", 12)
        .add("batches", 100)
        .generate();
    }

    public override Classifier experiment(IRpropPExperimentParameters ep, Dataset sample, GestureProcessingOptions options) {
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
      var tp = new IRpropPParameters(ep.batches, ep.error, 
        ep.nm, ep.np, ep.delta0, ep.deltaMin,ep.deltaMax,0.5d);

      var p = new NeuralNetworkParameters(inputSize, ep.hidden, sample.classes());
      var trainer = new IRpropPTrainer(tp,p);

      return trainer.train(sample);
    }
  }
}