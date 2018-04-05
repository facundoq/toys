using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KinectUI.mouse.utility{
  public static class Utility<T>{
    public static List<T> with(params T[] e){
      List<T> l = new List<T>();
      l.AddRange(e);
      return l;
    }
  }
}