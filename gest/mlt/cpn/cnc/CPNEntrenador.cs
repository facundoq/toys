/*
 * Creado por SharpDevelop.
 * Usuario: Administrador
 * Fecha: 07/07/2013
 * Hora: 15:28
 * 
 * Para cambiar esta plantilla use Herramientas | Opciones | Codificación | Editar Encabezados Estándar
 */

using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace mlt.cpn.cnc{
  public class CPNEntrenador
  {
    private ConjuntoDeGestos _conjuntoDeGestos;
    private CPN _cpn;
    
    public CPNEntrenador(CPN cpn, ConjuntoDeGestos conjuntoDeGestos)
    {
      _conjuntoDeGestos = conjuntoDeGestos;
      _cpn=cpn;
    }
    

    /// <summary>
    /// Devuelve un histograma por clase de gesto, cada histograma para una clase está compuesto
    /// por la salida de la CPN luego de exitar la red con todos los patrones de
    /// todos los gestos de entrenamiento para esa clase
    /// </summary>
    /// <returns></returns>
    private List<Histograma> _getHistogramas3()
    {
      List<Histograma> conjuntoDeHistogramas=new List<Histograma>();
      List<Gesto> gestosEntrenamiento = _conjuntoDeGestos.GestosDeEntrenamiento;

      var query=from g in gestosEntrenamiento
                group g by g.Nombre;

      List<Vector> patrones=new List<Vector>();
      foreach(var grupo in query)
      {
        string nombre=grupo.Key;
        patrones=new List<Vector>();
        foreach(var g in grupo)
        {
          patrones.AddRange(g.Vectores);
        }
        double[] vector=_cpn.EvaluarYdevolverHistograma(patrones);
        Histograma histo=new Histograma(){clase=nombre,VectorCaracteristico = vector};
        conjuntoDeHistogramas.Add(histo);
      }
      return conjuntoDeHistogramas;
    }
    
    
    /// <summary>
    /// Devuelve un histograma por cada clase como el promedio de los histogramas producidos por cada
    /// gesto de la misma clase
    /// </summary>
    /// <returns></returns>
    private List<Histograma> _getHistogramas2()
    {
      List<Histograma> conjuntoDeHistogramas=new List<Histograma>();
      List<Gesto> gestosEntrenamiento = _conjuntoDeGestos.GestosDeEntrenamiento;

      var query=from g in gestosEntrenamiento
                group g by g.Nombre;

      List<Vector> patrones=new List<Vector>();
      foreach(var grupo in query)
      {
        string nombre=grupo.Key;
        double[] promedio=new double[1]; //no importa la dimensión, despúes lo vuelvo a instanciar
        int cont=0;
        foreach(var g in grupo)
        {
          cont++;
          double[] vector=_cpn.EvaluarYdevolverHistograma(g.Vectores);
          if (cont == 1)
          {
            promedio = vector;
          }
          else
          {
            for(int i=0;i<promedio.Length;i++)
            {
              promedio[i]= (promedio[i]*(cont -1) + vector[i])/cont;
            }
          }
        }
        Histograma histo=new Histograma(){clase=nombre,VectorCaracteristico = promedio};
        conjuntoDeHistogramas.Add(histo);
      }
      return conjuntoDeHistogramas;
    }
    
    /// <summary>
    /// Devuelve un histograma por cada uno de los gestos de entrenamiento utilizados
    /// </summary>
    /// <returns></returns>
    private List<Histograma> _getHistogramas1()
    {
      List<Histograma> conjuntoDeHistogramas=new List<Histograma>();
      List<Gesto> gestosEntrenamiento = _conjuntoDeGestos.GestosDeEntrenamiento;
      foreach (Gesto g in gestosEntrenamiento)
      {
        double[] vector=_cpn.EvaluarYdevolverHistograma(g.Vectores);
        Histograma histo=new Histograma(){clase=g.Nombre,VectorCaracteristico = vector};
        conjuntoDeHistogramas.Add(histo);
      }
      return conjuntoDeHistogramas;
    }

    
    
    public List<Histograma> Entrenar(int veces)
    {
      List<Vector> patrones=_conjuntoDeGestos.GetPatronesParaEntrenar();
      for(int i=1;i<=veces;i++)
      {
        _cpn.entrenarUnaPasadaDePatrones(patrones);
      }
      //_cpn.EliminarNeuronasQueNuncaGanaron();
      List<Histograma> result = this._getHistogramas1();
      //       result.AddRange(this._getHistogramas3());
//        result.AddRange(this._getHistogramas2());

      return result;
    }
    
  }
}

