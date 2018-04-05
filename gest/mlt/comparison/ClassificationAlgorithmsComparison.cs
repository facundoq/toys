using System;
using System.Collections.Generic;
using mlt.classification;
using mlt.data;
using mlt.experiments;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.comparison {
  public class ClassificationAlgorithmsComparison {

    public void testRotation(){
      var a = new Position3(3, 4, 5).normalize();
      var b = new Position3(1, 1, 1).normalize();
      var R = a.rotationMatrixTo(b);
      var c = a.rotateBy(R);
      C.p("a:" + a);
      C.p("b:" + b);
      //var cross = a.array().cross(b.array());
      //C.p("cross:"+cross.toString());
      //C.p("cross x a:"+cross.dot(b.array()));
      //C.p("cross x b:" + cross.dot(a.array()));
      C.p("c:" + c);
      C.p("R:" + R.toString());


    }
      
    public void run(){
      var basePath="D:\\Dropbox\\Tesina de Facundo Quiroga\\gestures\\samples\\";
      //var path = basePath+"numbers.csv";
     //var path = basePath+"visap-half.csv";
      //var path = basePath + "letters.csv";
      //var path = basePath+"letters_numbers.csv";
      var path = basePath + "letters_numbers_numbers2.csv";
      //var path = basePath + "numbers2.csv";
      //var path = basePath + "rot_letters.csv";
      //var path = basePath + "rot_numbers.csv";
      //var path = basePath + "rot_numbers2.csv";
      //var path = basePath + "rot_letters_numbers_numbers2.csv";
      var runner = new ClassificationExperimentRunner<GestureProcessingOptions>(true, false);
      string id = path.Split('\\').last().Split('.').first();
      var pos = new GestureProcessingOptions[]{
       new GestureProcessingOptions(false,16,true,2,false,id),
       //new GestureProcessingOptions(false,32,true,2,false,id),
       //new GestureProcessingOptions(false,64,true,2,false,id), 
      // new GestureProcessingOptions(false,128,true,2,false,id),
      };
      var percentTests = new []{0.1,0.3,0.5,0.7,0.85}.ToList();
      
      var dg = new GestureFileDatasetGenerator(pos.ToList(),path);
      var singleCO = new ClassificationExperimentOptions(percentTests, 1);
      var co = new ClassificationExperimentOptions(percentTests, 3);
      var threePerClass = new ClassificationExperimentOptions(0.85, 3);
      var svm = new SVMClassificationExperiment(co);
      var gmm = new GMMClassificationExperiment(singleCO);
      var irpropp = new IRpropPClassificationExperiment(co);
      var cnc = new CNCClassificationExperiment(threePerClass);
      //var ff = new FFClassificationExperiment(new ClassificationExperimentOptions(.5, 1));
      var ff = new FFClassificationExperiment(co);
      var experiments =
      new List<IClassificationExperiment<GestureProcessingOptions>> {cnc};
      runner.run(dg,experiments);
    }

  }
}
