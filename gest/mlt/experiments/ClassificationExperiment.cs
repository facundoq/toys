using System;
using System.Collections.Generic;
using mlt.classification;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.experiments {

  public class ClassificationExperimentResult{

  }

  public class ClassificationExperiment<Parameters, Samples, Result>{

      public IList<Parameters> specs;
      public ExperimentOptions options;

      protected Experiment(IList<Parameters> specs, ExperimentOptions options) {
        this.specs = specs;
        this.options = options;
      }

      public ExperimentResult<Parameters, Result> run(PatternSets sets) {
        int specIndex = 0;
        var results = new List<SpecResult<Parameters, Result>>();
        foreach (Parameters spec in specs) {
          specIndex++;
          if (filter(spec)) {
            results.add(experiment(spec, options, specIndex));
          } else {
            Console.WriteLine("" + specIndex + "/" + specs.Count + " - ignoring spec:" + spec.fields().toString());
          }
        }
        return new ExperimentResult<Parameters, Result>(options, results);
      }

    public abstract Result experiment(Parameters p,Samples s);
    }
  }
}
