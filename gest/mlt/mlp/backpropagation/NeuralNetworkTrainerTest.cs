using System;
using System.Collections.Generic;
using System.Linq;
using MathNet.Numerics.LinearAlgebra.Double;
using NUnit.Framework;
using mlt.classification;
using mlt.experiments;
using mlt.samples;
using utilities;

namespace mlt.mlp.backpropagation{
  internal class NeuralNetworkTrainerTest{

    [Test]
    public void parity(){
      var k = 20;
      var profile = new Profile().snapshot("Start");
      var samples = generateSamples(k);
      var split = samples.split(0.7);
      var training = split.Item1.addBias();
      var tp = new NeuralNetworkTrainerParameters(100, 0.05, 0.01, 0.01, 0.01, 0.01, 0.5d);

      var p = new NeuralNetworkParameters(k+1, 20, samples.classes());
      var trainer = new NeuralNetworkTrainer(tp, p);

      NeuralNetwork network= trainer.train(training);//NetworkSampleGenerator.scale(trainingSamples,new Range(-1,1));
      profile.snapshot("Sample generation");
      C.p("Training:"+split.Item1.evaluateWith(network));
      C.p("Test:"+split.Item2.evaluateWith(network));
      Console.WriteLine( profile.snapshot("Training"));
      
    }

    private List<double[]> generateParity(int length){
      if (length > 0){
        var result = generateParity(length - 1);
        return result.SelectMany(s =>{
          return new List<double[]>(){
            s.ToList().clone().add(0).ToArray(),
            s.ToList().clone().add(1).ToArray()
          };
        }).ToList();
      }
      else{
        return new List<double[]>().add(new double[0]);
      }
    }

    private Dataset generateSamples(int k){
      var odd = new List<double[]>();
      var even = new List<double[]>();
      generateParity(k).ForEach(s =>{
        var parity = s.Aggregate((r, i) => (i == r) ? 1 : 0);
        if (parity == 0){
          even.Add(s);
        }else{
          odd.Add(s);
        }
      });
      List<Pattern> patterns= new List<Pattern>();
      odd.ForEach(p => patterns.add(new Pattern(p,0)));
      even.ForEach(p => patterns.add(new Pattern(p,0)));

      return new Dataset(patterns);
    }

    
  }
}