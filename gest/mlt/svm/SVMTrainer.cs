using System;
using System.Collections;
using System.Collections.Generic;
using mlt.experiments;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using mlt.classification;

namespace mlt.svm{

  public class TrainingState{
    private readonly int maxIterations;
    public int iterations ;
    public Boolean tryAll;
    public int multipliersAdjusted;
    public TrainingState(int maxIterations){
      this.maxIterations = maxIterations;
      iterations = 0;
       tryAll = true;
       multipliersAdjusted = 0;
    }

    public Boolean finished(){
      return iterations == maxIterations || (multipliersAdjusted == 0 && !tryAll);
    }

    public void iterationFinished() {
      iterations++;
      tryAll = !tryAll;
    }

    public void iterationStarted(){
      multipliersAdjusted = 0;
    }
  }

  public class SVMTrainer{
    public SVMTrainingOptions o;
    private readonly Dataset training;
    private readonly Kernel kernel;
    public double[] a; // lagrange multipliers
    public double b; 
    public double[,] k; // kernel matrix
    public double[] e; //error cache
    public double[] y; // class labels
    public double eps;
  

    public SVMTrainer(SVMTrainingOptions o, Dataset training,Kernel kernel,int klass ){
      var y = training.getOutputs().Select(c => (c == klass) ? 1d : -1d).ToArray();
      this.o = o;
      this.eps = o.eps;
      this.training = training;
      this.kernel = kernel;
      this.k = kernel.matrix(training.getInputs().ToList());
      this.y = y;
      a=new double[y.Length];
      e=new double[y.Length];
      b = 0;
      //Console.WriteLine("errors"+e.toString());
      //Console.WriteLine("k"+k.toString());
    }

    

    public bool bounded(int i){
      return eps < a[i] && a[i] < o.c;
    }
    public SVM train(){
      var s = new TrainingState(o.maxIterations);

      while (!s.finished()){
        s.iterationStarted();
        if (s.tryAll){
           for (var i = 0; i < a.Length; i++){
             s.multipliersAdjusted+=tryToAdjust(i).toInt();
           }
        }else{
          for (var i = 0; i < a.Length; i++) {
            if (o.eps<a[i] && a[i]<o.c){
              s.multipliersAdjusted += tryToAdjust(i).toInt();
            }
          }
        }

        //Console.WriteLine("Finished iteration:" + s.iterations);            
        //Console.WriteLine("Multipliers adjusted:" + s.multipliersAdjusted);            
        
        s.iterationFinished();
      }
     return makeSVM();
    }

    private int maximumStepMultiplier(int i){
      var result = 0;
      for (var j = 1; j < e.Count(); j++){
        double dij=(e[i] - e[j]).abs();
        double dir =(e[i] -e[result]).abs();
        if (dij > dir) {
          result =j;
        }
      }
      return result;
    }

    

    private bool tryToAdjust(int i, int j){
      if (i == j) { return false; }
      var e1 = e[i];
      var a1 = a[i];
      var y1 = y[i];

      e1 = at(i) - y[i];
      
      var a2 = a[j];
      var e2 = e[j];
      e2 = at(j) - y[j];
      var y2 = y[j];
      
      var s = y1*y2;
      var g = a1+s*a2;
      var sg = s*g;
     
      var l = Math.Max(0, sg - s.positive()* o.c);
      var h = Math.Min(o.c, sg + s.negative()* o.c);

      //Console.WriteLine("Adjusting (i,j)=({0:N},{1:N}), y=({2:N},{3:N}),a=({4:N},{5:N}),e=({6:N},{7:N}),(l,h)=({8:N},{9:N})",i,j,y1,y2,a1,a2,e1,e2,l,h);
      if ( (l- h).abs() < eps){
        return false;
      }
      // calculate a2

      var k11 = k[i, i];
      var k22 = k[j, j];
      var k12 = k[i, j];
      var n = 2 * k12 - k11 - k22;
      //Console.WriteLine("n="+n);
      if (n < 0){
        a2 = (a2 + y2*(e2 - e1)/n);
        //Console.WriteLine("n<0 a2:"+a2);
        a2=a2.restrict(l, h);
       // Console.WriteLine("n<0 restricted a2:" + a2);
      }else{
        double c1 = n/2;
        double c2 = y2*(e1 - e2) - n*a[j];
        double wl = c1*l*l + c2*l;
        double wh = c1 * h * h + c2 * h;
        if (wl + eps < wh){
          a2 = h;
        }else  if (wh + eps < wl){
          a2 = l;
        }else{ C.p("Error: n should be non-positive, n:"+n);}
      }
      
      // correct numerical errors
      //Console.WriteLine("final a2:"+a2);
      if (a2 < eps){
        a2 = 0;
      }else if (a2 > o.c - eps){
        a2 = o.c;
      }

      if ((a2 - a[j]).abs() < eps*(eps + a2 + a[j])){
        //Console.WriteLine("da"+(a2-a[j]));
        return false;
      }
      
      a1 = g - s*a2;
      //a1 = a[i] - s*(a2 - a[j]);


      // update b
      
      double da2 = y2 * (a2 - a[j]);
      double da1 = y1 * (a1 - a[i]);
      double b1 = b + e1 + da2 * k12+  da1 * k11 ;
      double b2 = b + e2 + da1 * k12 + da2 * k22;
      //C.pv("a1 a2 da1 da2 b1 b2",a1,a2,da1,da2,b1,b2);
      double bn;
      if (0 < a2 && a2<o.c){
        bn = b2;
      }else if (0 < a1 && a1 < o.c){
        bn = b1;
      }else{
        bn=(b1 + b2)/2;
      }
      var db = bn - b;
      //update b and adjusted multipliers
      b = bn;
      a[j] = a2;
      a[i] = a1;
      // Update the error for each sample
      foreach (int sample in a.indexes()){
        e[sample] += db + da1 * k[sample, i] + da2 * k[sample, j];
      }
      return true;
    }
    private double at(int i){
      double result = -b;
      foreach (int j in a.indexes()){
        result += a[j]*y[j]*k[i, j];
      }
      return result;
    }

    private bool tryToAdjust(int i){
      e[i] =at(i)-y[i];
      var e1 = e[i];
      var a1 = a[i];
      var r1 = y[i]* e1;
      //Console.WriteLine(i+"-> r1="+r1);
      if ((a1 > 0 && r1 > o.tolerance) || (a1 < o.c && r1 < -o.tolerance)) {
        //Console.WriteLine("KKT Violator:"+i);
        if (tryToAdjust(i, maximumStepMultiplier(i))){
          return true;
        }  
        foreach (var j in a.indexes()) {
          if ((o.eps<a[j] && a[j]<o.c) && tryToAdjust(i, j)) {
              return true;
          }      
        }
        foreach(var j in a.indexes()) {
            if (tryToAdjust(i, j)) {
              return true;
            }
        }
      }
      return false;
    }

    private SVM makeSVM(){
      var indices = this.a.Select((d, i) => 0 < d ? i : int.MinValue).Where(i => i != int.MinValue);

      //Console.WriteLine("\n indices:"+indices.ToArray().toString());
      var xs = this.training.getInputs().getAll(indices);
      var a = this.a.getAll(indices).ToArray();
      var y = this.y.getAll(indices).ToArray();
      return new SVM(xs,a,b,y,kernel);
    }

     
    }
}