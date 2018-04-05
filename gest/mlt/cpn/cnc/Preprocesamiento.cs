using System;
using System.IO;
using System.Collections.Generic;

namespace mlt.cpn.cnc{
  public abstract class Preprocesamiento
  {
    public abstract void Preprocesar(List<Gesto> listaDeGestos);
  }


  /// <summary>
  /// Escala a todos los vectores del gesto haciendo que tengan módulo 1
  /// </summary>
  public class PreprocModulo1:Preprocesamiento
  {
    public override void Preprocesar(List<Gesto> listaDeGestos)
    {
      foreach(Gesto g in listaDeGestos)
      {
        g.NormalizarTodosModulo1();
      }
    }
  }


  /// <summary>
  /// Escala los vectores del gesto haciendo que el más grande tenga módulo 1
  /// </summary>
  public class PreprocNormalizar:Preprocesamiento
  {
    public override void Preprocesar(List<Gesto> listaDeGestos)
    {
      foreach(Gesto g in listaDeGestos)
      {
        g.Normalizar();
      }
    }
  }


  /// <summary>
  /// Cambia los puntos de todos los gestos de n coordenadas por n-1 ángulos
  /// Se pierde información (el módulo del vector)
  /// </summary>
  public class PreprocAngulos:Preprocesamiento
  {
    public override void Preprocesar(List<Gesto> listaDeGestos)
    {
      foreach(Gesto g in listaDeGestos)
      {
        g.TransformarEnAngulos();
      }
    }
  }


  /// <summary>
  /// Resta el primer punto a cada uno de los puntos, trasladando así el origen de coordenadas
  /// al primer punto que queda en (0,0,0)
  /// </summary>
  public class PreprocTrasladoOrigenAlPrimerPunto:Preprocesamiento
  {
    public override void Preprocesar(List<Gesto> listaDeGestos)
    {
      foreach(Gesto g in listaDeGestos) {
        Vector primerPunto=g.Vectores[0];
        for(int i=1;i<g.Vectores.Count;i++)
        {
          g.Vectores[i].Restarle(primerPunto);
        }
        primerPunto.Clear();
      }
    }

  }


  public class PreprocFiltroSuavisado:Preprocesamiento
  {
    int _tamanioDeVentana=5;
    public PreprocFiltroSuavisado(int tamanioDeVentana)
    {
      _tamanioDeVentana=tamanioDeVentana;
    }
    public override void Preprocesar(List<Gesto> listaDeGestos)
    {
      foreach(Gesto g in listaDeGestos)
      {
        g.Suavizar(_tamanioDeVentana);
      }
    }
  }

  /// <summary>
  /// Cambia los puntos de todos los gestos de n coordenadas por n-1 ángulos
  /// Se pierde información (el módulo del vector)
  /// </summary>
  public class PreprocDiferenciasDeVectores:Preprocesamiento
  {
    public override void Preprocesar(List<Gesto> listaDeGestos)
    {
      foreach(Gesto g in listaDeGestos)
      {
        for(int i=1;i<g.Vectores.Count;i++)
        {
          g.Vectores[i-1].RestarloDe(g.Vectores[i]);
        }
        g.Vectores.RemoveAt(g.Vectores.Count-1); //borro el último punto
      }
    }
  }
}