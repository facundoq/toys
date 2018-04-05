using System.Collections.Generic;
using mlt.experiments;

namespace mlt.classification{
  
  public class ClassificationExperimentRunner<DatasetGeneratorOptions> where DatasetGeneratorOptions : Report,ID{
    private readonly bool consoleOutput;
    private readonly bool fileOutput;
    public ExperimentRunnerOutputManager output;
    
    public ClassificationExperimentRunner(bool consoleOutput, bool fileOutput){
      this.consoleOutput = consoleOutput;
      this.fileOutput = fileOutput;
    }

    public void run(DatasetGenerator<DatasetGeneratorOptions> datasetGenerator,
                    List<IClassificationExperiment<DatasetGeneratorOptions>> experiments){
      output = new ExperimentRunnerOutputManager(consoleOutput, fileOutput);
      int dataset = 0;
      foreach (GeneratedDataset<DatasetGeneratorOptions> g in datasetGenerator){
        dataset++;
        output.p("\n\ndataset " + dataset + ": " + g.options.report());
        foreach (var e in experiments){
            output.p("\nRunning experiment " + e.GetType().Name);
            e.run(g.dataset, g.options, output);
        }
      }
      output.close();
    }    
  }

  }