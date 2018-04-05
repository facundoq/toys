using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using mlt.classification;
using mlt.experiments;
using utilities;

namespace mlt.svm{

  public class MulticlassSVMTrainer : Trainer<MulticlassSVM> {
    private readonly SVMTrainingOptions o;
    private readonly Kernel kernel;

    public MulticlassSVMTrainer(SVMTrainingOptions o, Kernel kernel){
      this.o = o;
      this.kernel = kernel;
    }

    public MulticlassSVM train(Dataset training){
       var svms=training.classes().map(i => new SVMTrainer(o, training, kernel, i).train());
      return new MulticlassSVM(svms.ToList());
    }

  }

  public class MulticlassSVM:Classifier{
    public readonly List<SVM> svms;

    public MulticlassSVM(List<SVM> svms){
      this.svms = svms;
    }

    public double[] classify(double[] input){
      return svms.Select(svm => svm.classify(input)[0]).ToArray();
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass){
      return actualOutput[expectedClass] > 0 && actualOutput.Where(v=> v>0).Count()==1;
    }
  }
}