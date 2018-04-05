using System;
using System.Collections.Generic;
using System.Linq;
using SampleLibrary;
using utilities;

namespace mlt.samples{
  public class PreprocessingParameters{
    public int samplingPoints; //sampling points
    public int w; //smoothing window size (w frames in the past and future, 2*w total)

    public PreprocessingParameters(int samplingPoints, int w){
      this.samplingPoints = samplingPoints;
      this.w = w;
    }
  }

  public class SamplePreprocessor{
    public List<double[]> preprocess(PreprocessingParameters parameters, List<Position4[]> samples,
                                     List<Position3[]> centers, List<Plane3[]> floorClippingPlanes){
      return samples.Select((s, i) => preprocess(parameters, s, centers[i], floorClippingPlanes[i])).ToList();
    }

    public double[] preprocess(PreprocessingParameters parameters, Position4[] positions, Position3[] centers,
                               Plane3[] floorClippingPlanes){
      center(positions, centers, floorClippingPlanes);
      toUnitTime(positions);
      //smooth(positions, p.w);
      var resampledPositions = resampleByTrajectoryLength(positions, parameters);
      //Console.WriteLine( "aaaaaaaaaaaaaaaaaaaaaaaaaaa");
      //Console.WriteLine(positions.toString());
      //Console.WriteLine( "qqqqqqqqqqqqqqqqqqqq");
      //Console.WriteLine(resampledPositions.toString());
      return vectorForm(resampledPositions);
    }

    private void toUnitTime(Position4[] positions){
      float length = positions.Last().t;
      for (int i = 0; i < positions.Length; i++){
        positions[i].t /= length;
      }
    }

    private void center(Position4[] positions, Position3[] centers, Plane3[] floorClippingPlanes){
      for (int i = 0; i < positions.Length; i++){
        positions[i].p -= centers[i];
        positions[i].p.y += floorClippingPlanes[i].d;
      }
    }

    private Position4[] resampleByTrajectoryLength(Position4[] positions, PreprocessingParameters detectionParameters){
      if (positions.Length == 1){
        return positions[0].inArray(detectionParameters.samplingPoints);
      }
      float length = getTrajectoryLength(positions);
      Position4 current = positions[0];
      Position4 last = positions[0];
      int currentIndex = 0;
      var result = new Position4[detectionParameters.samplingPoints];
      float step = length/(float) result.Length;
      result[0] = positions[0];
      result[result.Length - 1] = positions.Last();
      float distance = 0;
      var distanceBetweenCurrentAndLast = step;
      for (int i = 1; i < result.Length - 1; i++){
        float targetDistance = i*step;
        while (currentIndex < positions.Length - 1 && distance < targetDistance){
          currentIndex++;
          last = positions[currentIndex - 1];
          current = positions[currentIndex];
          distanceBetweenCurrentAndLast = last.distanceTo(current);
          distance += distanceBetweenCurrentAndLast;
        }

        if (distanceBetweenCurrentAndLast < 0.00001){
          result[i] = last;
        }
        else{
          float weightCurrent = (distance - targetDistance)/distanceBetweenCurrentAndLast;
          var p = last.p*(1 - weightCurrent) + current.p*weightCurrent;
          result[i] = new Position4(p, last.t*(1 - weightCurrent) + current.t*weightCurrent);
        }
      }
      return result;
    }

    private float getTrajectoryLength(Position4[] positions){
      var last = positions.first();
      float length = 0;
      foreach (var p in positions){
        length += last.distanceTo(p);
        last = p;
      }
      return length;
    }

    private Position4[] resampleByTime(Position4[] positions, PreprocessingParameters detectionParameters){
      if (positions.Length == 1){
        return positions[0].inArray(detectionParameters.samplingPoints);
      }
      Position4 current = positions[0];
      Position4 last = positions[0];
      int currentIndex = 1;
      var result = new Position4[detectionParameters.samplingPoints];
      float step = (float) 1/result.Length;
      result[0] = positions[0];
      result[result.Length - 1] = positions.Last();
      for (int i = 1; i < result.Length - 1; i++){
        float t = i*step;

        while (currentIndex < positions.Length - 1 && positions[currentIndex].t <= t){
          currentIndex++;
        }
        last = positions[currentIndex - 1];
        current = positions[currentIndex];
        if (last.distanceTo(current) < 0.00001){
          result[i] = last;
        }
        else{
          result[i] = last.lineTo(current).at(t);
        }
      }
      return result;
    }

    private void smooth(Position4[] positions, int w){
      for (int i = 1; i < positions.Length - 1; i++){
        bool inBorder = (i < w || i >= (positions.Length - w));
        int l = inBorder ? i : w;
        Position3 s = new Position3();
        for (int j = i - l; j <= i + l; j++){
          s += positions[j].p;
        }
        positions[i].p = s/((l*2) + 1);
      }
    }

    private double[] vectorForm(Position4[] positions){
      double[] result = new double[positions.Length*3 + 1];
      result[0] = 1;
      foreach (var p in positions.enumerate()){
        long i = p.i*3 + 1;
        var point = p.e;
        result[i] = point.p.x;
        result[i + 1] = point.p.y;
        result[i + 2] = point.p.z;
      }
      return result;
    }
  }
}