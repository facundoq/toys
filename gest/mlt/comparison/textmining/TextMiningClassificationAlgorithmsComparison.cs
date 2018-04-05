using System;
using System.Collections.Generic;
using mlt.classification;
using mlt.comparison.textmining;
using mlt.data;
using mlt.experiments;
using mlt.svm;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.comparison {
  public class TextMiningClassificationAlgorithmsComparison {


    public void run(){
      var path = "D:\\files\\text.csv";
      //var path = "D:\\Dropbox\\Tesina de Facundo Quiroga\\gestures\\samples\\article\\visap-half.csv";
      var runner = new ClassificationExperimentRunner<KeywordProcessingOptions>(true,true);

      var pos = new []{
        new KeywordProcessingOptions(), 
      };
      var dg = new KeywordsFileDatasetGenerator(pos.ToList(), path);
      var co = new ClassificationExperimentOptions(.8, 1);
      var spec = KeywordSVMClassificationExperiment.productionSpec();
      var svm = new KeywordSVMClassificationExperiment(spec, co);
      var gmm = new GMMClassificationExperiment(co);
      var ff = new FFClassificationExperiment(new ClassificationExperimentOptions(.5, 1));
      var experiments =
      new List<IClassificationExperiment<KeywordProcessingOptions>> {svm};
      runner.run(dg,experiments);

    }


    public class KeywordSVMClassificationExperiment : ClassificationExperiment<Parameters, KeywordProcessingOptions> {
      
      public static List<Parameters> productionSpec(){
      return new Specs<Parameters>()
        .add("tolerance", 0.01)
        .add("c", 20,50,100,200)
        .add("batches", 10000)
        .add("sd", 1,2,4,8,16,64)
        .generate();
    }
      public KeywordSVMClassificationExperiment(IList < Parameters > specs, ClassificationExperimentOptions
      options):base(specs, options)
      {
      }
      public override Classifier experiment(Parameters ep, Dataset sample, KeywordProcessingOptions options) {
        C.p("starting");
        var rp = new SVMParameters();
        var tp = new SVMTrainingOptions(ep.c, 0.001, ep.tolerance, ep.batches);

        Difference difference=new CartesianDifference();
        Distance distance = new EuclideanDistance(difference);
        //var trainer = new MulticlassSVMTrainer(tp, new GaussianDistanceKernel(ep.sd, distance));
        var trainer = new MulticlassSVMTrainer(tp, new LinearKernel());
        var svm = trainer.train(sample);
        return svm;
      }
    }

  }
}
