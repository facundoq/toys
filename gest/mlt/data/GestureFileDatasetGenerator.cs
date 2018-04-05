using System;
using System.Collections.Generic;
using System.Globalization;
using mlt.classification;
using mlt.experiments;
using mlt.mlp;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.data {
  public class GestureFileDatasetGenerator : DatasetGenerator<GestureProcessingOptions> {
    public readonly string filepath;
    public GestureFileDatasetGenerator(List<GestureProcessingOptions> options,String filepath) : base(options){
      this.filepath = filepath;
    }

    public override Dataset generateDataset(GestureProcessingOptions options){
      IEnumerable<Pattern3D> data = readFile(filepath);
      if (options.smoothingWindowSize > 0){
        data=data.Select(p => smooth(p,options.smoothingWindowSize));
      }
      if (options.n > 0){
        data = data.Select( p => resample(p, options.n));
      }
      if (options.smoothingWindowSize > 0) {
        data = data.Select(p => smooth(p, options.smoothingWindowSize));
      }
      data = data.Select(firstDifference);
      if (options.normalize){
        data = data.Select(normalize);
      }
      if (options.rotate) {
        data = data.Select(rotate);
      }
      List<Pattern> result;
      if (options.angles){
        result = data.Select(angleTransform).ToList();
      }else{
        result = data.Select(p => p.flatten()).ToList();
      }
      return new Dataset(result);
    }

    private Pattern3D rotate(Pattern3D p) {
      var x = p.x;
      var R = x[0].rotationMatrixTo(new Position3(1, 1, 1)); 
      var result = new Position3[x.Length];
      for (int i = 0; i < result.Length; i++) {
        result[i] = x[i].rotateBy(R);
      }
      p.x = result;
      return p;
    }

    private Pattern angleTransform(Pattern3D p){
      var x = new double[p.x.Length][];
      for (int i = 0; i < p.x.Length; i++) {
        x[i] = p.x[i].angle();
      }
      return new Pattern(x.flatten(),p.y);
    }

    private Pattern3D normalize(Pattern3D p){
      for (int i = 0; i < p.x.Length; i++) {
        p.x[i] = p.x[i].normalize();
      }
      return p;
    }

    private Pattern3D firstDifference(Pattern3D p){
      var x = p.x;
      var result = new Position3[x.Length - 1];
      for (int i = 0; i < result.Length; i++){
        result[i] = x[i + 1] - x[0];
      }
      p.x = result;
      return p;
    }


    private Position3[] resample(Position3[] positions, int n) {
      if (positions.Length == 1) {
        return positions[0].inArray(n);
      }
      float length = getTrajectoryLength(positions);
      Position3 current = positions[0];
      Position3 last = positions[0];
      int currentIndex = 0;
      var result = new Position3[n];
      float step = length / (float)result.Length;
      result[0] = positions[0];
      result[result.Length - 1] = positions.Last();
      float distance = 0;
      var distanceBetweenCurrentAndLast = step;
      for (int i = 1; i < result.Length - 1; i++) {
        float targetDistance = i * step;
        while (currentIndex < positions.Length - 1 && distance < targetDistance) {
          currentIndex++;
          last = positions[currentIndex - 1];
          current = positions[currentIndex];
          distanceBetweenCurrentAndLast = last.distanceTo(current);
          distance += distanceBetweenCurrentAndLast;
        }

        if (distanceBetweenCurrentAndLast < 0.000000001) {
          result[i] = last;
        } else {
          var weightCurrent = (distance - targetDistance) / distanceBetweenCurrentAndLast;
          var p = current * (1 - weightCurrent) + last * weightCurrent;
          result[i] = p;
        }
      }
      return result;
    }

    private Pattern3D resample(Pattern3D p, int n){
      p.x = resample(p.x, n);
      return p;
    }

    private float getTrajectoryLength(Position3[] positions) {
      var last = positions.first();
      float length = 0;
      foreach (var p in positions) {
        length += last.distanceTo(p);
        last = p;
      }
      return length;
    }

    private Pattern3D smooth(Pattern3D p, int w){
      if (p.x.Length <= 2*w+1){
        return p;
      }
      var x = p.x;
      var positions = p.x.Length;
      var r = new Position3[positions];
      r[0] = x[0];
      r[positions - 1] = x[positions - 1];
      for (int i = 1; i < positions-1; i++){
        int realW =Math.Min(w,i);
        realW = Math.Min(realW,positions-i-1);
        Position3 s = new Position3();
        for (int j = i - realW; j <= i + realW; j++) {
          s += x[j];
        }
        x[i] = s / ((realW * 2) + 1);
      }

      p.x = x;
      return p;
    }

    public List<Pattern3D> readFile(String filepath){
      return (from line in filepath.ReadFrom()
             where line.Length>0 && !line.StartsWith("#") 
             select parsePattern(line)).ToList();
    }

    private Pattern3D parsePattern(string line){
      var parts = line.Split(','.inArray());
      //C.p(parts.Length.ToString());
      var y = Int32.Parse(parts[1]);
      var x = parts.Where((v, i) => i > 2).Select(v => Double.Parse(v, CultureInfo.InvariantCulture)).ToArray();
      return new Pattern3D(x,y);
    }

  }
}
