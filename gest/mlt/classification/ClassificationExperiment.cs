using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.IO;
using System.Linq;
using KinectUI.utility;
using System.Text;
using mlt.classification;
using mlt.mlp;
using utilities;

namespace mlt.experiments{

  public interface IClassificationExperiment< DatasetGeneratorOptions>
  where DatasetGeneratorOptions : Report,ID{
    void run(Dataset dataset, DatasetGeneratorOptions datasetGeneratorOptions, ExperimentRunnerOutputManager outputManager) ;
  }
  public interface ID{
    string id();
  }
  public class BaseParameters : Report{
    public String report(){
      return this.fields().toString();
    }

    public string csv(){
      return ",".join(this.fieldsValues());
    }
    public string header() {
      return ",".join(this.fieldsNames());
    }
  }

  public abstract class ClassificationExperiment<TParameters, DatasetGeneratorOptions> :
    IClassificationExperiment<DatasetGeneratorOptions>
    where TParameters : Report 
    where DatasetGeneratorOptions : Report,ID{
    
    public readonly IList<TParameters> experimentSpecs;
    public readonly ClassificationExperimentOptions experimentOptions;

    protected ClassificationExperiment(IList< TParameters> specs, ClassificationExperimentOptions options){
      this.experimentSpecs = specs;
      this.experimentOptions = options;
    }

    public abstract Classifier experiment(TParameters ep, Dataset sample,
                                          DatasetGeneratorOptions datasetGeneratorOptions);

    public Boolean filter(TParameters spec, Dataset sample, DatasetGeneratorOptions datasetGeneratorOptions){
      return true;
    }

    public void run(Dataset dataset, DatasetGeneratorOptions datasetGeneratorOptions, ExperimentRunnerOutputManager outputManager){
      int specIndex = 0;
      var random = new Random(100);
      var output = outputManager.getExperimentOutput(this, datasetGeneratorOptions.id());
      foreach (TParameters spec in experimentSpecs){
        specIndex++;
        if (filter(spec, dataset, datasetGeneratorOptions)){
          int repetitions = experimentOptions.repetitions;
          foreach (var percentTest in experimentOptions.percentTest){
            var training = new MeanAndVarianceCalculator();
            var test = new MeanAndVarianceCalculator();
            for (int repetition = 0; repetition < repetitions; repetition++){
              dataset.shuffle(random);
              var split = dataset.split(percentTest);
              Classifier classifier = experiment(spec, split.Item1, datasetGeneratorOptions);
              training.update(split.Item1.evaluateWith(classifier)*100);
              test.update(split.Item2.evaluateWith(classifier) * 100);
            }
            output.print(datasetGeneratorOptions, spec, training, test, specIndex, experimentSpecs.Cast<Report>().ToList(), repetitions, percentTest);
          }
          
        }
        else{
          output.p("" + specIndex + "/" + experimentSpecs.Count + " - ignoring ep:" + spec.fields().toString());
        }
      }
    }
  }
}