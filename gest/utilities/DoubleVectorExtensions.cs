using System;
using System.Collections.Generic;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace utilities {
  public static class DoubleVectorExtensions {
    public static List<double[]> splitIntoGroups(this double[] v, int groupSize) {
      var result = new List<double[]>();
      var groups = v.Length / groupSize;
      for (int i = 0; i < groups; i++) {
        var offset = i * groupSize;
        result.add(v.subArray(offset, offset + groupSize));
      }
      return result;
    }

    public static double distanceSquaredTo(this double[] from, double[] to) {
      double result = 0;
      for (int i = 0; i < @from.Length; i++) {
        result += Math.Pow(@from[i] - to[i], 2);
      }
      return result;
    }

    public static double distanceTo(this double[] from, double[] to) {
      double result = 0;
      for (int i = 0; i < @from.Length; i++) {
        result += Math.Pow(@from[i] - to[i], 2);
      }
      return Math.Sqrt(result);
    }

    public static double[,] rows(this double[,] m, IList<int> rows) {
      double[,] result = new double[rows.Count, m.columnCount()];

      for (int i = 0; i < rows.Count; i++) {
        var row = rows[i];
        for (int j = 0; j < m.columnCount(); j++) {
          result[i, j] = m[row, j];
        }
      }
      return result;
    }

    public static double[] row(this double[,] m, int row) {
      double[] result = new double[m.columnCount()];
      for (int i = 0; i < m.columnCount(); i++) {
        result[i] = m[row, i];
      }
      return result;
    }

    public static void setRow(this double[,] m, int row, double[] data) {
      for (int i = 0; i < m.columnCount(); i++) {
        m[row, i] = data[i];
      }
    }

    public static double[,] subColumns(this double[,] m, int from, int to) {
      int size = to - @from;
      double[,] result = new double[m.rowCount(), size];
      for (int i = 0; i < m.rowCount(); i++) {
        for (int j = @from; j < to; j++) {
          result[i, j - @from] = m[i, j];
        }
      }
      return result;
    }

    public static double[,] subMatrix(this double[,] m, int from, int to) {
      int size = to - @from;
      double[,] result = new double[size, m.columnCount()];
      for (int i = @from; i < to; i++) {
        for (int j = 0; j < m.columnCount(); j++) {
          result[i - @from, j] = m[i, j];
        }
      }
      return result;
    }

    public static double[] se(this double[,] m, double[] values) {
      var result = new double[m.columnCount()];
      var cols = m.GetLength(1);
      var rows = m.GetLength(0);
      for (int j = 0; j < cols; j++) {
        result[j] = 0;
        var value = values[j];
        for (int i = 0; i < rows; i++) {
          result[j] += Math.Pow(m[i, j] - value, 2);
        }
        result[j] = Math.Sqrt(result[j]);
      }
      return result;
    }

    public static double[] sse(this double[,] m, double[] values) {
      var result = new double[m.columnCount()];
      var cols = m.GetLength(1);
      var rows = m.GetLength(0);
      for (int j = 0; j < cols; j++) {
        result[j] = 0;
        var value = values[j];
        for (int i = 0; i < rows; i++) {
          result[j] += Math.Pow(m[i, j] - value, 2);
        }
      }
      return result;
    }

    public static double[] sumOfColumns(this double[,] m) {
      var result = new double[m.columnCount()];
      var cols = m.GetLength(1);
      var rows = m.GetLength(0);
      for (int j = 0; j < cols; j++) {
        result[j] = 0;
        for (int i = 0; i < rows; i++) {
          result[j] += m[i, j];
        }
      }
      return result;
    }

    public static void plusColumns(this double[,] m, double[] values) {
      var cols = m.GetLength(1);
      var rows = m.GetLength(0);
      for (int j = 0; j < cols; j++) {
        for (int i = 0; i < rows; i++) {
          m[i, j] += values[j];
        }
      }
    }

    public static void divideByColumns(this double[,] m, double[] values) {
      var cols = m.GetLength(1);
      var rows = m.GetLength(0);
      for (int j = 0; j < cols; j++) {
        for (int i = 0; i < rows; i++) {
          m[i, j] /= values[j];
        }
      }
    }

    public static void minusColumns(this double[,] m, double[] values) {
      var cols = m.GetLength(1);
      var rows = m.GetLength(0);
      for (int j = 0; j < cols; j++) {
        for (int i = 0; i < rows; i++) {
          m[i, j] -= values[j];
        }
      }
    }

    public static double[] plus(this double[] x, double[] y) {
      double[] result = new double[x.Length];
      for (int i = 0; i < result.Length; i++) {
        result[i] = x[i] + y[i];
      }
      return result;
    }

    public static double[] minus(this double[] x, double[] y) {
      double[] result = new double[x.Length];
      for (int i = 0; i < result.Length; i++) {
        result[i] = x[i] - y[i];
      }
      return result;
    }

    public static double[] divide(this double[] x, double[] y) {
      double[] result = new double[x.Length];
      for (int i = 0; i < result.Length; i++) {
        result[i] = x[i] / y[i];
      }
      return result;
    }

    public static List<double[]> rows(this double[,] m) {
      var cols = m.GetLength(1);
      var rows = m.GetLength(0);
      var result = new List<double[]>();
      for (int i = 0; i < rows; i++) {
        var row = new double[cols];
        for (int j = 0; j < row.Length; j++) {
          row[j] = m[i, j];
        }
        result.Add(row);
      }
      return result;
    }

    public static double[] times(this double[] x, double y) {
      double[] result = new double[x.Length];
      for (int i = 0; i < result.Length; i++) {
        result[i] = x[i] * y;
      }
      return result;
    }

    public static double dot(this double[] x, double[] y) {
      var result = 0d;
      for (int i = 0; i < x.Length; i++) {
        result += x[i] * y[i];
      }
      return result;
    }

    public static double[] cross(this double[] x, double[] y) {
      var result = new double[x.Length];
      for (int i = 0; i < x.Length; i++) {
        result[i]= x[i] * y[i];
      }
      return result;
    }
    public static double SumMagnitudes(this double[] x) {
      double result = 0;
      for (var i = 0; i < x.Length; i++) {
        var e = x[i];
        result += (e < 0) ? -e : e;
      }
      return result;
    }

    public static string toString(this double[,] x) {
      return "\n".@join(x.rows().Select(
        r => ",".@join(
          r.Select(i => i.ToString("0.00")))));
    }

    public static string toString(this double[] x) {
      return ";".@join(x.Select(i => i.ToString("0.00")));
    }
    public static bool greaterThanAll(this double[] x,int pos){
      bool greater = true;
      for (int i = 0; i < x.Length & greater; i++){
        if (i != pos){
          greater = x[i] < x[pos];
        }
      }
      return greater;
    }

  }
}
