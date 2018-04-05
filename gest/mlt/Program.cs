using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Kinect;
using SampleLibrary;
using mlt.classification;
using mlt.comparison;
using mlt.data;
using mlt.experiments;
using mlt.gmm;
using mlt.mlp;
using mlt.samples;
using mlt.svm;
using utilities;
using mlt.cpn.cncoriginal;

namespace mlt{
  internal class Program{
    public static void exportGestures(){
      var gestures = Samples.librarySample(new PreprocessingParameters(30, 0), 300, 300);
      var basePath="D:\\Dropbox\\Tesina de Facundo Quiroga\\gestures\\samples\\";
      //var path = basePath + "letters_numbers_numbers2.csv";
      var path = basePath + "letters_numbers_numbers2.csv";
       
      var po=new GestureProcessingOptions(false, 16, true, 2, false, "lnhg");
      var dg = new GestureFileDatasetGenerator(po.inList(), path);
      var ds=dg.First().dataset;
      var data = "\n".join(ds.csv());
       System.IO.File.WriteAllText("lnhg_normalized.csv", data);
      //System.IO.File.WriteAllText("rot_numbers.csv",
      //"\n".join(Samples.rawGestureData(JointType.HandLeft, JointType.HipCenter)));
    }

    public static void normalizeRotation(List<Gesture> gestures){
      C.p(gestures[0].samples[0].frames[0].skeleton.Joints[JointType.HandRight].Position.toPosition3().ToString());
      gestures.ForEach(g => g.samples.ForEach(normalizeRotation));
      C.p(gestures[0].samples[0].frames[0].skeleton.Joints[JointType.HandRight].Position.toPosition3().ToString());

    }
    public static int counter=0;
    public static void normalizeRotation(Sample g){
      g.frames.ForEach( f => f.skeleton.center(JointType.ShoulderCenter));
      var angles = g.frames.Select(f => angle(f.skeleton) );
      var rotations = angles.Select(rotationXZ).ToList();
      //rotations.ForEach(r => C.p(r.toString()));
      if (counter < 10){
        counter++;
        C.p(rotations[0].toString());
        
      }
      for (var i = 0; i < g.frames.Count; i++){
        rotate(g.frames[i].skeleton, rotations[i]);
      }
    }

    private static void rotate(Skeleton skeleton, float[,] R){
      
      foreach (Joint j in skeleton.Joints){
        var p = j.Position.toPosition3().rotateBy(R);
        var nj = j;
        nj.Position = p.toSkeletonPoint();
        skeleton.Joints[j.JointType] = nj;
      }
    }


    public static float[,] rotationXZ(Tuple<float,float> angle){
      var cos = angle.Item1;
      var sin = angle.Item2;
      return new float[3, 3]{
        {cos, 0f, sin},
        {0f, 1f, 0f},
        {-sin, 0f, cos}
      };
    }
    public static void normalizeGestureRotation(){
      var l = Samples.defaultLibrary();
      normalizeRotation(l.gestures);
      l.save();
    }
    public static Tuple<float,float> angle(Skeleton s){
      var v1 = -s.Joints[JointType.ShoulderLeft].Position.toPosition3();
      var v2 = s.Joints[JointType.ShoulderRight].Position.toPosition3();
      var v = v1 + v2;
      var a = new Position(v2.x, v2.y).normalize();
      return new Tuple<float, float>(a.x,a.y);
    }

    private static void Main(string[] args){
      Thread.CurrentThread.CurrentCulture = CultureInfo.CreateSpecificCulture("en-GB");
      //new TextMiningClassificationAlgorithmsComparison().run();
      //new ClassificationAlgorithmsComparison().run();
      //CNCMain.run();
      Experimentador.ejecutar();
      //exportGestures();
      //normalizeGestureRotation();
      //Visap2013GestureGenerator.generate();
       Console.ReadLine();

    }
  }
}