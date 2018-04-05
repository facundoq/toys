using System;
using System.Collections.Generic;
using utilities;
using System.Linq;

namespace KinectUI.gestures.trainable{
  public class IntervalInformation{
    public int lastClass, lastClassRepetitions;
    public double correctThreshold, incorrectThreshold;
    public int lastClassMinimumRepetitions;

    public IntervalInformation(double correctThreshold, double incorrectThreshold, int lastClassMinimumRepetitions){
      this.correctThreshold = correctThreshold;
      this.incorrectThreshold = incorrectThreshold;
      this.lastClassMinimumRepetitions = lastClassMinimumRepetitions;
    }

    public IntervalInformation(){
      reset();
    }

    private void reset(){
      lastClass = -1;
      lastClassRepetitions = 0;
    }

    public Maybe<int> decide(List<IntervalOutput> intervals){
      return decide(IntervalOutput.combine(intervals));
    }

    private Maybe<int> decide(IntervalOutput interval){
      var o = interval.output;
      var bestClass = o.MaxIndex();
      var p = o[bestClass];
      var belowIncorrectThreshold = o.Count(c => c != p && c < incorrectThreshold);
      if (p > correctThreshold && belowIncorrectThreshold == o.Length - 1){
        if (bestClass == lastClass){
          lastClassRepetitions++;
        }
        else{
          lastClass = bestClass;
          lastClassRepetitions = 0;
        }
      }
      else{
        reset();
      }

      if (lastClassRepetitions >= lastClassMinimumRepetitions){
        reset();
        return Maybe<int>.unit(bestClass);
      }
      else{
        return Maybe<int>.Nothing;
      }
    }
  }

  public class IntervalOutput{
    public float interval;
    public double[] output;

    public IntervalOutput(float interval, double[] output){
      this.interval = interval;
      this.output = output;
    }

    public static IntervalOutput combine(List<IntervalOutput> outputs){
      int classes = outputs[0].output.Length;
      double[] output = new double[classes];
      foreach (var i in outputs){
        output = output.plus(i.output);
      }
      output = output.times(1/((double) outputs.Count));
      return new IntervalOutput(outputs[0].interval, output);
    }
  }

  public class DetectorOutput{
    public List<IntervalOutput> outputs;

    public DetectorOutput(List<IntervalOutput> outputs){
      this.outputs = outputs;
    }
  }

  public class OutputDecider{
    public double correctThreshold, incorrectThreshold;
    public int lastClassMinimumRepetitions;
    public Dictionary<float, IntervalInformation> intervals;

    public OutputDecider(double correctThreshold, double incorrectThreshold, int lastClassMinimumRepetitions){
      this.correctThreshold = correctThreshold;
      this.incorrectThreshold = incorrectThreshold;
      this.lastClassMinimumRepetitions = lastClassMinimumRepetitions;
      intervals = new Dictionary<float, IntervalInformation>();
    }

    public IntervalInformation getIntervalInformation(float interval){
      if (!intervals.ContainsKey(interval)){
        intervals.Add(interval,
                      new IntervalInformation(correctThreshold, incorrectThreshold, lastClassMinimumRepetitions));
      }
      return intervals[interval];
    }

    public Maybe<int> decide(List<DetectorOutput> detectorOutputs){
      var results = getIntervalInputs(detectorOutputs)
        .Select(i => getIntervalInformation(i.Key).decide(i.Value));
      return results.Where(r => r.isResult()).DefaultIfEmpty(Maybe<int>.Nothing).First();
    }

    private MultivalueDictionary<float, IntervalOutput> getIntervalInputs(List<DetectorOutput> detectorOutputs){
      var result = new MultivalueDictionary<float, IntervalOutput>();
      foreach (var d in detectorOutputs){
        foreach (var i in d.outputs){
          result.Add(i.interval, i);
        }
      }
      return result;
    }
  }
}