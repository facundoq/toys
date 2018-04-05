using System;
using System.Collections.Generic;
using utilities;

namespace mlt.classification{
  public class PatternSets{
    public PatternSet training, test, validation;

    public PatternSets(PatternSet training, PatternSet test, PatternSet validation){
      this.training = training;
      this.test = test;
      this.validation = validation;
    }

    public PatternSets(PatternSet set, double percentTest) : this(set, 0, percentTest){
    }

    public PatternSets(PatternSet set, double percentValidation, double percentTest){
      int patterns = set.xs.rowCount();
      int v = (int) Math.Floor(percentValidation*patterns);
      int t = (int) Math.Floor(percentTest*patterns);
      int tr = patterns - (v + t);

      List<List<int>> classes = set.indexesByClasses();
      var turn = 0;
      var sets = 3.map(i => new List<int>()).ToArray();
      var targets = new int[]{tr, v, t};
      while (!classes.empty()){
        var s = sets[turn];
        var target = targets[turn];
        if (s.Count < target){
          foreach (var c in classes){
            s.Add(c[0]);
            c.RemoveAt(0);
          }
        }
        turn = (turn + 1)%sets.Length;
        classes.RemoveAll(c => c.Count == 0);
      }
      training = set.subset(sets[0]);
      validation = set.subset(sets[1]);
      test = set.subset(sets[2]);
    }

    public int totalPatterns(){
      return training.xs.rowCount() + test.xs.rowCount() + validation.xs.rowCount();
    }

    public double[] means(){
      double[] s = training.xs.sumOfColumns().plus(test.xs.sumOfColumns()).plus(validation.xs.sumOfColumns());
      return s.times((double) 1/totalPatterns());
    }

    public double[] sd(){
      double[] means = this.means();
      var result = training.xs.se(means).plus(test.xs.se(means).plus(validation.xs.se(means)));
      result = result.times((double) 1/(totalPatterns() - 1));
      return result;
    }

    public void normalizeColumns(){
      substractMeans();
      divideBySd();
    }

    public void normalizeColumns(double[] means, double[] sd){
      substractMeans(means);
      divideBySd(sd);
    }

    public void divideBySd(){
      divideBySd(sd());
    }

    public void divideBySd(double[] sd){
      training.xs.divideByColumns(sd);
      test.xs.divideByColumns(sd);
      validation.xs.divideByColumns(sd);
    }

    public void substractMeans(){
      substractMeans(means());
    }

    public void substractMeans(double[] means){
      training.xs.minusColumns(means);
      test.xs.minusColumns(means);
      validation.xs.minusColumns(means);
    }
  }
}