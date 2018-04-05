using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace utilities{
  public class NoFunctionPatternMatchesException : Exception{
  }

  public class FunctionPattern<Output>{
    private List<Tuple<Predicate<Object>, Func<Object, Output>>> cases =
      new List<Tuple<Predicate<object>, Func<object, Output>>>();

    public FunctionPattern(){
    }

    public FunctionPattern<Output> Case(Predicate<Object> condition, Func<Object, Output> function){
      cases.Add(new Tuple<Predicate<Object>, Func<Object, Output>>(condition, function));
      return this;
    }

    public FunctionPattern<Output> Case<T>(Predicate<T> condition, Func<T, Output> function){
      return Case(
        o => o is T && condition((T) o),
        o => function((T) o));
    }

    public FunctionPattern<Output> Case<T>(Func<T, Output> function){
      return Case(
        o => o is T,
        o => function((T) o));
    }

    public FunctionPattern<Output> Case<T>(Predicate<T> condition, Output o){
      return Case(condition, x => o);
    }

    public FunctionPattern<Output> Case<T>(Output o){
      return Case<T>(x => o);
    }

    public FunctionPattern<Output> Default(Func<Object, Output> function){
      return Case(o => true, function);
    }

    public FunctionPattern<Output> Default(Output o){
      return Default(x => o);
    }

    public Output Match(Object o){
      foreach (var tuple in cases){
        if (tuple.Item1(o)){
          return tuple.Item2(o);
        }
      }
      throw new NoFunctionPatternMatchesException();
    }
  }
}