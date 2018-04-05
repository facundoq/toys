using System.IO;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using utilities;

namespace mlt.cpn.cnc{
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
    
    public double[] Evaluar(double[] inputVector)
    {
      double[] resultado=new double[_histogramas.Count];
      for(int i=0;i<_histogramas.Count;i++)
      {
        resultado[i] = _medidor.Calculate(_histogramas[i].VectorCaracteristico,inputVector);
      }
      return resultado;
    }

    
    public int GetClaseGanadora(double[] resultado)
    {
      return int.Parse(_histogramas[resultado.MinIndex()].clase);        
    }
    
    
  }
}
