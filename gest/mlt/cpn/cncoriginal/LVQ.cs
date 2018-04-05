using System.IO;
using System.Collections.Generic;

namespace mlt.cpn.cncoriginal{
  public class LVQ
  {
    // la lista de histogramas representa la capa de salida
    List<Histograma> _histogramas;
    MedidorDistancia _medidor;
    
    public LVQ(List<Histograma> histogramas,MedidorDistancia medidor)
    {
      _histogramas=histogramas;
      _medidor = medidor;
    }
    
    public double[] Evaluar(double[] inputVector,out string NombreGanador)
    {
      double[] resultado=new double[_histogramas.Count];
      for(int i=0;i<_histogramas.Count;i++)
      {
        resultado[i] = _medidor.Calculate(_histogramas[i].VectorCaracteristico,inputVector);
      }
      NombreGanador=GetNombreDelGanador(resultado);
      return resultado;
    }

    
    public string GetNombreDelGanador(double[] resultado)
    {
      //determino nombre del ganador
      int indiceMin=0;
      double Min=resultado[0];
      for(int i=1;i<resultado.Length;i++)
      {
        if (resultado[i]<Min)
        {
          indiceMin=i;
          Min=resultado[i];
        }
      }
      return _histogramas[indiceMin].clase;        
    }
    
    
    
    
    
    
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      foreach(Histograma h in _histogramas)
      {
        sw.Write(h.clase+"\t");
        foreach(double d in h.VectorCaracteristico){
          sw.Write(d+"\t");
        }
        sw.WriteLine();
      }
      return sw.ToString();
    }
    
  }
}
