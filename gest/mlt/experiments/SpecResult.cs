using System.Collections.Generic;

namespace mlt.experiments{
  public struct RepetitionResult<Result>{
    public Result result;
    public long time;

    public RepetitionResult(Result result, long time)
      : this(){
      this.result = result;
      this.time = time;
    }
  }

  public struct SpecResult<Parameters, Result>{
    public Parameters spec;
    public List<RepetitionResult<Result>> results;

    public SpecResult(Parameters spec, List<RepetitionResult<Result>> results){
      this.spec = spec;
      this.results = results;
    }
  }
}