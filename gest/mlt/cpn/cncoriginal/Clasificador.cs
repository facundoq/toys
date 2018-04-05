using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;

namespace mlt.cpn.cncoriginal{
  public class Clasificador
  {
    private  List<CPN> CPNs=new List<CPN>();
    private List<LVQ> LVQs = new List<LVQ>();
    private ConjuntoDeGestos _gestos;
    
    public Clasificador(CPNconfig config, ConjuntoDeGestos gestos)
    {
      _gestos = gestos;
      for(int i=1;i<=config.CantidadDeRedesEnElClasificador;i++)
      {
        CPN cpn=new CPN(config);
            
        //Entreno la cpn para obtener los vectores característicos (histograma) de cada gesto
        CPNEntrenador entrenador=new CPNEntrenador(cpn,_gestos);
        List<Histograma> histogramas = entrenador.Entrenar(config.CantPasadasEntrenamiento);
            
        //Instancio la red LVQ basada en los histogramas (cada histograma representa el vector de pesos de una neurona de salida)
        LVQ lvq = new LVQ(histogramas,config.MedidorDeDistancia);
        //Documentador.AddDocu("LVQ-histogramas.txt",lvq.ToString());
        CPNs.Add(cpn);
        LVQs.Add(lvq);
      }
    }
    
    private string _escrutinio(List<string> listaDeStrings)
    {
        
      var query=from st in listaDeStrings
                group st by st;
        
      var query2=from grupo in query
                 orderby grupo.Count() descending
                 select new{Nombre=grupo.Key,Cantidad=grupo.Count()};
        
      if (query2.Count()==1)
      {
        return query2.First().Nombre;
      }
      else
      {
        var arreglo=(from grupo in query
                     orderby grupo.Count() descending
                     select grupo.Count()).ToArray();
        if (arreglo[0] > arreglo[1])
        {
          return query2.First().Nombre;
        }
        else
        {
          return "Sin decisión";
        }
      }
    }
    
    private string _evaluarGesto(Gesto g)
    {
      List<string>ganadores=new List<string>();
      double[] resultadoAcumulado=new double[1]; //no interesa el índice después lo vuelvo a instanciar
      for(int i=0;i<CPNs.Count;i++ )
      {
        CPN cpn=CPNs[i];
        LVQ lvq=LVQs[i];
        double[] vector = cpn.EvaluarYdevolverHistograma(g.Vectores);
        string nombreGanador;
        double[] resultado=lvq.Evaluar(vector,out nombreGanador);
        ganadores.Add(nombreGanador);
        if (i==0)
        {
          resultadoAcumulado = new double[resultado.Length];
        }
        for(int j=0;j<resultado.Length;j++)
        {
          resultadoAcumulado[j]+=resultado[j];
        }
      }
        
        
      //        string ganadorDeLaVotacion=_escrutinio(ganadores);
        
      string ganadorDeLaVotacion= LVQs[0].GetNombreDelGanador(resultadoAcumulado);
        
      string stAuxi=string.Format("ID={0} Clase={1} -> {2} ",g.Id,g.Nombre,ganadorDeLaVotacion );
      if (g.Nombre==ganadorDeLaVotacion)
      {
        Documentador.AgregarContenido(stAuxi+"OK" );
      }
      else
      {
        Documentador.AgregarContenido(stAuxi+"x");
      }
        
      return ganadorDeLaVotacion;
    }
    
    public double Evaluar()
    {
      List<Gesto> conjuntoDePrueba=_gestos.GestosDeTest;
      //conjuntoDePrueba=gestos.ListaDeGestos;
        
      StringWriter sw=new StringWriter();
      int aciertos=0;
      foreach (Gesto g in conjuntoDePrueba )
      {
        string nombreGanador=this._evaluarGesto(g);
        if (g.Nombre==nombreGanador)
        {
          aciertos++;
        }
            
      }
      double porcentajeAcertado=100.0*(double)aciertos/conjuntoDePrueba.Count;
        
      //Console.WriteLine(porcentajeAcertado);
      Documentador.AgregarContenido("------------------------\r\n"+porcentajeAcertado.ToString()+"\r\n");
      return porcentajeAcertado;
    }
  }
}
