using System.IO;
using System.Collections.Generic;
using System.Linq;
using utilities;

namespace mlt.cpn.cncoriginal{
  public class ConjuntoDeGestos
  {
    private List<Gesto> _gestos = new List<Gesto>();
    private int _cantParaEntrenar=3;
    private bool _eleccionAleatoriaParaEntrenamiento=false;
    
    public List<Preprocesamiento> Preprocesamientos{get;set;}
       

    private Gesto _LeerGestoDesdeString(string st)
    {
      string[] arreglo=st.Split(',');
      string nombre=arreglo[0];
      List<Vector> vectores = new List<Vector>();
        
        
      Vector v=new Vector();
      int i=3;
      while (i<arreglo.Length)
      {
        if ((i % 3) == 0)
        {
          v=new Vector();
          vectores.Add(v);
        }
        v.AddCoordenada(arreglo[i]);
        i++;
      }

      return  new Gesto(nombre,vectores);
    }
    
    
    public List<Gesto> GestosDeEntrenamiento
    {
      get
      {
        var query=from x in _gestos
                  where x.Rol==RolGesto.ParaEntrenar
                  orderby x.Nombre
                  select x;
        return query.ToList();
      }
    }
    
    public List<Gesto> ListaDeGestos
    {
      get
      {
        var query=from x in _gestos
                  orderby x.Nombre
                  select x;
        return query.ToList();
      }
    }
    
    public List<Gesto> GestosDeTest
    {
      get
      {
        var query=from x in _gestos
                  where x.Rol== RolGesto.ParaTesteo
                  orderby x.Nombre
                  select x;
        return query.ToList();
      }
    }
    
    
    /// <summary>
    /// Carga los gestos desde el archivo cuyo nombre se pasa como primer parámetro
    /// Por cada tipo de gesto leido, se reservan para entrenar la cantidad pasada
    /// como segundo parámetro, el resto se utiliza para Test.
    /// </summary>
    public ConjuntoDeGestos(string nombreArchivo,int cantParaEntrenar,bool eleccionAleatoriaParaEntrenamiento)
    {
      this._cantParaEntrenar = cantParaEntrenar;
      this._eleccionAleatoriaParaEntrenamiento=eleccionAleatoriaParaEntrenamiento;
      string st;
      using(StreamReader sr=new StreamReader(nombreArchivo))
      {
        int id=0;
        while (!sr.EndOfStream)
        {
          st=sr.ReadLine();
          id++;
          if (st.Length > 0)
          {
            Gesto g=_LeerGestoDesdeString(st);
            g.Id=id;
            _gestos.Add(g);
          }
        }
      }
    }
    

    public void EstablecerGestosDeEntrenamiento()
    {
        
      var query=from g in _gestos
                group g by g.Nombre;
        
      foreach(var grupo in query)
      {
        List<Gesto> listaAuxi=grupo.ToList();
        if (_eleccionAleatoriaParaEntrenamiento)
        {
          Permutador.Permutar<Gesto>(listaAuxi);
        }
        for(int i=0;i<listaAuxi.Count;i++ )
        {
          if (i < _cantParaEntrenar)
          {
            listaAuxi[i].Rol= RolGesto.ParaEntrenar;
          }
          else
          {
            listaAuxi[i].Rol= RolGesto.ParaTesteo;
          }
        }
      }
    }
    
    
    public void Preprocesar()
    {
      foreach(Preprocesamiento p in this.Preprocesamientos)
      {
        p.Preprocesar(this._gestos);
      }
    }
    
    
    /// <summary>
    /// Devuleve una lista de puntos para entrenamiento, extraídos de los
    /// gestos marcados para ello. (Los devuelve en orden aleatorio)
    /// </summary>
    public List<Vector> GetPatronesParaEntrenar()
    {
      List<Vector> resultado=new List<Vector>();
      foreach(Gesto g in _gestos)
      {
        if (g.Rol==RolGesto.ParaEntrenar)
        {
          resultado.AddRange(g.Vectores);
        }
      }
        
      Permutador.Permutar<Vector>(resultado);
//        
//        //TODO:usar la clase Permutador en lugar de hacerlo a mano
//        //desordeno para devolver los patrones en orden aleatorio
//        RandomGenerator rg=RandomGenerator.GetInstance();
//        for(int i=0;i<resultado.Count;i++)
//        {
//            int indice1=rg.Next(0, resultado.Count - 1);
//            int indice2=rg.Next(0, resultado.Count - 1);
//            Vector p=resultado[indice1];
//            resultado[indice1]=resultado[indice2];
//            resultado[indice2]=p;
//        }

      return resultado;
    }
    
    
    
    public override string ToString()
    {
      StringWriter sw=new StringWriter();
      foreach(Gesto g in _gestos)
      {
        sw.WriteLine(g.ToString());
      }
      return sw.ToString();
    }
    
    public string ToStringSeparadoPorComas()
    {
      StringWriter sw=new StringWriter();
      foreach(Gesto g in _gestos)
      {
        sw.WriteLine(g.ToStringSeparadoPorComas());
      }
      return sw.ToString();
    }
    
    
    
    
    
    /*
    
    public void PreProcesarTrasladarOrigenAlPrimerPunto()
    {
        foreach(Gesto g in _gestos) {
            Vector primerPunto=g.Vectores[0];
            for(int i=1;i<g.Vectores.Count;i++)
            {
                g.Vectores[i].Restarle(primerPunto);
            }
            primerPunto.Clear();
        }
    }
    
    public void PreprocesarFiltroSuavizado(int tamanioVentana)
    {
        foreach(Gesto g in _gestos) {
            g.Suavizar(tamanioVentana);
        }
    }
    
    public void PreProcesarVectoresDiferencia()
    {
        foreach(Gesto g in _gestos) {
            for(int i=1;i<g.Vectores.Count;i++)
            {
                g.Vectores[i-1].RestarloDe(g.Vectores[i]);
            }
            g.Vectores.RemoveAt(g.Vectores.Count-1); //borro el último punto
        }
    }
    
    
    /// <summary>
    /// Escala los vectores del gesto haciendo que el más grande tenga módulo 1
    /// </summary>
    public void PreProcesarNormalizar()
    {
        foreach(Gesto g in _gestos) {
            g.Normalizar();
        }
    }

    
    /// <summary>
    /// Escala a todos los vectores del gesto haciendo que tengan módulo 1
    /// </summary>
    public void PreProcesarNormalizarModulo1()
    {
        foreach(Gesto g in _gestos) {
            g.NormalizarTodosModulo1();
        }
    }
    
       
    /// <summary>
    /// Cambia los puntos de todos los gestos de n coordenadas por n-1 ángulos
    /// Se pierde información (el módulo del vector)
    /// </summary>
    public void PreProcesarTransformarEnAngulos()
    {
        foreach(Gesto g in _gestos) {
            g.TransformarEnAngulos();
        }
    }

    
  */
      
    
  }
}

