using System;
using System.Collections.Generic;
using System.Linq;


namespace utilities{
  public struct Position4{
    public Position3 p;
    public float t;

    public Position4(Position3 p, float t){
      this.p = p;
      this.t = t;
    }

    public Line3 lineTo(Position4 q){
      Position3 slope = (p - q.p)/(t - q.t);
      Position3 b = p - slope*t;
      return new Line3(slope, b);
    }

    public float distanceTo(Position4 q){
      return p.distanceTo(q.p);
    }

    public override string ToString(){
      return string.Format("{0} (t:{1})", p, t);
    }
  }

  public class Line3 : Function3{
    public Position3 d;
    public Position3 b;

    public static Line3 origin(Position3 d){
      return new Line3(d, new Position3());
    }

    public static Line3 horizontal(Position3 b){
      return new Line3(new Position3(), b);
    }

    public Line3(Position3 d, Position3 b){
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

    public Position4 at(float t){
      return new Position4(b + (d*t), t);
    }

    public Position4 at(double time){
      var t = (float) time;
      return new Position4(b + (d*t), t);
    }

    public static Line3 random(Random r){
      return new Line3(Position3.random(r), Position3.random(r));
    }

    public static Line3 random(){
      return random(new Random());
    }
  }
  public struct Position3{
    public float x;
    public float y;
    public float z;

    public override string ToString(){
      var format = "0.00";
      return "(" + x.ToString(format) + "," + y.ToString(format) + "," + z.ToString(format) + ")";
    }

    private float restrict(float f, float from, float to){
      f = Math.Max(f, from);
      f = Math.Min(f, to);
      return f;
    }

    public Position3(float x, float y, float z){
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public Position3(float xyz) : this(xyz, xyz, xyz){
    }

    public Position3(double[] xyz)
      : this((float)xyz[0], (float)xyz[1], (float)xyz[2]) { 

    }

    public Position3(float[] xyz): this(xyz[0], xyz[1], xyz[2]) { 

    }

    public Position3 flipY(){
      return new Position3(x, -y, z);
    }

    public Position3 flipX(){
      return new Position3(-x, y, z);
    }

    public Position3 flipZ(){
      return new Position3(x, y, -z);
    }

    public static Position3 operator +(Position3 p, Position3 q){
      return new Position3(p.x + q.x, p.y + q.y, p.z + q.z);
    }

    public static Position3 operator -(Position3 p) {
      return new Position3(-p.x, -p.y, -p.z );
    }
    public static Position3 operator -(Position3 p, Position3 q){
      return new Position3(p.x - q.x, p.y - q.y, p.z - q.z);
    }

    public static Position3 operator *(Position3 p, float q){
      return new Position3(p.x*q, p.y*q, p.z*q);
    }

    public static Position3 operator /(Position3 p, float q){
      return new Position3(p.x/q, p.y/q, p.z/q);
    }

    public static Position3 operator +(Position3 p, float q){
      return new Position3(p.x + q, p.y + q, p.z + q);
    }

    public static Position3 random(Random r){
      return new Position3(r.nextFloat()*2 - 1, r.nextFloat()*2 - 1, r.nextFloat()*2 - 1);
    }

    public static Position3 random(){
      return random(new Random());
    }

    public float norm(){
      return (float) Math.Sqrt(x*x + y*y + z*z);
    }

    public float distanceTo(Position3 p){
      return (this - p).norm();
    }

    public Position3 normalize(){
      float n = norm();
      return (n > 0) ? this/n : this;
    }

    public double[] angle(){
      var r = norm();
      
      var p = 0d;
      var t = 0d;
      if(r!=0) {
        t = (2*Math.Acos(z / r))/ Math.PI-1;
        p = Math.Atan2(y, x)/Math.PI;
      }
      return new[]{p,t};
    }

    /* this and p must have norm 1 */
    public float[,] rotationMatrixTo(Position3 p){
      var a1 = array();
      var a2 = p.array();
      var cosTheta = a1.dot(a2);
      var theta = Math.Acos(cosTheta);
      var sinTheta = (float) Math.Sin(theta);
      var c = a1.cross(a2).normalize();
      var A = new float[3, 3]{
        {0,-c[2],c[1]},
        {c[2],0,-c[0]},
        {-c[1],c[0],0}
      };
      var R= new float[3,3]{
        {1,0,0},
        {0,1,0},
        {0,0,1}
      };
      var R2 = A.times(sinTheta);
      var R3 = A.times(A).times(1 - cosTheta);
      R = R.plus(R2).plus(R3);
      return R;
    }

    public float[] array(){
      return new[]{x,y,z};
    }

    public Position3 rotateBy(float[,] R){
      return new Position3(R.times(array()));
    }
  }


}