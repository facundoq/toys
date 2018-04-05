using System;
using System.Collections.Generic;
using System.Linq;

namespace utilities{
  


  public class Quadratic3 : Function3{
    public Position3 d;
    public Position3 b;

    public static Quadratic3 origin(Position3 d){
      return new Quadratic3(d, new Position3());
    }

    public Quadratic3(Position3 d, Position3 b){
      this.d = d;
      this.b = b;
    }

    public override string ToString(){
      return "[d:" + d + ";" + "b:" + b + "]";
    }

    public IEnumerable<Position4> at(IEnumerable<double> ts){
      var o = this;
      return ts.Select(t => o.at(t));
    }

    public Position4 at(double t){
      return at((float) t);
    }

    public Position4 at(float t){
      return new Position4(b + (d*(t*t)), t);
    }

    public static Quadratic3 random(Random r){
      return new Quadratic3(Position3.random(r), Position3.random(r));
    }

    public static Quadratic3 random(){
      return random(new Random());
    }
  }
}