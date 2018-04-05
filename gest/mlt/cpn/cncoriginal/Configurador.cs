using System.IO;
using System.Collections.Generic;

namespace mlt.cpn.cncoriginal{
  public class Configurador
  {
    public string Gestos_NombreArchivo{get;set;}
    public int Gestos_CantUsadaParaEntrenar{get;set;}
    public bool Gestos_EleccionAleatoriaParaEntrenar{get;set;}
    public List<Preprocesamiento> Gestos_Preprocesamientos{get;set;}
    public CPNconfig cpnConfig{get;set;}
    public int Experimentador_CantidadDePruebas{get;set;}
    public string Experimentador_NombreConjuntoDePruebas{get;set;}
    
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      sw.WriteLine("clase del conjunto de pruebas: "+this.Experimentador_NombreConjuntoDePruebas);
      sw.WriteLine("Cantidad de pruebas: "+this.Experimentador_CantidadDePruebas);
      sw.WriteLine("\r\n------  Sobre los Gestos  -------");
      sw.WriteLine("clase del archivo con los gestos: "+this.Gestos_NombreArchivo);
      sw.WriteLine("Cantidad de gestos de entrenamiento: "+this.Gestos_CantUsadaParaEntrenar);
      sw.WriteLine("Eleccion aleatoria de los gestos de entrenamiento: "+this.Gestos_EleccionAleatoriaParaEntrenar);
      sw.WriteLine("Preprocesamiento realizado sobre los gestos (listados en orden): ");
      for(int i=0;i<Gestos_Preprocesamientos.Count;i++)
      {
        sw.WriteLine(string.Format("\t {0}) {1} ",i+1,Gestos_Preprocesamientos[i]));
      }
      sw.WriteLine("\r\n------  Sobre el clasificador  -------");
      sw.WriteLine(cpnConfig);
      return sw.ToString();
    }
    
  }
}
