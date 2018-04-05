using System;
using System.Collections.Generic;
using System.IO;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SampleLibrary {
  public class Visap2013GestureGenerator{
    public static String path = "D:\\Dropbox\\Tesina de Facundo Quiroga\\gestures\\samples\\WeightedDTW-Visapp2013-DB";

    public static void generate(){
     var gestures= new Visap2013GestureGenerator().convert();
      var outputFilepath = path + "\\visap.csv";
      //File.CreateText(outputFilepath);
      File.WriteAllText(outputFilepath, gestures);
    }
    public String convert(){
      var gestures = process(path + "\\training").Concat(process(path+"\\testing"));
      return "\n".@join(gestures);
    }

    public List<String> process(String path){
      var result = new List<String>();
      int klass=0;
      foreach (var d in Directory.EnumerateDirectories(path)){
        
        foreach (var file in Directory.EnumerateFiles(d)){
          String hand = (file.Contains("Left")) ? "HandLeft" : "HandRight";
          List<Position3> positions = parseGesture(file, hand);
          result.Add(output(positions,klass,file));
        }
         
        klass++;
      }

      return result;
    }

    private string output(List<Position3> positions, int klass, string file){
      string name = "unknown";
      foreach (var line in file.ReadFrom()){
        if (line.StartsWith("@")){
          name = line.Substring(1);
        }
        break;
      }
      String result = name+","+klass+",1,";
      result += ",".join(positions.Select(p=> ""+p.x+","+p.y+","+p.z));
      return result;

    }

    private List<Position3> parseGesture(string file, string hand){
      var result=new List<Position3>();
      var count = 0;
      double[] p= new double[3];
      foreach (var line in file.ReadFrom()){
        
        if (count > 0){
          p[3 - count] = Double.Parse(line);
          if (count == 1){
            result.Add(new Position3(p));
          }
          count--;
        }
        if (line.Contains("&" + hand)) {
          count = 3;

        }
      }
      return result;
    }
  }
}
