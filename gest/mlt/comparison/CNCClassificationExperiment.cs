using System;
using System.Collections.Generic;
using mlt.classification;
using mlt.cpn.cnc;
using mlt.data;
using mlt.experiments;
using mlt.mlp;
using mlt.mlp.backpropagation;
using mlt.samples;
using mlt.svm;
using utilities;

namespace mlt.comparison{
  public class CNCExperimentParameters : BaseParameters {
    public double n1, n2, u1, u2, error;
    public int hidden, batches;
  }

  public class CNCClassificationExperiment : ClassificationExperiment<CNCExperimentParameters, GestureProcessingOptions> {
    public CNCClassificationExperiment(IList<CNCExperimentParameters> specs, ClassificationExperimentOptions options)
      : base(specs, options){
    }

    public CNCClassificationExperiment(ClassificationExperimentOptions co) : this(productionSpec(),co){
    }

    public static List<CNCExperimentParameters> productionSpec() {
      return new Specs<CNCExperimentParameters>()
        .add("n1", 0.1)
        .add("n2", 0.5)
        .add("u1", 0.5)
        .add("u2", 0.1)
        .add("error", 0.05)
        .add("hidden", 50)
        .add("batches", 100)
        .generate();
    }

    public override Classifier experiment(CNCExperimentParameters ep, Dataset sample, GestureProcessingOptions options) {
      
      Configurador c = new Configurador();

      //parámetros relativos a los gestos
      c.Gestos_EleccionAleatoriaParaEntrenar = true;
      c.Gestos_NombreArchivo = "";
      c.Experimentador_CantidadDePruebas = 1;

      CPNconfig config = new CPNconfig();
      config.CantNeuronas = 50;
      config.DimensionVectorDePesos = 3;
      config.MaximoValorVectorDePeso = 0.5;
      config.MinimoValorVectorDePeso = -0.5;
      config.MedidorDeDistancia = new MedidorDistanciaSimple();
      config.Alfa = 0.01;
      config.CantPasadasEntrenamiento = 3; //Entrenamiento de la CPN
      config.CantidadDeRedesEnElClasificador = 10;
      

      var gestos = new ConjuntoDeGestos(sample,sample.patterns.Count,true);
      gestos.EstablecerGestosDeEntrenamiento();
      Clasificador clasificador = new Clasificador(config, gestos);
      
      return clasificador;
    }
  }
}