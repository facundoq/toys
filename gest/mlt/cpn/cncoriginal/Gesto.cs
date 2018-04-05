using System;
using System.IO;
using System.Collections.Generic;

namespace mlt.cpn.cncoriginal{
  public enum RolGesto
  {ParaEntrenar, ParaTesteo}


  public class Gesto
  {
    private RolGesto _rol=RolGesto.ParaTesteo;
    public int Id{get;set;}
    public string Nombre{get;set;}
    public List<Vector> Vectores{get;set;}
    public RolGesto Rol {
      get { return _rol; }
      set { _rol = value; }
    }
    
    
    public Gesto(string nombre, List<Vector> vectores)
    {
      Nombre = nombre;
      Vectores = vectores;
    }
    
    
    /// <summary>
    /// escalado para que el el vector más grande tenga módulo 1
    /// </summary>
    public void Normalizar()
    {
      double moduloMayor=-1;
      foreach(Vector v in Vectores)
      {
        if (v.ModuloDelVector  > moduloMayor)
        {
          moduloMayor = v.ModuloDelVector;
        }
      }
      foreach(Vector v in Vectores)
      {
        v.DividirPor(moduloMayor);
      }
    }
    
    
    
    /// <summary>
    /// escalado para que todos los vectores posean módulo 1
    /// </summary>
    public void NormalizarTodosModulo1()
    {
      foreach(Vector v in Vectores)
      {
        v.NormalizarModulo1();
      }
    }

    
    public void Suavizar(int tamanioVentana)
    {
      List<Vector> result=new List<Vector>();
      for(int i=0;i<=Vectores.Count-tamanioVentana;i++)
      {
        List<Vector> auxi=new List<Vector>();
        for(int j=i;j<i+tamanioVentana;j++)
        {
          auxi.Add(Vectores[j]);
        }
        Vector v=Vector.Promediar(auxi);
        result.Add(v);
      }
      Vectores = result;
    }
    
    public void PreprocesarPegarDosVectores()
    {
      List<Vector>nuevo=new List<Vector>();
        
      for(int i=1;i<Vectores.Count;i++)
      {
        Vector v=new Vector();
        for(int j=i-1; j<=i;j++){
          v.AddRangeCoordenada(Vectores[j]);
        }
        nuevo.Add(v);
      }
      Vectores=nuevo;
    }
    

    
    
    /// <summary>
    /// Cambia los puntos del gesto de n coordenadas por n-1 ángulos
    /// Se pierde información (el módulo del vector)
    /// </summary>
    public void TransformarEnAngulos()
    {
      foreach(Vector v in Vectores)
      {
        v.TransformarEnAngulos();
      }
    }
    
    
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      sw.WriteLine("ID = "+Id+" - Clase = " + Nombre+": ");
      //sw.WriteLine("("+_rol+")");
      foreach(Vector p in Vectores)
      {
        sw.WriteLine(p.ToString());
      }
      return sw.ToString();
    }
    
    public  string ToStringSeparadoPorComas()
    {
      StringWriter sw=new StringWriter();
      sw.Write(Nombre+",0,0,");
      foreach(Vector p in Vectores)
      {
        sw.Write(p.ToStringSeparadoPorComas());
      }
      string st= sw.ToString();
      return st.Substring(0,st.Length -1);
    }
  }
}