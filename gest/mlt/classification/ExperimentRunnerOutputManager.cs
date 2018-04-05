using System;
using System.Collections.Generic;
using System.IO;
using mlt.experiments;
using utilities;

namespace mlt.classification{

  public class ExperimentOutput {
    private StreamWriter resultFile;
    private ExperimentRunnerOutputManager manager;
    private bool firstTime = true;
    public ExperimentOutput(string path, ExperimentRunnerOutputManager manager){
      
      this.manager = manager;
      resultFile = File.AppendText(path);
    }

    public void p(string s) {
      resultFile.WriteLine(s);
    }
    public void close() {
      resultFile.Close();
    }

    public void print( Report datasetGeneratorOptions, Report spec, MeanAndVarianceCalculator training, MeanAndVarianceCalculator test, int specIndex, List<Report> experimentSpecs, int repetitions, double percentTest) {
            if (firstTime){
              firstTime = false;
              printHeaders(spec, datasetGeneratorOptions);
            }
            var trainingResult = training.calculate();
            var testResult = test.calculate();
            var result = "\n" + specIndex + "/" + experimentSpecs.Count + ", " + repetitions + " repetitions"+", "+percentTest+" percentTest";
            result += "\nTr: " + trainingResult + ". Te: " + testResult;
            result += "\nSpec: " + spec.report();
            manager.p(result);
            string s = percentTest+","+ datasetGeneratorOptions.csv()+","+spec.csv()
              + "," + trainingResult.u.toStringTwoDecimals() + "," + trainingResult.sd.toStringTwoDecimals()
              + "," + testResult.u.toStringTwoDecimals() + "," + testResult.sd.toStringTwoDecimals();
            resultFile.WriteLine(s);
    }

    private void printHeaders(Report spec, Report datasetGeneratorOptions){
      string header = "percentTest,"+datasetGeneratorOptions.header() + "," + spec.header();
      header += ",Tr,Tr(sd),Te,Te(sd)";
        resultFile.WriteLine(header);
    }
  }

  public interface Report {
    String report();
    String csv();
    String header();
  }

  public class ExperimentRunnerOutputManager{
    private readonly bool console;
    private readonly bool file;
    private string timestamp;
    private StreamWriter logFile;
    private const string resultDirectoryBasePath = "results";
    private Dictionary<Tuple<object, string>, ExperimentOutput> outputs;
    private string directory;

    public ExperimentRunnerOutputManager(bool console, bool file){
      this.console = console;
      this.file = file;
      var day = DateTime.Now.ToString("yyyy-MM-dd");
      directory = resultDirectoryBasePath + "\\" + day;
      Directory.CreateDirectory(directory);
      timestamp = DateTime.Now.ToString("h-mm-ss");
      if (file){
        logFile = File.AppendText(directory + "\\" + timestamp + "-experiment.log");
      }
      outputs = new Dictionary<Tuple<object, string>, ExperimentOutput>();
    }

    public void p(string s){
      if (console){
        C.p(s);
      }
      if (file){
        logFile.Write(s);
      }
    }

    public ExperimentOutput getExperimentOutput(object experiment, string id) {
      Tuple<object,string> key=new Tuple<object, string>(experiment,id);
      if (!outputs.ContainsKey(key)) {
        var output=new ExperimentOutput(directory + "\\" + timestamp + "-"+id+ "-"+experiment.GetType().Name+".csv",this);
        outputs.Add(key,output);
      }
      return outputs[key];
    }

    public void close(){
      if (file){
        logFile.Close();
      }
      outputs.Values.ForEach(o => o.close());
    }

  }
}