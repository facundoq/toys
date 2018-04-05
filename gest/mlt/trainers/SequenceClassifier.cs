using System;
using System.Collections.Generic;
using mlt.classification;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.trainers {
  
  public class SequenceClassifierParameters{
    public int groupSize;
    public DecisionIntegrator decision;
    public ErrorEstimation errorEstimation;

    public SequenceClassifierParameters(int groupSize, DecisionIntegrator decision, ErrorEstimation errorEstimation) {
      this.groupSize = groupSize;
      this.decision = decision;
      this.errorEstimation = errorEstimation;
    }
  }




  public class SequenceClassifier<TClassifier> 
    : Classifier
    where TClassifier: Classifier{
    
    public TClassifier[] classifiers;
    public SequenceClassifierParameters p;

    public SequenceClassifier(TClassifier[] classifiers, SequenceClassifierParameters p){
      this.classifiers = classifiers;
      this.p = p;
    }

    public double[] classify(double[] input){
      var inputs = input.splitIntoGroups(p.groupSize);
      var outputs= inputs.Select((v, i) => classifiers[i].classify(v));
      return p.decision.classify(outputs.ToList());
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass){
      return p.errorEstimation.correctlyClassified(actualOutput, expectedClass);
    }
  }

  public class SequenceClassifierTrainer<TClassifier>
    : Trainer<SequenceClassifier<TClassifier>>
  where TClassifier: Classifier{
    
    public Trainer<TClassifier> trainer;
    public SequenceClassifierParameters p;

    public SequenceClassifier<TClassifier> train(Dataset set) {
       Dataset[] sets = set.splitIntoGroups(p.groupSize);
      var classifiers = sets.Select(s => trainer.train(s)).ToArray();
      return new SequenceClassifier<TClassifier>(classifiers,p);
    }
  }
}
