using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using mlt.classification;
using utilities;

namespace mlt.cpn.cnc{
  public class Clasificador : Classifier{
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
    
    public double[] gestureScore(Gesto g){
      double[] resultadoAcumulado = new double[1]; //no interesa el índice después lo vuelvo a instanciar
      
      for (int i = 0; i < CPNs.Count; i++) {
        CPN cpn = CPNs[i];
        LVQ lvq = LVQs[i];
        double[] vector = cpn.EvaluarYdevolverHistograma(g.Vectores);
        double[] resultado = lvq.Evaluar(vector);
        if (i == 0) {
          resultadoAcumulado = new double[resultado.Length];
        }
        for (int j = 0; j < resultado.Length; j++) {
          resultadoAcumulado[j] += resultado[j];
        }
      }
      return resultadoAcumulado;
    }
    
    

    public double[] classify(double[] input){
      var g = new Gesto("i", Vector.fromInput(input).inList());
      return gestureScore(g);
    }

    public bool correctlyClassified(double[] actualOutput, int expectedClass){
      //C.p(expectedClass+"->"+actualOutput.toString());
      //return actualOutput[expectedClass] <= actualOutput.Where((v, i) => i != expectedClass).Min();    
      return LVQs[0].GetClaseGanadora(actualOutput) == expectedClass;
    }
  }
}
