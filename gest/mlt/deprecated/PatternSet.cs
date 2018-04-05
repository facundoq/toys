using System.Collections.Generic;
using System.Linq;
using mlt.mlp;
using utilities;

namespace mlt.classification{
  public class PatternSet{
    public double[,] xs;
    public double[,] ys;
    public double correctLabel, incorrectLabel;

    public List<Pattern> patterns(){
      var outputs = getOutputs();
      return xs.rows().Select((x, i) => new Pattern(x, outputs[i])).ToList();
    }

    public PatternSet(double[,] xs, double[,] ys, double correct, double incorrect){
      this.xs = xs;
      this.ys = ys;
      this.correctLabel = correct;
      this.incorrectLabel = incorrect;
    }

    public PatternSet(List<List<double[]>> inputClasses, double correct, double incorrect){
      var totalInputs = inputClasses.Aggregate(0, (s, g) => s + g.Count);
      var inputSize = inputClasses[0][0].Count();
      xs = new double[totalInputs,inputSize];
      ys = new double[totalInputs,inputClasses.Count];
      int row = 0;
      for (int g = 0; g < inputClasses.Count; g++){
        var inputClass = inputClasses[g];
        for (int i = 0; i < inputClass.Count; i++){
          //generate input vector
          var input = inputClass[i];
          for (int j = 0; j < inputSize; j++){
            xs[row, j] = input[j];
          }
          //generate output vector
          for (int j = 0; j < inputClasses.Count; j++){
            ys[row, j] = (g == j) ? correct : incorrect;
          }
          row++;
        }
      }
      this.correctLabel = correct;
      this.incorrectLabel = incorrect;
    }

    protected PatternSet(){
    }

    public List<string> csv(List<string> names){
      var outputs = getOutputs();
      return patterns().Select((p, i) => names[outputs[i]] + "," + outputs[i] +
                                         "," + ",".join(p.x.Select(d => d.ToString().Replace(",", ".")))).ToList();
    }

    public PatternSet subset(List<int> rows){
      return new PatternSet(xs.rows(rows), ys.rows(rows), correctLabel, incorrectLabel);
    }

    public PatternSet subset(int from, int to){
      return new PatternSet(xs.subMatrix(from, to), ys.subMatrix(from, to), correctLabel, incorrectLabel);
    }

    public int[] getOutputs(){
      return ys.rows().Select(y => y.indexOf(correctLabel)).ToArray();
    }

    public List<double[]> getInputs(){
      return patterns().Select(p => p.x).ToList();
    }

    public List<List<double[,]>> byGroupsAndClasses(int groupSize){
      int groups = xs.columnCount()/groupSize;
      var result = new List<List<double[,]>>();
      var outputs = getOutputs();
      int classes = outputs.Max() + 1;
      for (int i = 0; i < groups; i++){
        var group = new List<double[,]>();
        var groupData = xs.subColumns(i*groupSize, (i + 1)*groupSize);
        for (int j = 0; j < classes; j++){
          group.add(groupData.rows(outputs.indexesOf(j)));
        }
        result.Add(group);
      }
      return result;
    }

    public List<List<int>> indexesByClasses(){
      var result = classes().map(i => new List<int>());
      getOutputs().ForEach((i, o) => result[o].Add(i));
      return result;
    }

    public int classes(){
      return getOutputs().Max() + 1;
    }
  }
}