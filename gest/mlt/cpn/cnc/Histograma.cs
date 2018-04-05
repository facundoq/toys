/*
 * Creado por SharpDevelop.
 * Usuario: Administrador
 * Fecha: 07/07/2013
 * Hora: 15:28
 * 
 * Para cambiar esta plantilla use Herramientas | Opciones | Codificación | Editar Encabezados Estándar
 */

using System.IO;

namespace mlt.cpn.cnc{
  public class Histograma
  {
    public string clase{get;set;}
    public double[] VectorCaracteristico {get;set;}
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      sw.Write(clase+":\t");
      foreach(double d in VectorCaracteristico)
      {
        sw.Write(d+" \t");
      }
      return sw.ToString();
    }
  }
}
