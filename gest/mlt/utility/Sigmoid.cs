using System;
using MathNet.Numerics.LinearAlgebra.Double;

namespace mlt.utility{
  internal class Sigmoid{
    static Sigmoid(){
    }

    public static double sigmoid(double x){
      return MathNet.Numerics.SpecialFunctions.Logistic(x);
      //double e = Math.Exp(x);
      //return e/(1.0d + e);
    }

    public static double tanh(double d){
      double e = Math.Exp(2*-d);
      return (e - 1)/(e + 1);
    }

    //public static void a(DenseVector x) {
    //    for (int i = 0; i < x.Count; i++) {
    //        x[i] = 1.7159 * Sigmoid.tanh((2 / 3) * x[i]);
    //    }
    //}
    //public static void da(DenseVector x) {
    //    for (int i = 0; i < x.Count; i++) {
    //        x[i] = 1.14393 * (1 - Sigmoid.tanh(2 / 3 * x[i]));
    //    }
    //}

    public static void a(DenseVector x){
      for (int i = 0; i < x.Count; i++){
        x[i] = sigmoid(x[i]);
      }
    }

    public static void da(DenseVector x){
      for (int i = 0; i < x.Count; i++){
        double t = sigmoid(x[i]);
        ;
        x[i] = t - (t*t);
      }
    }

    // TANH
    //public static void a(DenseVector x) {
    //    for (int i = 0; i < x.Count; i++) {
    //        x[i] = Sigmoid.tanh(x[i]);
    //    }
    //}
    //public static void da(DenseVector x) {
    //    for (int i = 0; i < x.Count; i++){
    //        double t = Sigmoid.tanh(x[i]);
    //        x[i] = 1- (t*t);
    //    }
    //}

    public static double cosh(double d){
      return Math.Exp(d) + Math.Exp(-d);
    }

    //public void a(DenseVector x) {
    //    for (int i = 0; i < x.Count; i++) {
    //        x[i] = Sigmoid.at(x[i]);
    //    }
    //}
  }
}