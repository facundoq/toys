using System.IO;

namespace mlt.cpn.cncoriginal{
  public static class Documentador
  {
    
    private static bool _guardarTodo=false;
    
    public static bool GuardarTodo {
      get { return _guardarTodo; }
      set { _guardarTodo = value; }
    }
    
    private static StringWriter _contenido=new StringWriter();
    
    private static string _nombreDocumento;
    
    public static  void NuevoDocumento(string nombreDocumento)
    {
      _nombreDocumento=nombreDocumento;
      _contenido=new StringWriter();
    }
    
    public static void AgregarSeccion(string nombreSeccion,string contenido)
    {
      if (_guardarTodo)
      {
        _contenido.WriteLine("---------------------");
        _contenido.WriteLine(nombreSeccion);
        _contenido.WriteLine("---------------------");
        _contenido.WriteLine(contenido );
      }
    }
    
    public static void AgregarContenido(string st)
    {
      if (_guardarTodo)
      {
        _contenido.WriteLine(st);
      }
    }
    
    /// <summary>
    /// Si Documento.GuardarTodo está establecido en true
    /// guarda el documento corriente que comenzó a escribirse a partir de
    /// la invocación a Documento.NuevoDocumento(string nombreDocumento)
    /// </summary>
    public static void guardarDocumento()
    {
      if (_guardarTodo)
      {
        using(StreamWriter sw=new StreamWriter(_nombreDocumento,false,System.Text.Encoding.Default))
        {
          sw.WriteLine(_contenido);
        }
      }
    }
    
    /// <summary>
    /// Guarda en disco un documento cuyo contenido y nombre se pasan por parametro
    /// </summary>
    public static void guardarDocumento(string nombre, string contenido)
    {
      using(StreamWriter sw=new StreamWriter(nombre,false,System.Text.Encoding.Default))
      {
        sw.WriteLine(contenido);
      }
    }

   
  }
}

