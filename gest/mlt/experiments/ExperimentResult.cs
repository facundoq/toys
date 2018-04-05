using System;
using System.Collections.Generic;
using System.Linq;
using utilities;

namespace mlt.experiments{
  public interface ExperimentResultInformation{
    IDictionary<String, object> getResultVariables();
  }

  public class ExperimentResult<Parameters, Result> where Result : ExperimentResultInformation{
    public ExperimentOptions options;
    public List<SpecResult<Parameters, Result>> results;
    public const string s = ";";

    public ExperimentResult(ExperimentOptions options, List<SpecResult<Parameters, Result>> results){
      this.options = options;
      this.results = results;
    }

    public override string ToString(){
      return report();
    }

    public List<string> variableNames(){
      var names = results[0].spec.fields().Keys.ToList();
      names.Sort();
      return names;
    }

    public string reportHeader(){
      return s.@join(variableNames()) + s + s.@join(resultVariableNames()) + s + "time(s)";
    }

    public string report(){
      var parameters = variableNames();
      var resultVariables = resultVariableNames();
      return reportHeader() + "\n" + "\n".join(
        results.Select(
          r => "\n".join(
            r.results.Select((instance, i) =>
                             variableValues(r.spec.fields(), parameters)
                             + s + variableValues(instance.result.getResultVariables(), resultVariables)
                             + s + instance.time
              ))));
    }

    private List<string> resultVariableNames(){
      var names = results[0].results[0].result.getResultVariables().Keys.ToList();
      names.Sort();
      return names;
    }

    private String variableValues(IDictionary<string, object> values, List<string> names){
      return s.@join(names.Select(name => "" + values[name]));
    }
  }
}