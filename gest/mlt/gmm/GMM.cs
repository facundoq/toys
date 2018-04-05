using System;
using System.Collections.Generic;
using mlt.classification;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.gmm{
  public class DecisionUnit{
    public double calculate(double[] probabilitiesPerGroup){
      return probabilitiesPerGroup.mean();
    }
  }

  public class DistanceUnit{
    public class Detector{
      public double[] center;
      public double spread;
      public Distance distance;

      public Detector(double[] center, double spread, Distance distance) {
        this.center = center;
        this.spread = spread;
        this.distance = distance;
      }

      public double detect(double[] input){
        return Math.Exp(- spread* distance.between(center,input));
      }
    }

    public List<List<Detector>> detectorsPerClass;

    public DistanceUnit(){
      detectorsPerClass = new List<List<Detector>>();
    }

    public void addClass(){
      addClass(new List<Detector>());
    }

    public void addClass(List<Detector> detectors){
      detectorsPerClass.Add(detectors);
    }

    public List<double> calculate(double[] input){
      return detectorsPerClass.Select(
        klass => klass.Select(h => h.detect(input))
                      .Max()).ToList();
    }
  }

  public class GMM:Classifier{
    public List<DistanceUnit> groups;
    public List<DecisionUnit> decision;
    private GMMParameters p;

    public GMM(List<DistanceUnit> groups, List<DecisionUnit> decision, GMMParameters p){
      this.groups = groups;
      this.decision = decision;
      this.p = p;
    }

    public double[] classify(double[] input){
      List<List<double>> probabilitiesPerGroup = input.splitIntoGroups(p.groupSize).Select(
        (g, i) => groups[i].calculate(g)
        ).ToList();
      List<double[]> probabilitiesPerClass =
        decision.Count.map(i => probabilitiesPerGroupForClass(probabilitiesPerGroup, i));
      return decision.Select((d, i) => d.calculate(probabilitiesPerClass[i])).ToArray();
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass){
     // return actualOutput[expectedClass] > 0.7 && actualOutput.Where(v => v > 0.7).Count() == 1;
      return actualOutput.greaterThanAll(expectedClass);
    }

    public double[] probabilitiesPerGroupForClass(List<List<double>> probabilitiesPerGroup, int c){
      return probabilitiesPerGroup.Select(g => g[c]).ToArray();
    }
  }
}