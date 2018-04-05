using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using mlt.classification;

namespace mlt.cpn.cnc{
  public class Vector
  {
    public List<double> _coordenadas=new List<double>();
    
    public Vector copiarConAlgoDeRuido()
    {
      RandomGenerator rn=RandomGenerator.GetInstance();
      Vector result=new Vector();
      foreach(double d in this._coordenadas)
      {
        double valor=d+rn.NextDouble()*0.1;
        result.AddCoordenada(valor);
      }
      return result;
    }
    
    
    public static Vector Promediar(List<Vector> vectores)
    {
      Vector result = vectores[0].GetCopia(); ;
      for(int i=1;i<vectores.Count;i++)
      {
        result.Sumarle(vectores[i]);
      }
      //TODO: Revisar esto, anda mejor si no divido, es decir si devuelvo la suma en lugar del promedio
      result.DividirPor((vectores.Count));
      //        result.DividirPor((0.001));
      return result;
    }
    
    public Vector GetCopia()
    {
      Vector result = new Vector();
      foreach(double d in this._coordenadas)
      {
        result.AddCoordenada(d);
      }
      return result;
    }
    
    public void Sumarle(Vector v)
    {
      for (int i=0;i<v.Length;i++)
      {
        this._coordenadas[i]+=v._coordenadas[i];
      }
    }
    
    public double[] ToArray()
    {
      return _coordenadas.ToArray();
    }
    
    public int Length
    {
      get
      {
        return _coordenadas.Count;
      }
    }

    public double ModuloDelVector
    {
      get
      {
        double sum=0;
        for(int i=0;i<_coordenadas.Count;i++)
        {
          sum+=_coordenadas[i]*_coordenadas[i];
        }
        return Math.Sqrt(sum);
      }
    }
    
    public void NormalizarModulo1()
    {
      this.DividirPor(this.ModuloDelVector);
    }
    
    public void AddCoordenada(double valor)
    {
      _coordenadas.Add(valor);
    }
    
    public void AddRangeCoordenada(Vector v)
    {
      foreach(double d in v._coordenadas)
      {
        _coordenadas.Add(d);
      }
    }
    
    public void AddCoordenada(string valor)
    {
      double val=double.Parse(valor.Replace('.',','));
      _coordenadas.Add(val);
    }
    
    public void DividirPor(double d)
    {
      for(int i = 0; i< _coordenadas.Count; i++)
      {
        _coordenadas[i] = _coordenadas[i]/d;
      }
    }
    
    public void Restarle(Vector p)
    {
      for(int i = 0; i< _coordenadas.Count; i++)
      {
        _coordenadas[i] -= p._coordenadas[i];
      }
    }
    
    public void RestarloDe(Vector p)
    {
      for(int i = 0; i< _coordenadas.Count; i++)
      {
        _coordenadas[i] = (p._coordenadas[i] - _coordenadas[i]);
      }
    }
    
    public void Clear()
    {
      for(int i=0;i<_coordenadas.Count;i++)
      {
        _coordenadas[i]=0;
      }
    }
    
    /// <summary>
    /// Cambia el punto de n coordenadas por n-1 ángulos
    /// Se pierde información del módulo del vector
    /// </summary>
    public void TransformarEnAngulos()
    {
      List<double> angulos=new List<double>();
      for(int i=1;i<_coordenadas.Count;i++)
      {
        double x =_coordenadas[i-1];
        double y =_coordenadas[i];
        angulos.Add(this._anguloEntreVectores2D(x,y));
      }
      _coordenadas=angulos;
        
    }
    
    private double _anguloEntreVectores2D(double x, double y)
    {
      double hip=Math.Sqrt(x*x+y*y);
      double result=0;
      if (hip==0)
      {
        return 0; // en realidad no hay ángulo pero tengo que devolver algo
      }
      else
      {
        result = Math.Acos(x/hip);
      }
      if (y<0) //esto es porque el Acos me dá solo valores positivos entre 0 y pi, por eso no distingue cuando el ángulo es negativo, es decir (0,1) da 1/4 de pi, pero también (0,-1) dá 1/4 de pi
      {
        result = -result;
      }
      return result;
    }
    
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      foreach(double d in _coordenadas)
      {
        sw.Write(d+"\t");
      }
      return sw.ToString();
    }
    
    public  string ToStringSeparadoPorComas()
    {
      StringWriter sw=new StringWriter();
      foreach(double d in _coordenadas)
      {
        string st=d.ToString();
        st=st.Replace(",",".");
        sw.Write(st+",");
      }
      return sw.ToString();
    }
    
    public static Vector fromPattern(Pattern p){
      return fromInput(p.x);
    }

    public static Vector fromInput(double[] input) {
      var v = new Vector();
      v._coordenadas = input.ToList();
      return v;
    }
  }
}
