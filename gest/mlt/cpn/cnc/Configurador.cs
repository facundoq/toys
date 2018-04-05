using System.IO;
using System.Collections.Generic;

namespace mlt.cpn.cnc{
  public class Configurador
  {
    public string Gestos_NombreArchivo{get;set;}
    public bool Gestos_EleccionAleatoriaParaEntrenar{get;set;}
    public List<Preprocesamiento> Gestos_Preprocesamientos{get;set;}
    public CPNconfig cpnConfig{get;set;}
    public int Experimentador_CantidadDePruebas{get;set;}
    public string Experimentador_NombreConjuntoDePruebas{get;set;}
    
  }
}
