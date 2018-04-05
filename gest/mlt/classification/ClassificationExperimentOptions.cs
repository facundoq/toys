using System;
using System.Collections.Generic;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.classification{
  public class ClassificationExperimentOptions{
    public readonly List<double> percentTest;
    
    public readonly int repetitions;

    public ClassificationExperimentOptions(double percentTest, int repetitions) {
      this.percentTest = percentTest.inList();
      this.repetitions = repetitions;
    }
    public ClassificationExperimentOptions(List<double> percentTest, int repetitions){
      this.percentTest = percentTest;
      this.repetitions = repetitions;
    }
  }
}