using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using mlt.experiments;

namespace mlt.classification{
  public interface Trainer<TClassifier> where TClassifier : Classifier{
    TClassifier train(Dataset set);
  }

  public interface Classifier{
    /* given an input pattern, returns the probability [0..1]
         * that it belongs to each class 
         */
    double[] classify(double[] input);
    bool correctlyClassified(double[] actualOutput, int expectedClass);
  }
}