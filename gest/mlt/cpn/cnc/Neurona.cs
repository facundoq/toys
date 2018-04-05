using System.IO;

namespace mlt.cpn.cnc{
  public class Neurona
  {
    private CPNconfig _config;
    private double[] _vectorDePesos;
    private int _id;
    private int _vecesGanadas=0;
    private double _SumaAcumuladaDeActivaciones=0;
    private double _valorDeActivacion=double.MaxValue;
    
    public double ValorDeActivacion
    {
      get
      {
        return _valorDeActivacion;
      }
    }
    
    
    public int Id
    {
      get
      {
        return _id;
      }
    }
    
    
    public int VecesGanadas {
      get { return _vecesGanadas; }
    }
    
    
    /// <summary>
    /// Devuelve la suma acumulada de las distancias a los patrones para los
    /// cuales la neurona resultó ganadora
    /// </summary>
    public double SumaAcumuladaDeActivaciones {
      get { return _SumaAcumuladaDeActivaciones; }
    }

    
    public void ActualizarEstadisticaPorSerLaGanadora()
    {
      _SumaAcumuladaDeActivaciones += _valorDeActivacion;
      _vecesGanadas++;
    }
    
    
    public void ResetearEstadisticas()
    {
      _vecesGanadas=0;
      _SumaAcumuladaDeActivaciones=0;
    }
    
    
    public Neurona(int id, CPNconfig config)
    {
      _id = id;
      _config = config;
      RandomGenerator rg= RandomGenerator.GetInstance();
      _vectorDePesos = rg.GetVector(_config.DimensionVectorDePesos,_config.MinimoValorVectorDePeso,_config.MaximoValorVectorDePeso);
    }
    
    
    public void Eval(double[] inputVector)
    {
      _valorDeActivacion = _config.MedidorDeDistancia.Calculate(this._vectorDePesos,inputVector);
    }
    
    
    
    /// <summary>
    /// ajusta el peso de la neurona acercándolo levemente al vector inputVector
    /// </summary>
    public void AjustarAcercando(double[] inputVector)
    {
      for(int i=0;i<_vectorDePesos.Length;i++)
      {
        _vectorDePesos[i] = _vectorDePesos[i] + _config.Alfa * (inputVector[i] - _vectorDePesos[i]);
      }
    }
    
    
    /// <summary>
    /// ajusta el peso de la neurona alejándolo levemente del vector inputVector.
    /// Se usa en el entrenamiento supervisado LVQ
    /// </summary>
    public void AjustarAlejando(double[] inputVector)
    {
      for(int i=0;i<_vectorDePesos.Length;i++)
      {
        _vectorDePesos[i] = _vectorDePesos[i] - _config.Alfa * (inputVector[i] - _vectorDePesos[i]);
      }
    }

    
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      sw.Write(string.Format("{0,3}\t{1,-7}\t",_id,_vecesGanadas));
      foreach(double d in _vectorDePesos)
      {
        sw.Write(d+"\t");
      }
      return sw.ToString();
    }
  }
}
