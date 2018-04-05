/*
 * Creado por SharpDevelop.
 * Usuario: lccorbalan
 * Fecha: 04/07/2013
 * Hora: 9:01
 * 
 * Para cambiar esta plantilla use Herramientas | Opciones | Codificación | Editar Encabezados Estándar
 */

using System;

namespace mlt.cpn.cnc{
  public class RandomGenerator
  {

    private static RandomGenerator _instance = new RandomGenerator();
    
    public static RandomGenerator GetInstance()
    {
      return RandomGenerator._instance;
    }

    Random _rnd = new Random();
   
    private RandomGenerator()
    {
    }

    public double NextDouble()
    {
      return _rnd.NextDouble();
    }
    
    public int Next(int minValue, int maxValue)
    {
      return _rnd.Next(minValue, maxValue + 1);
    }
    
    public double[] GetVector(int count, double inf, double sup)
    {
      double[] v = new double[count];
      for(int i=0;i<count;i++)
      {
        double rango=(sup - inf);
        double valor = _rnd.NextDouble() * rango;
        v[i]= valor + inf;
      }
      return v;
    }
  }
}
