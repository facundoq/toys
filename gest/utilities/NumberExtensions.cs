using System;
using System.Collections.Generic;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace utilities {

  public static class C{
    public static void pv(String variableNames, params Object[] variables){
      String[] names = variableNames.Split(" ".ToCharArray());
      Console.WriteLine(",".join(variables.Select((v, i) => names[i] + "=" + v)));
    }
    public static void p(string s) {
      Console.WriteLine(s);
    }
    public static void p(string s,params Object[] variables){
     Console.WriteLine(s,variables);
    }
    public static void p( params Object[] variables) {
      Console.WriteLine(variables);
    }
  }
  public static class NumberExtensions {

    public static int restrict(this int d, int a, int b) {
      if (d < a) {
        d = a;
      }
      if (d > b) {
        d = b;
      }
      return d;
    }

    public static double restrict(this double d, double a, double b) {
      if (d < a){
        d = a;
      }
      if (d>b){
        d = b;
      }
      return d;
    }
    public static double positive(this double a) {
      return (a > 0) ? 1 : 0;
    }
    public static double negative(this double a) {
      return (a < 0) ? 1 : 0;
    }


    public static string toStringTwoDecimals(this double a) {
      return a.ToString("0.00");
    }

    public static int positive(this int a) {
      return (a>0)?1:0;
    }
    public static int negative(this int a) {
      return (a < 0) ? 1 : 0;
    }
  }
}
