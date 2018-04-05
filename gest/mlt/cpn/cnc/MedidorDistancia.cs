using System;

namespace mlt.cpn.cnc{
  /// <summary>
  /// Clase abstracta base de los distintos medidores de distancias utilizados
  /// </summary>
  public abstract class MedidorDistancia
  {
    abstract public double Calculate(double[] x, double[] y);
  }


  public class MedidorDistanciaSimple:MedidorDistancia
  {
    
    public override double Calculate(double[] x, double[] y)
    {
      double sum=0;
      for(int i=0;i<x.Length;i++)
      {
        sum+=Math.Abs(x[i]-y[i]);
      }
      return sum;
    }
  }

  public class MedidorDistanciaEuclidea:MedidorDistancia
  {
    public override double Calculate(double[] x, double[] y)
    {
      double sum=0;
      for(int i=0;i<x.Length;i++){
        sum=sum+((x[i]-y[i])*(x[i]-y[i]));
      }
      return Math.Sqrt(sum);
    }
  }


  public class MedidorDistanciaAngulos:MedidorDistancia
  {
    public override double Calculate(double[] x, double[] y)
    {
        
      double sum=0;
      for(int i=0;i<x.Length;i++)
      {
        sum+=diferencia(x[i],y[i]);
      }
      return sum;

    }
    
    private double diferencia(double alfa, double beta)
    {
      double result=0;
      if (alfa > beta)
      {
        result = alfa - beta;   
      }
      else
      {
        result = beta - alfa;    
      }
      if (result > Math.PI) //si el resultado es mayor que pi el ángulo en realidad es el complemento a 360
      {
        result = 2*Math.PI - result;
      }
      return result; 
    }
  }
}