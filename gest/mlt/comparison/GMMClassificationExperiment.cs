using System;
using System.Collections.Generic;
using mlt.classification;
using mlt.data;
using mlt.experiments;
using mlt.gmm;
using mlt.samples;
using mlt.svm;
using utilities;

namespace mlt.comparison {
  public class GMMClassificationExperimentParameters : BaseParameters{
    public double spread;
    public int groupSize;

  }
  public class GMMClassificationExperiment : ClassificationExperiment<GMMClassificationExperimentParameters, GestureProcessingOptions> {

    public GMMClassificationExperiment(IList<GMMClassificationExperimentParameters> specs, ClassificationExperimentOptions options)
      : base(specs, options) {
    }

    public GMMClassificationExperiment(ClassificationExperimentOptions co)
      : this(productionSpec(), co) {
    }


    public static List<GMMClassificationExperimentParameters> productionSpec() {
      return new Specs<GMMClassificationExperimentParameters>()
      .add("spread",1,2,4,16,32,64,128)
      .add("groupSize",0)
        .generate();
    }

    public override Classifier experiment(GMMClassificationExperimentParameters ep,
      Dataset sample, GestureProcessingOptions options){
      int groupSize = (options.angles) ? 2 : 3;
      if (ep.groupSize==0){
        groupSize = options.n;
      }
      Difference difference;
      if (options.angles){
        difference=new AngleDifference();
      }else{
        difference=new CartesianDifference();
      }
      //difference = (options.angles) ? (new AngleDifference()) : (new CartesianDifference());
      Distance distance = new EuclideanDistance(difference);  
      var rp = new GMMParameters(groupSize ,ep.spread,distance);
      var trainer = new GMMTrainer(rp);
      return trainer.train(sample);
    }
  }
}