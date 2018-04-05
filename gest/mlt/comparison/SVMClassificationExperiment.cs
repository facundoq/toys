using System;
using System.Collections.Generic;
using ILNumerics;
using mlt.classification;
using mlt.data;
using mlt.experiments;
using mlt.samples;
using mlt.svm;
using utilities;

namespace mlt.comparison{
  public class Parameters : BaseParameters {
    public double c,tolerance,sd;
    public int batches;
  }

  public class SVMClassificationExperiment : ClassificationExperiment<Parameters, GestureProcessingOptions> {
    public SVMClassificationExperiment(IList<Parameters> specs, ClassificationExperimentOptions options)
      : base(specs, options){
    }

    public SVMClassificationExperiment(ClassificationExperimentOptions co) : this(productionSpec(),co){
    }

    public static List<Parameters> productionSpec(){
      return new Specs<Parameters>()
        .add("tolerance", 0.01)
        .add("c", 10,50,100,200)
        .add("batches", 10000)
        .add("sd", 32)//,64,128,256)
        .generate();
    }

    public override Classifier experiment(Parameters ep,Dataset sample,GestureProcessingOptions options){
      var rp = new SVMParameters();
      var tp = new SVMTrainingOptions(ep.c,0.001,ep.tolerance,ep.batches);

      Difference difference;
      if (options.angles) {
        difference = new AngleDifference();
      } else {
        difference = new CartesianDifference();
      }

      Distance distance = new EuclideanDistance(difference);
      //Kernel k = new GaussianDistanceKernel(ep.sd, distance);
      Kernel k = new LinearKernel();
      var trainer = new MulticlassSVMTrainer(tp,k);
      var svm = trainer.train(sample);
      return svm;
    }
  }
}