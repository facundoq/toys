using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace utilities{
  public abstract class Maybe<T>{
    public static Maybe<T> Nothing = new Nothing<T>();
    public abstract void ifResult(Action<T> a);

    public static Maybe<T> unit(T t){
      return new Result<T>(t);
    }

    public abstract bool isResult();
  }

  public class Result<T> : Maybe<T>{
    public T r;

    public Result(T r){
      this.r = r;
    }

    public override void ifResult(Action<T> a){
      a.Invoke(r);
    }

    public override bool isResult(){
      return true;
    }
  }

  public class Nothing<T> : Maybe<T>{
    public override void ifResult(Action<T> a){
    }

    public override bool isResult(){
      return false;
    }
  }
}