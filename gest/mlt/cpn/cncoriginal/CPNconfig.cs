using System.IO;

namespace mlt.cpn.cncoriginal{
  public class CPNconfig
  {
    private double _alfa=0.01;
    private double _minimoValorVectorDePeso=-1;
    private double _maximoValorVectorDePeso=1;
    private int _cantPasadasEntrenamiento=50;
    private int _cantidadDeRedesEnElClasificador=3;
    
    public int CantidadDeRedesEnElClasificador {
      get { return _cantidadDeRedesEnElClasificador; }
      set { _cantidadDeRedesEnElClasificador = value; }
    }
    
    
    public int CantPasadasEntrenamiento {
      get { return _cantPasadasEntrenamiento; }
      set { _cantPasadasEntrenamiento = value; }
    }
    
    

    public double Alfa {
      get { return _alfa; }
      set { _alfa = value; }
    }
    
    public int CantNeuronas {get;set;}
    
    public int DimensionVectorDePesos{get;set;}
    
    
    /// <summary>
    /// Especifica el máximo valor en cada uno de los elementos del vector de peso
    /// tenido en cuenta en la inicialización aleatoria de la CPN
    /// su valor por defecto es +1
    /// </summary>
    public double MaximoValorVectorDePeso {
      get { return _maximoValorVectorDePeso; }
      set { _maximoValorVectorDePeso = value; }
    }

    
    /// <summary>
    /// Especifica el mínimo valor en cada uno de los elementos del vector de peso
    /// tenido en cuenta en la inicialización aleatoria de la CPN
    /// su valor por defecto es -1
    /// </summary>
    public double MinimoValorVectorDePeso {
      get { return _minimoValorVectorDePeso; }
      set { _minimoValorVectorDePeso = value; }
    }

    
    public MedidorDistancia MedidorDeDistancia{get;set;}
    
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      sw.WriteLine("Cantidad de redes CPN utilizadas en el clasificador: "+this.CantidadDeRedesEnElClasificador);
      sw.WriteLine("Cantidad de neuronas en la capa competitiva de la CPN: "+this.CantNeuronas);
      sw.WriteLine("Dimensiónn de los vectores de peso: "+this.DimensionVectorDePesos);
      sw.WriteLine(string.Format("Intervalo de inicialización de los vectores de peso: [{0} ; {1}]",this._minimoValorVectorDePeso,this._maximoValorVectorDePeso));
      sw.WriteLine("Medidor de distancia utilizado: "+this.MedidorDeDistancia);
      sw.WriteLine("Velocidad de aprendizaje alfa: "+this.Alfa);
      sw.WriteLine("Cantidad de pasadas de todos los patrones de entrenamiento: "+this.CantPasadasEntrenamiento);
      return sw.ToString();
            
    }
  }
}
