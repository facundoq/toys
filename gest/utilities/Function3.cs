using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace utilities{
  public interface Function3{
    IEnumerable<Position4> at(IEnumerable<double> ts);
    Position4 at(float t);
  }
}