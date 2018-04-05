using System;
using System.Collections.Generic;
using mlt.experiments;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using mlt.classification;

namespace mlt.gmm{
  public class GMMTrainerParameters{
  }

  public class GMMTrainer : Trainer<GMM> {
    public GMMParameters p;

    public GMMTrainer(GMMParameters p){
      this.p = p;
    }

    public GMM train(Dataset training){
      int[] outputs = training.getOutputs();
      List<List<double[,]>> xs = training.byGroupsAndClasses(p.groupSize);
      var x = training.patterns;
      int classes = outputs.Max() + 1;
      // convert each class into a distance unit
      List<DistanceUnit> distanceUnits = new List<DistanceUnit>();
      for (int i = 0; i < xs.Count; i++){
        var d = new DistanceUnit();
        var group = xs[i];
        for (int j = 0; j < group.Count; j++){
          var klass = group[j];
          var meanSd = klass.sds().mean();
          d.addClass(klass.rows().Select(r => new DistanceUnit.Detector(r, meanSd*p.spread,p.distance)).ToList());
        }
        distanceUnits.add(d);
      }
      return new GMM(distanceUnits, new DecisionUnit().inList(classes), p);
      
    }
  }
}