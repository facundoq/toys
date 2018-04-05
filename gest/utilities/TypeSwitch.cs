using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace utilities{
  public class TypeNotFoundException : Exception{
  }

  public class TypeSwitch{
    private Action elseAction;
    private Dictionary<Type, Action<object>> matches = new Dictionary<Type, Action<object>>();

    public TypeSwitch Case<T>(Action<T> action){
      matches.Add(typeof (T), (x) => action((T) x));
      return this;
    }

    public TypeSwitch Else(Action action){
      elseAction = action;
      return this;
    }

    public void Switch(object x){
      Type type = x.GetType();
      if (matches.ContainsKey(type)){
        matches[type](x);
      }
      else if (elseAction != null){
        elseAction();
      }
      else{
        throw new TypeNotFoundException();
      }
    }
  }
}