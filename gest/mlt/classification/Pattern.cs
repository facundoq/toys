using System;
using System.Collections.Generic;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.classification{
  public struct Pattern{
    public double[] x;
    public int y;

    public Pattern(double[] x, int y){
      this.x = x;
      this.y = y;
    }

    public List<Pattern> splitIntoGroups(int groupSize){
      var thisy = y;
      return x.splitIntoGroups(groupSize).Select(pg => new Pattern(pg, thisy)).ToList();
    }
  }

  public struct Pattern3D{
    public Position3[] x;
    public int y;

    public Pattern3D(Position3[] x, int y){
      this.x = x;
      this.y = y;
    }

    public Pattern3D(double[] x, int y){
      this.x = x.splitIntoGroups(3).Select(g => new Position3(g)).ToArray();
      this.y = y;
    }

    public Pattern flatten(){
      double[] nx = new double[x.Length*3];
      for (int i = 0; i < x.Length; i++){
        int b = i*3;
        nx[b] = x[i].x;
        nx[b + 1] = x[i].y;
        nx[b + 2] = x[i].z;
      }
      return new Pattern(nx, y);
    }
  }
}