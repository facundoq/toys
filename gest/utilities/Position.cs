using System;

namespace utilities{
  public struct Position{
    public float x;
    public float y;

    public override string ToString(){
      string format = "0.00";
      return "(" + x.ToString(format) + "," + y.ToString(format) + ")";
    }

    private float restrict(float f, float from, float to){
      f = Math.Max(f, from);
      f = Math.Min(f, to);
      return f;
    }

    public Position clipTo(Area r){
      float x = restrict(this.x, r.x1, r.x2);
      float y = restrict(this.y, r.y1, r.y2);
      return new Position(x, y);
    }

    public Position(float x, float y){
      this.x = x;
      this.y = y;
    }

    // scales from source space to destination space
    public Position scale(Area source, Area destination){
      return this.scaleToUnitSpace(source).scaleFromUnitSpace(destination);
    }

    //scales from unit space to to space
    public Position scaleFromUnitSpace(Area to){
      float x = (this.x*to.width()) + to.x1;
      float y = (this.y*to.height()) + to.y1;
      return new Position(x, y);
    }

    // scales from from space to unit space
    public Position scaleToUnitSpace(Area from){
      float x = (this.x - from.x1)/from.width();
      float y = (this.y - from.y1)/from.height();
      return new Position(x, y);
    }

    public Position flipY(){
      return new Position(x, -y);
    }

    public Position flipX(){
      return new Position(-x, y);
    }

    public Position normalize(){
      float n = norm();
      return new Position(x/n,y/n);
    }

    public float norm(){
      return (float) Math.Sqrt(normSquared());
    }
    public float normSquared(){
      return x*x + y*y;
    }
  }
}