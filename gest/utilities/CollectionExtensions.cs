using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.Linq;
using System.Text;
using MathNet.Numerics.LinearAlgebra.Double;
using MathNet.Numerics.LinearAlgebra.Generic;

namespace utilities{
  public static class CollectionExtensions{
    public static int columnCount(this double[,] m){
      return m.GetLength(1);
    }

    public static int rowCount(this double[,] m){
      return m.GetLength(0);
    }

    public static double[] toDouble(this int[] v){
      return v.Select(i => (double) i).ToArray();
    }

    public static double mean(this double[] v){
      return v.Sum()/v.Length;
    }

    public static double[] means(this double[,] m){
      return m.sumOfColumns().divide(m.rowCount().inArray(m.columnCount()).toDouble());
    }

    public static double[] sds(this double[,] m){
      return m.se(m.means()).divide(((double) (m.rowCount() - 1)).inArray(m.columnCount()));
    }

    public static T[] subArray<T>(this T[] v, int from, int to){
      var result = new T[to - from];
      for (int i = 0; i < result.Length; i++){
        result[i] = v[from + i];
      }
      return result;
    }


    public static double[] flatten(this double[][] v){
      var result=new List<double>();
      foreach (double[] a in v){
        foreach (double d in a){
          result.Add(d);
        }
      }
      return result.ToArray();
    }

    public static T[,] splitIntoGroupsArray<T>(this T[] v, int groupSize) {
      var groups = v.Length / groupSize;
      var result = new T[groups,groupSize];
      for (int i = 0; i < groups; i++) {
        var offset = i * groupSize;
        for (int j = 0; j < groupSize;j++ ){
          result[i,j]=v[offset+j];
        }
        }
      return result;
    }

    public static List<T[]> splitIntoGroups<T>(this T[] v, int groupSize) {
      var result = new List<T[]>();
      var groups = v.Length / groupSize;
      for (int i = 0; i < groups; i++) {
        var offset = i * groupSize;
        result.add(v.subArray(offset, offset + groupSize));
      }
      return result;
    }

    public static float[] toFloatArray(this double[] a){
      return a.Select(v => (float) v).ToArray();
    }

    public static double[] insert(this double[] a,double v,int pos){
      var r = new double[a.Length + 1];
      r[pos] = v;
      for (int i = 0; i < pos;i++){
        r[i] = a[i];
      }
      for (int i = pos; i < a.Length; i++) {
        r[i+1] = a[i];
      }
      return r;
    }

    
    public static void AddAll<T>(this ICollection<T> collection, IEnumerable<T> newItems){
      foreach (T item in newItems){
        collection.Add(item);
      }
    }

    public static void AddAll<T>(this ObservableCollection<T> collection, IEnumerable<T> newItems){
      foreach (T item in newItems){
        collection.Add(item);
      }
    }

    public static Range asRange(this int n){
      return new Range(n);
    }

    public static void repeat(this int n, Action<int> a){
      for (int i = 0; i < n; i++){
        a(n);
      }
    }

    public static void each<T>(this IEnumerable<T> xs, Action<T, int> a){
      for (int i = 0; i < xs.Count(); i++){
        a(xs.ElementAt(i), i);
      }
    }

    public static List<T> results<T>(this IEnumerable<Maybe<T>> xs){
      return xs.Where(x => x.isResult()).Select(x => ((Result<T>) x).r).ToList();
    }

    public static List<T> map<T>(this int n, Func<int, T> f){
      var result = new List<T>();
      for (int i = 0; i < n; i++){
        result.Add(f(i));
      }
      return result;
    }

    public static IDictionary<T, E> clone<T, E>(this IDictionary<T, E> d){
      var result = new Dictionary<T, E>();
      foreach (var k in d){
        result.Add(k.Key, k.Value);
      }
      return result;
    }

    public static String toString<T, E>(this IDictionary<T, E> d){
      var result = "{";
      foreach (var x in d){
        result += " " + x.Key + ":" + x.Value + ",";
      }
      result += "}";
      return result;
    }

    public static OrderedDictionary clone(this OrderedDictionary d){
      var result = new OrderedDictionary();
      foreach (var k in d.Keys){
        result.Add(k, d[k]);
      }
      return result;
    }

    public static List<T> add<T>(this List<T> l, T e){
      l.Add(e);
      return l;
    }

    public static T removeAt<T>(this List<T> l, int i){
      var e = l[i];
      l.RemoveAt(i);
      return e;
    }

    public static void ForEach<T>(this IEnumerable<T> c, Action<int, T> action){
      int i = 0;
      foreach (var x in c){
        action.Invoke(i, x);
        i++;
      }
    }

    public static void ForEach<T>(this IEnumerable<T> c, Action<T> action){
      foreach (var x in c){
        action.Invoke(x);
      }
    }

    public static List<T> clone<T>(this List<T> l){
      var result = new List<T>();
      for (int i = 0; i < l.Count; i++){
        result.Add(l[i]);
      }
      return result;
    }

    public static List<T> inList<T>(this T item){
      return new List<T>{item};
    }

    public static List<T> inList<T>(this T item, int n){
      return n.map(i => item);
    }

    public static List<T> inListCloning<T>(this T item, int n) where T : ICloneable{
      return n.map(i => (T) item.Clone());
    }

    public static T[] inArray<T>(this T item){
      return new T[]{item};
    }
    public static int toInt(this bool b){
      return (b) ? 1 : 0;
    }
    public static int indexOf<T>(this T[] ts, T e){
      for (int i = 0; i < ts.Length; i++){
        if (ts[i].Equals(e)){
          return i;
        }
      }
      return -1;
    }
    /// Warning: n*m on number of indices m and elements n
    public static IEnumerable<T> at<T>(this IEnumerable<T> ts,IEnumerable<int> indices){
      return ts.Where((v, i) => indices.Contains(i));
    }
    public static List<T> getAll<T>(this IEnumerable<T> ts, IEnumerable<int> indices){
      var result = new List<T>();
      indices.ForEach(i => result.Add(ts.ElementAt(i)));
      return result;
    }

    public static DenseVector insert(this DenseVector a, double v, int pos) {
      var r =  new DenseVector(a.Count + 1);
      r[pos] = v;
      for (int i = 0; i < pos; i++) {
        r[i] = a[i];
      }
      for (int i = pos; i < a.Count; i++) {
        r[i + 1] = a[i];
      }
      return r;
    }

    public static Range indexes<T>(this IEnumerable<T> ts) {
      return new Range(0,ts.Count(),1);
    }
    public static List<int> indexesOf<T>(this T[] ts, T e){
      var result = new List<int>();
      for (int i = 0; i < ts.Length; i++){
        if (ts[i].Equals(e)){
          result.add(i);
        }
      }
      return result;
    }

    

    public static T[] inArray<T>(this T item, int n){
      var ts = new T[n];
      for (int i = 0; i < n; i++){
        ts[i] = item;
      }
      return ts;
    }

    public static double abs(this double d){
      return Math.Abs(d);
    }

    public static DenseVector abs(this DenseVector d){
      var result = (DenseVector) d.Clone();
      for (int i = 0; i < result.Count; i++){
        result[i] = result[i].abs();
      }
      return result;
    }

    public static void randomize(this Matrix m, double from, double to){
      var r = new Random();

      for (int i = 0; i < m.RowCount; i++){
        for (int j = 0; j < m.ColumnCount; j++){
          m[i, j] = r.NextDouble()*(to - from) + from;
        }
      }
    }

    public static IEnumerable<T> onlyOfType<E, T>(this IEnumerable<E> xs) where T : E{
      return (from x in xs where x.GetType() == typeof (T) select (T) x);
    }

    public static IDictionary<T, E> addAll<T, E>(this IDictionary<T, E> d, IDictionary<T, E> x){
      d.AddAll(x);
      return d;
    }

    public static string join<T>(this string s, IEnumerable<T> xs){
      return string.Join(s, xs);
    }

    public static T first<T>(this IList<T> list){
      return list[0];
    }

    public static Boolean empty<T>(this IList<T> list){
      return list.Count == 0;
    }

    public static String toString<T>(this IList<T> list){
      return "[" + ",".join(list) + "]";
    }

    public static Boolean hasElements<T>(this IList<T> list){
      return list.Count != 0;
    }

    public static T last<T>(this IList<T> list){
      return list[list.Count - 1];
    }

    public static int MaxIndex<T>(this IList<T> list, Func<T, double> function){
      int result = -1;
      double max = Double.MinValue;
      for (int i = 0; i < list.Count; i++){
        double y = function.Invoke(list[i]);
        if (y > max){
          max = y;
          result = i;
        }
      }
      return result;
    }

    public static int Max2Index(this IList<Double> list){
      int maxIndex = -1;
      int maxIndex2 = -1;
      double max = Double.MinValue;
      double max2 = Double.MinValue;
      for (int i = 0; i < list.Count; i++){
        Double y = list[i];
        if (y > max){
          max2 = max;
          maxIndex2 = maxIndex;
          max = y;
          maxIndex = i;
        }
        else if (y > max2){
          max2 = y;
          maxIndex2 = i;
        }
      }
      return maxIndex2;
    }

    public static int MinIndex(this IList<Double> list) {
      int result = -1;
      double min = Double.MaxValue;
      for (int i = 0; i < list.Count; i++) {
        Double y = list[i];
        if (y < min) {
          min = y;
          result = i;
        }
      }
      return result;
    }
    public static int MaxIndex(this IList<Double> list){
      int result = -1;
      double max = Double.MinValue;
      for (int i = 0; i < list.Count; i++){
        Double y = list[i];
        if (y > max){
          max = y;
          result = i;
        }
      }
      return result;
    }

    public static void shuffle<T>(this IList<T> list,Random r) {
      int n = list.Count;
      while (n > 1) {
        n--;
        int k = r.Next(list.Count);
        T value = list[k];
        list[k] = list[n];
        list[n] = value;
      }
    }
    public static void shuffle<T>(this IList<T> list){
      shuffle(list,new Random());
    }

    public struct EnumeratedInstance<T>{
      public int i;
      public T e;
    }

    public static IEnumerable<EnumeratedInstance<T>> enumerate<T>(this IEnumerable<T> collection){
      int counter = 0;
      foreach (var item in collection){
        yield return new EnumeratedInstance<T>{
          e = item,
          i = counter++
        };
      }
    }
  }
}