using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using utilities;

namespace mlt.gmm{
  public class GMMParameters{
    public int groupSize;
    public double spread;
    public readonly Distance distance;

    public GMMParameters(int groupSize, double spread, Distance distance) {
      this.groupSize = groupSize;
      this.spread = spread;
      this.distance = distance;
    }
  }
}