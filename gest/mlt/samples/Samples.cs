using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using SampleLibrary;
using mlt.mlp;
using utilities;

namespace mlt.samples{
  public class Samples{
    public static List<List<double[]>> threeLinesSample(int trainingSetSize, PreprocessingParameters p){
      Line3[] lines ={
        new Line3(new Position3(-1, 0.5f, -0.25f), new Position3(4)),
        new Line3(new Position3(-0.2f, -0.3f, 0.05f), new Position3(1)),
        new Line3(new Position3(+0.15f, -0.53f, 0.15f), new Position3(-5))
      };
      return generatePoints(trainingSetSize, p, lines.ToList());
    }

    public static List<List<double[]>> linesSample(int similarForEach, PreprocessingParameters p,
                                                   int differentLines){
      var r = new Random();
      IEnumerable<Line3> lines = differentLines.asRange().Select(i => Line3.random(r));
      //Console.WriteLine( "\nLines");
      //fs.ForEach(l => Console.WriteLine(l));
      return generatePoints(similarForEach, p, lines);
    }

    public static List<List<double[]>> quadraticSample(int trainingSetSize, PreprocessingParameters pp, int gestureTypes){
      var r = new Random();
      var quadratics = gestureTypes.asRange().Select(i => Quadratic3.random(r));
      return generatePoints(trainingSetSize, pp, quadratics);
    }
    public static List<List<double[]>> gaussianSample(int samplesPerClass, PreprocessingParameters pp, int gestureTypes) {
      var r = new Random();
      var quadratics = gestureTypes.asRange().Select(i => Quadratic3.random(r));
      return generatePoints(samplesPerClass, pp, quadratics);
    }

    private static List<List<double[]>> generatePoints(int similarForEach, PreprocessingParameters p,
                                                       IEnumerable<Function3> fs){
      var r = new DoubleRange(0, 2, (double) 2/p.samplingPoints).Take(p.samplingPoints);
      List<double[]> inputs = fs.Select(l => createInput(p, l, new Position3(), r)).ToList();
      return generateSimilarSamples(inputs, similarForEach);
    }

    private static List<List<double[]>> generateSimilarSamples(List<double[]> samples, int similarForEach){
      return samples.Select<double[], List<double[]>>(i => createSimilar(i, similarForEach)).ToList();
    }

    private static List<double[]> createSimilar(double[] xs, int n){
      var d = new Random(93847);
      var result = xs.inListCloning(n);
      result.ForEach(ys =>{
        for (int i = 0; i < ys.Length; i++){
          ys[i] += (d.NextDouble() - 0.5)/10;
        }
      });
      return result;
    }

    public static double[] createInput(PreprocessingParameters p, Function3 f, Position3 center, IEnumerable<double> r){
      var t = Tuple.Create(f.at(r).ToArray(), center.inArray(r.Count()), new Plane3().inArray(r.Count()));
      return new SamplePreprocessor().preprocess(p, t.Item1, t.Item2, t.Item3);
    }

    public static List<List<double[]>> librarySample(PreprocessingParameters pp, int maximumSamples,
                                                     int kinectGestures, List<Gesture> gestures){
      var ig = new NetworkInputGenerator(JointType.HandLeft, JointType.HipCenter);
      var sp = new SamplePreprocessor();
      kinectGestures = Math.Min(kinectGestures, gestures.Count);
      return gestures.Take(kinectGestures).Select(g =>{
        C.p();
        g.samples = g.samples.Take(Math.Min(maximumSamples, g.samples.Count)).ToList();
        var t = ig.generate(g);
        return sp.preprocess(pp, t.Item1, t.Item2, t.Item3);
      }).ToList();
    }

    public static Library defaultLibrary(){
      return new Library("D:\\dev\\cs\\KinectUI\\bin\\x86\\Release\\library");
    }

    public static List<List<double[]>> librarySample(PreprocessingParameters pp, int maximumSamples, int kinectGestures){
      Library library = defaultLibrary();
      return librarySample(pp, maximumSamples, kinectGestures, library.gestures);
    }

    private static Position3 center(SkeletonFrameInfo frame, JointType joint, JointType center){
      var p = frame.skeleton.Joints[joint].Position.toPosition3();
      //p -= frame.skeleton.Joints[center].Position.toPosition3();
      //p.y += frame.frame.floorClippingPlane.d;
      return p;
    }

    public static List<string> rawGestureData(JointType joint, JointType center){
      System.Globalization.CultureInfo customCulture =
        (System.Globalization.CultureInfo) System.Threading.Thread.CurrentThread.CurrentCulture.Clone();
      customCulture.NumberFormat.NumberDecimalSeparator = ".";
      System.Threading.Thread.CurrentThread.CurrentCulture = customCulture;
      var gestures = defaultLibrary().gestures;

      return gestures.enumerate()
                             .SelectMany(g => g.e.samples
                                               .Select(s => g.e.id + "," + g.i + ",1," + ",".join(s.frames
                                                                                                   .Where(
                                                                                                     f =>
                                                                                                     f.skeleton.Joints[
                                                                                                       joint]
                                                                                                       .TrackingState !=
                                                                                                     JointTrackingState
                                                                                                       .NotTracked
                                                                                                     &&
                                                                                                     f.skeleton.Joints[
                                                                                                       center]
                                                                                                       .TrackingState !=
                                                                                                     JointTrackingState
                                                                                                       .NotTracked)
                                                                                                   .Select(f =>{
                                                                                                     var p =
                                                                                                       Samples.center(
                                                                                                         f, joint,
                                                                                                         center);
                                                                                                     return "" + p.x +
                                                                                                            "," + p.y +
                                                                                                            "," + p.z;
                                                                                                   })))).ToList();
    }
  }
}