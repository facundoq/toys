using System.IO;
using System.Linq;
using System.Collections.Generic;

namespace mlt.cpn.cncoriginal{
  public class CPN
  {
    private  Neurona[] _CapaSalida;
    
    public CPN(CPNconfig config)
    {
      _CapaSalida = new Neurona[config.CantNeuronas];
      for(int i=0;i<_CapaSalida.Length;i++)
      {
        _CapaSalida[i]=new Neurona(i,config);
      }
    }

    
    /// <summary>
    /// Evalua la red con el vector InputVector. Las neuronas de la capa de salida actualizarán
    /// su valor de activación. Me devuelve la neurona ganadora (minimiza la distancia con inputVector)
    /// </summary>
    private Neurona _evaluar(double[] inputVector)
    {
      for(int i=0;i<_CapaSalida.Length;i++)
      {
        _CapaSalida[i].Eval(inputVector);
      }
      double minValue = _CapaSalida.Min(x=>x.ValorDeActivacion);
      Neurona neuronaGanadora=_CapaSalida.First(x=>x.ValorDeActivacion==minValue);
      neuronaGanadora.ActualizarEstadisticaPorSerLaGanadora();
      return neuronaGanadora;
    }

    
    
    
    /// <summary>
    /// Resetea la estadística en cada una de las neuronas de la red para comenzar a contar desde cero
    /// </summary>
    private void _resetearEstadisticas()
    {
      foreach(Neurona n in _CapaSalida)
      {
        n.ResetearEstadisticas();
      }
    }
    
    
    private void _getEstadisticas(out int totalEvaluaciones, out int cantidadDeClusters, out double distanciaAcumuladaAlosCentroides )
    {
      totalEvaluaciones = _CapaSalida.Sum(x=>x.VecesGanadas);
      distanciaAcumuladaAlosCentroides = _CapaSalida.Sum(x=>x.SumaAcumuladaDeActivaciones);
      cantidadDeClusters = _CapaSalida.Count(x=>x.VecesGanadas != 0);
    }
    
    
    /// <summary>
    /// Evalúa y Ajusta la neurona ganadora para el inputVector de acuerdo con la regla de aprendizaje
    /// </summary>
    private void _evaluarYajustar(double[] inputVector)
    {
      Neurona n = this._evaluar(inputVector);
      n.AjustarAcercando(inputVector);
    }
    
    
    /// <summary>
    /// Devuelve un vector con tantos elementos como neuronas en la capa de salida
    /// con la información sobre las veces que se activaron cada una de ellas desde la
    /// última vez que se invocó el método _resetearEstadisticas(). Normalizado dividiendo
    /// cada valor por la cantidad total de activaciones. Es decir que puede considerarse
    /// una distribuciónn de probabilidades pues la suma de los elementos del vector dará 1
    /// </summary>
    private double[] _getHistogramaDeSalida()
    {
      var query = from n in _CapaSalida select (double)n.VecesGanadas;
      double suma=query.Sum();
      var query2 = from d in query select d/suma;
      return query2.ToArray();
    }
    
    
    public void entrenarUnaPasadaDePatrones(List<Vector> patrones)
    {
      this._resetearEstadisticas();
      foreach(Vector p in patrones)
      {
        this._evaluarYajustar(p.ToArray());
      }
    }
    
    
    /// <summary>
    /// Evalua la red CPN con todos los patrones pasados como parámetro y devuelve un histograma
    /// que representa la cantidad de veces que se activaron las neuronas de la capa de salida
    /// con ese conjunto de patrones. Como el histograma está normalizado, se corresponde con una
    /// distribución de probabilidad pues la suma de los elem. del vector da 1
    /// </summary>
    public double[] EvaluarYdevolverHistograma(List<Vector> patrones)
    {
      this._resetearEstadisticas();
      foreach(Vector p in patrones)
      {
        this._evaluar(p.ToArray());
      }
      return this._getHistogramaDeSalida();
    }
    
    
    public void EliminarNeuronasQueNuncaGanaron()
    {
      var query= from n in _CapaSalida
                 where n.VecesGanadas!=0
                 select n;
      Neurona[] nuevaCapa = query.ToArray();
      _CapaSalida=nuevaCapa;
    }
    
    
    public string GetEstadisticasToString()
    {
      double sumaDistancia=0;
      int total=0;
      int cantClus=0;
      this._getEstadisticas(out total,out cantClus,out sumaDistancia);
      return string.Format("DATOS ACUMULADOS DE LA ÚLTIMA PASADA DE PATRONES:\r\nCant. evaluaciones: {0}\r\nSuma total de distancias a los centroides: {1}\r\nCantidad de neuronas activadas: {2}",total,sumaDistancia,cantClus);
    }
    
    
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      sw.WriteLine("---------------------------------------------");
      sw.WriteLine(this.GetEstadisticasToString());
      sw.WriteLine("---------------------------------------------");
      sw.WriteLine("{0,3}\t{1,-7}\t{2}","ID","GANADAS","DATOS DEL VECTOR");
      foreach(Neurona neuron in _CapaSalida)
      {
        sw.WriteLine(neuron.ToString());
      }
      return sw.ToString();
    }
  }
}



