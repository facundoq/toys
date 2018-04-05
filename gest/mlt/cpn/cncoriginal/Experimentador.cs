using System;
using System.IO;
using System.Collections.Generic;
using utilities;

namespace mlt.cpn.cncoriginal{
  public class Experimentador
  {

    public static string basePath = "D:\\Dropbox\\Tesina de Facundo Quiroga\\gestures\\samples\\";

    public static void ejecutar(){
      var gestos = new[]{18,14,10,6,3};
      //gestos = new[]{3};
      var redes = new[]{1,3,5,7,10};
       //redes = new[]{10};
      var ocultas = new[]{20,30,50,70};
       ocultas = new[]{30};
      var ns = new[] { 16,32,64,128 };
      ns = new[]{16};
      String result = "cc,g,p,h,n\n";
      for (var g = 0; g < gestos.Length; g++){
        for (var r = 0; r < redes.Length; r++) {
          for (var o = 0; o < ocultas.Length; o++){
            foreach (var n in ns){
            C.p("Ejecutando con " + "g" + gestos[g] + " p:" + redes[r] + " h:" + ocultas[o]+" n:"+n);
            Configurador co = new Configurador();
            CPNconfig config = new CPNconfig();
            config.CantNeuronas = ocultas[0];
            
            //parámetros relativos a los gestos
            co.Gestos_CantUsadaParaEntrenar = gestos[g];
            config.CantidadDeRedesEnElClasificador = redes[r];
            var promedio = ejecutar2(co, config,n);
            result += promedio.toStringTwoDecimals() + "," + gestos[g] + "," + redes[r] + "," + ocultas[o]+","+n+"\n";
            }
          }
        }
      }
      File.WriteAllText(basePath+"cnc-lnhg-u-cc.csv",result);
    }
    public static double ejecutar2(Configurador c, CPNconfig config, int n) {
      c.Gestos_EleccionAleatoriaParaEntrenar = true;
      //c.Gestos_NombreArchivo = "lnhg.csv";
      c.Gestos_NombreArchivo = "lnhg_processed.csv";
      //c.Gestos_NombreArchivo = "lnhg-n" + n + ".csv";
      //c.Gestos_NombreArchivo = basePath + "celebi.csv";
      
      List<Preprocesamiento> preProc = new List<Preprocesamiento>();
      //preProc.Add(new PreprocTrasladoOrigenAlPrimerPunto());
      preProc.Add(new PreprocFiltroSuavisado(5));
      preProc.Add(new PreprocDiferenciasDeVectores());
      //preProc.Add(new PreprocFiltroSuavisado(5));
      //preProc.Add(new PreprocAngulos());
      preProc.Add(new PreprocNormalizar());

      c.Gestos_Preprocesamientos = preProc;

      //parámetros relativos a las CPNs y al clasificador
      config.DimensionVectorDePesos = 3;
      config.MaximoValorVectorDePeso = 0.5;
      config.MinimoValorVectorDePeso = -0.5;
      config.MedidorDeDistancia = new MedidorDistanciaSimple();
      //config.MedidorDeDistancia = new MedidorDistanciaAngulos();
      config.Alfa = 0.1;
      config.CantPasadasEntrenamiento = 10; //Entrenamiento de la CPN
      c.cpnConfig = config;

      //parámetros relativos al Experimentador
      c.Experimentador_CantidadDePruebas = 10;
      c.Experimentador_NombreConjuntoDePruebas = "05";

      Documentador.GuardarTodo = false;
      Experimentador experimentador = new Experimentador(c);
      return experimentador.RealizarPruebas();
    }
    
    private Configurador _configurador;
    private ConjuntoDeGestos _gestos;
    private List<double> _porcentajesAciertos=new List<double>();

    
    
    public Experimentador(Configurador c)
    {
      Documentador.NuevoDocumento(c.Experimentador_NombreConjuntoDePruebas+"_Detalle.txt");
      Documentador.AgregarContenido(c.ToString());
       
      _configurador = c;
      _gestos=new ConjuntoDeGestos(c.Gestos_NombreArchivo,c.Gestos_CantUsadaParaEntrenar,c.Gestos_EleccionAleatoriaParaEntrenar);
      _gestos.Preprocesamientos=c.Gestos_Preprocesamientos;
        
      Documentador.AgregarSeccion("Gestos antes del preprocesamiento",_gestos.ToString());
        
      _gestos.Preprocesar();
        
      Documentador.AgregarSeccion("Gestos luego del preprocesamiento",_gestos.ToString());
    }
    
    public double RealizarPruebas()
    {
        
      for(int i=1;i<=_configurador.Experimentador_CantidadDePruebas;i++)
      {
        Documentador.AgregarSeccion("PRUEBA NRO. "+i,"");
        _gestos.EstablecerGestosDeEntrenamiento(); //debo establecerlos antes de cada prueba para el caso de seleccion aleatoria de los gestos de entrenamiento
        Clasificador clasificador=new Clasificador(_configurador.cpnConfig,_gestos);
        double porcentajeAcertado = clasificador.Evaluar();
        _porcentajesAciertos.Add(porcentajeAcertado);
      }
      var promedio=_documentarResumen();
      Documentador.guardarDocumento();
      return promedio;
    }
    
    private double _documentarResumen()
    {
      StringWriter sw=new StringWriter();
      sw.WriteLine("Aciertos");
      double suma=0;
      foreach(double d in _porcentajesAciertos)
      {
        suma+=d;
        sw.WriteLine("{0:0.00}",d);
      }
      sw.WriteLine("----------");
      double promedio=suma/_porcentajesAciertos.Count;
        
      double desvio=0;
      foreach(double d in _porcentajesAciertos)
      {
        desvio+=(d-promedio)*(d-promedio);
      }
      desvio= desvio / (_porcentajesAciertos.Count-1 );
      desvio = Math.Sqrt(desvio);
      sw.WriteLine("Promedio = {0}   Desvío estándar = {1}",promedio,desvio);
      Console.WriteLine("Promedio = {0}   Desvío estándar = {1}",promedio,desvio);
        
      Documentador.AgregarSeccion("Resultado final de las "+_configurador.Experimentador_CantidadDePruebas+ " evaluaciones", string.Format("Promedio = {0}   Desvío estándar = {1}",promedio,desvio));
       
      Documentador.guardarDocumento(_configurador.Experimentador_NombreConjuntoDePruebas + "_Resumen.txt",sw.ToString());
      return promedio;
    }

    
  }
}


