using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace utilities{
  public struct Interval{
    public readonly int from, to;

    public Interval(int from, int to){
      this.from = from;
      this.to = to;
    }

    public Interval(int to) : this(0, to){
    }

    public int length(){
      return to - from;
    }

    public Interval restrict(int from, int to){
      return new Interval(Math.Max(from, this.from), Math.Min(to, this.to));
    }

    public bool contains(double n){
      return from <= n && n <= to;
    }
  }

  public struct DoubleInterval{
    public readonly double from, to;

    public DoubleInterval(double from, double to){
      this.from = from;
      this.to = to;
    }

    public DoubleInterval(double to) : this(0, to){
    }

    public double length(){
      return to - from;
    }

    public DoubleInterval restrict(double from, double to){
      return new DoubleInterval(Math.Max(from, this.from), Math.Min(to, this.to));
    }

    public bool contains(double n){
      return from <= n && n <= to;
    }
  }

  public struct Range : IEnumerable<int>{
    public readonly int step;
    public readonly Interval i;

    public Range(Interval i) : this(i, 1){
    }

    public Range(int to)
      : this(new Interval(to), 1){
    }

    public Range(int from, int to, int step) : this(new Interval(from, to), step){
    }

    public Range(Interval i, int step){
      this.i = i;
      this.step = step;
    }

    public IEnumerator<int> GetEnumerator(){
      int j;
      for (j = i.from; j < i.to; j += step){
        yield return j;
      }
    }

    IEnumerator IEnumerable.GetEnumerator(){
      return GetEnumerator();
    }

    public double length(){
      return i.length();
    }

    public Range restrict(int from, int to){
      return new Range(i.restrict(from, to), step);
    }

    public bool contains(int n){
      return i.contains(n);
    }
  }

  public struct DoubleRange : IEnumerable<double>{
    public readonly double step;
    public readonly DoubleInterval i;

    public DoubleRange(DoubleInterval i)
      : this(i, 1){
    }

    public DoubleRange(double to)
      : this(new DoubleInterval(to), 1){
    }

    public DoubleRange(DoubleInterval i, double step){
      this.i = i;
      this.step = step;
    }

    public DoubleRange(double from, double to, double step) : this(new DoubleInterval(from, to), step){
    }

    public IEnumerator<double> GetEnumerator(){
      double j;
      for (j = i.from; j < i.to; j += step){
        yield return j;
      }
    }

    IEnumerator IEnumerable.GetEnumerator(){
      return GetEnumerator();
    }

    public double length(){
      return i.length();
    }

    public DoubleRange restrict(double from, double to){
      return new DoubleRange(i.restrict(from, to), step);
    }

    public bool contains(double n){
      return i.contains(n);
    }
  }
}