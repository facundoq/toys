using System;
using System.Collections.Generic;
using System.Linq;
using utilities;

namespace mlt.classification{
  public class Dataset{
    public readonly List<Pattern> patterns;

    public Dataset(List<Pattern> patterns)
      : base(){
      this.patterns = patterns;
    }

    public void shuffle(Random random){
      patterns.shuffle(random);
    }

    public int[] getOutputs(){
      return patterns.Select(p => p.y).ToArray();
    }

    public int classes(){
      return getOutputs().Max() + 1;
    }

    public List<List<int>> indexesByClasses(){
      var result = classes().map(i => new List<int>());
      patterns.ForEach((i, p) => result[p.y].Add(i));
      return result;
    }

    public Tuple<Dataset, Dataset> split(double percentSecond){
      return split(percentSecond, null);
    }

    public Tuple<Dataset, Dataset> split(double percentSecond, Random r){
      var first = new List<Pattern>();
      var second = new List<Pattern>();
      foreach (var classIndexes in indexesByClasses()){
        var patternsInClass = classIndexes.Count;
        if (r != null){
          classIndexes.shuffle(r);
        }
        var secondCount = (int) Math.Floor(patternsInClass*percentSecond);
        var firstCount = patternsInClass - secondCount;
        first.AddAll(patterns.getAll(classIndexes.GetRange(0, firstCount)));
        second.AddAll(patterns.getAll(classIndexes.GetRange(firstCount, secondCount)));
      }
      return new Tuple<Dataset, Dataset>(new Dataset(first), new Dataset(second));
    }

    public List<List<double[,]>> byGroupsAndClasses(int groupSize){
      double[,] xs = inputsMatrix();
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

    private double[,] inputsMatrix(){
      var inputSize = patterns[0].x.Length;
      var xs = new double[patterns.Count,inputSize];
      for (var i = 0; i < patterns.Count; i++){
        xs.setRow(i, patterns[i].x);
      }
      return xs;
    }

    public IEnumerable<double[]> getInputs(){
      return patterns.Select(p => p.x);
    }

    public Dataset addBias(){
      var x = this.patterns.Select(p => new Pattern(p.x.insert(1, 0), p.y));
      return new Dataset(x.ToList());
    }

    public double evaluateWith(Classifier classifier){
      int recognized = 0;
      int total = 0;
      foreach (var p in patterns){
        total++;
        var output = classifier.classify(p.x);
        //C.p(p.y + "->"+output.toString());
        if (classifier.correctlyClassified(output, p.y)){
          recognized++;
        }
      }
      return (double) recognized/(double) total;
    }

    //public Dataset[] splitIntoGroups(int groupSize){
    //  int groupsPerPattern = patterns[0].x.Length/groupSize;
    //  List<List<double[]>> patternsPerGroup = patterns.Select(p => 
    //    p.x.splitIntoGroups(groupSize).Select()
    //    ).ToList();

    //  return patternsPerGroup.Select(ps => new Dataset(ps)).ToArray();
    //}
    public List<List<Pattern>> byClasses(){
      return indexesByClasses().Select(
        indexes => indexes.Select(i => patterns[i]).ToList()
        ).ToList();
    }

    public Dataset[] splitIntoGroups(int groupSize){
      var patternLength = patterns[0].x.Length;
      if (patternLength%groupSize != 0){
        throw new Exception("Invalid groupSize, must be divisor of patternLength");
      }
      int groups = patternLength/groupSize;
      var patternsByGroups = patterns.Select(p => p.splitIntoGroups(groupSize));
      return groups.map(g => new Dataset(patternsByGroups.Select(p => p[g]).ToList())).ToArray();
    }

    public IEnumerable<object> csv(){
      var outputs = getOutputs();
      return patterns.Select((p, i) => outputs[i] + "," + outputs[i] +
                                         "," + ",".join(p.x.Select(d => d.ToString().Replace(",", ".")))).ToList();
    }
  }
}