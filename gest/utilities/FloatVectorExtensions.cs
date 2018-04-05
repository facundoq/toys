using System;
using System.Collections.Generic;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace utilities {
  public static class FloatVectorExtensions {

    public static float[] times(this float[,] x, float[] y) {
      var m = x.GetLength(0);
      var p = y.Length;
      var result = new float[m];
      for (int i = 0; i < m; i++){
        result[i] = 0;
          for (int k = 0; k < p; k++) {
            result[i] += x[i, k] * y[k];
        }
      }
      return result;
    }
    public static float[,] times(this float[,] x, float[,] y) {
      var m = x.GetLength(0);
      var n = x.GetLength(1);
      var p = x.GetLength(1);
      var result = new float[m, p];
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < p; j++){
          result[i, j] = 0;
          for (int k = 0; k < n; k++){
            result[i, j] += x[i, k] * y[k,j];
          }
        }
      }
      return result;
    }

    public static float[,] times(this float[,] x, float y) {
      var m = x.GetLength(0);
      var n = x.GetLength(1);
      var result = new float[m, n];
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          result[i, j] = x[i, j] * y;
        }
      }
      return result;
    }

    public static float[] times(this float[] x, float y) {
      var result = new float[x.Length];
      for (int i = 0; i < result.Length; i++) {
        result[i] = x[i] * y;
      }
      return result;
    }

    public static float norm2(this float[] x){
      return (float) Math.Sqrt(x.norm2squared());
    }

    public static float[] normalize(this float[] x) {
      return x.times(1/x.norm2());
    }

    public static float norm2squared(this float[] x) {
      var result = 0f;
      for (int i = 0; i < x.Length; i++) {
        result += x[i]*x[i];
      }
      return result;
    }

    public static float dot(this float[] x, float[] y) {
      var result = 0f;
      for (int i = 0; i < x.Length; i++) {
        result += x[i] * y[i];
      }
      return result;
    }

    public static float[] cross(this float[] x, float[] y){
      return new[]{ 
      x[1]*y[2]- x[2]*y[1],
      -x[0]*y[2]+ x[2]*y[0],
      x[0]*y[1]- x[1]*y[0]
      };
    }
    public static float SumMagnitudes(this float[] x) {
      var result = 0f;
      for (var i = 0; i < x.Length; i++) {
        var e = x[i];
        result += (e < 0) ? -e : e;
      }
      return result;
    }

    public static float[,] plus(this float[,] x, float[,] y){
      var m = x.GetLength(0);
      var n = x.GetLength(1);
      var result = new float[m,n];
      for (int i = 0; i < m; i++){
        for (int j = 0; j < n; j++){
          result[i,j] = x[i,j] + y[i,j];
        }
      }
      return result;
    }


    public static List<float[]> rows(this float[,] m) {
      var cols = m.GetLength(1);
      var rows = m.GetLength(0);
      var result = new List<float[]>();
      for (int i = 0; i < rows; i++) {
        var row = new float[cols];
        for (int j = 0; j < row.Length; j++) {
          row[j] = m[i, j];
        }
        result.Add(row);
      }
      return result;
    }

    public static string toString(this float[,] x) {
      return "\n".@join(x.rows().Select(
        r => ",".@join(
          r.Select(i => i.ToString("0.00")))));
    }

    public static string toString(this float[] x) {
      return ";".@join(x.Select(i => i.ToString("0.00")));
    }
  }
}
