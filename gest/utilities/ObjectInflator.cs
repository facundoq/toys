using System;
using System.Collections.Generic;
using System.Reflection;
using KinectUI.utility;
using System.Linq;
using System.Text;

namespace utilities{
  public class ObjectInflator{
    public static T inflate<T>(IDictionary<String, Object> properties){
      T o = Activator.CreateInstance<T>();
      Type type = o.GetType();
      foreach (var x in properties){
        FieldInfo propertyInfo = type.GetField(x.Key);
        propertyInfo.SetValue(o, x.Value);
      }
      return o;
    }
  }
}