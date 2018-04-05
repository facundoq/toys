using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using utilities;

namespace SampleLibrary{
  [Serializable]
  public class Plane3{
    public float a, b, c, d;

    public Plane3(float a, float b, float c, float d){
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
    }

    public Plane3(Tuple<float, float, float, float> t){
      a = t.Item1;
      b = t.Item2;
      c = t.Item3;
      d = t.Item4;
    }

    public float[] coeficients(){
      return new float[]{a, b, c, d};
    }

    public override string ToString(){
      return "(" + (",".join(coeficients())) + ")";
    }

    public Plane3(){
    }
  }
}