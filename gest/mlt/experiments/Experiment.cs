using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using KinectUI.utility;
using System.Linq;
using System.Text;
using utilities;

namespace mlt.experiments{
  public abstract class Experiment<Parameters, Result> 
    where Result : ExperimentResultInformation{
    
    public IList<Parameters> specs;
    public ExperimentOptions options;

    protected Experiment(IList<Parameters> specs, ExperimentOptions options){
      this.specs = specs;
      this.options = options;
    }

    public int numberOfExperiments(){
      return specs.Count*options.repetitions;
    }

    public ExperimentResult<Parameters, Result> run(){
      int specIndex = 0;
      var results = new List<SpecResult<Parameters, Result>>();
      foreach (Parameters spec in specs){
        specIndex++;
        if (filter(spec)){
          results.add(experiment(spec, options, specIndex));
        }
        else{
          Console.WriteLine("" + specIndex + "/" + specs.Count + " - ignoring spec:" + spec.fields().toString());
        }
      }
      return new ExperimentResult<Parameters, Result>(options, results);
    }

    private SpecResult<Parameters, Result> experiment(Parameters spec, ExperimentOptions options, int specIndex){
      Console.WriteLine("" + specIndex + "/" + specs.Count + " spec: " + spec.fields().toString());
      return new SpecResult<Parameters, Result>(spec,
                                                options.repetitions.asRange().Select(i =>{
                                                  long time = DateTime.Now.Ticks;
                                                  Console.Write((i + 1) + "/" + options.repetitions);
                                                  var r = experiment(spec);
                                                  time = (DateTime.Now.Ticks - time)/10000000;
                                                  Console.WriteLine(" -> time " + time + " result: " +
                                                                    r.getResultVariables().toString());
                                                  return new RepetitionResult<Result>(r, time);
                                                }).ToList());
    }

    public Boolean filter(Parameters spec){
      return true;
    }

    public abstract Result experiment(Parameters spec);
  }
}