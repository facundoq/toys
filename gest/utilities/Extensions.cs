using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Runtime.Serialization.Formatters.Binary;
using KinectUI.utility;
using System.Linq;
using System.Text;

namespace utilities{
  public static class Extensions{
    public static float nextFloat(this Random r){
      return (float) r.NextDouble();
    }

    public static Dictionary<string, object> properties(this object o){
      var result = new Dictionary<string, object>();
      foreach (var i in o.GetType().GetProperties()){
        result.Add(i.Name, i.GetValue(o, null));
      }
      return result;
    }

    public static Dictionary<string, object> fields(this object o){
      var result = new Dictionary<string, object>();
      foreach (var  i in o.GetType().GetFields()){
        result.Add(i.Name, i.GetValue(o));
      }
      return result;
    }

    public static List<string> fieldsNames(this object o) {
      var result = new List<string>();
      foreach (var i in o.GetType().GetFields()) {
        result.Add(i.Name);
      }
      return result;
    }

    public static List<object> fieldsValues(this object o) {
      var result = new List<object>();
      foreach (var i in o.GetType().GetFields()) {
        result.Add(i.GetValue(o));
      }
      return result;
    }

    public static void serialize<T>(this T t, string filepath){
      Stream s = File.Create(filepath);
      new BinaryFormatter().Serialize(s, t);
      s.Close();
    }

    public static T deserialize<T>(string filepath){
      Stream s = File.OpenRead(filepath);
      T t = (T) (new BinaryFormatter().Deserialize(s));
      s.Close();
      return t;
    }

    public static IEnumerable<string> ReadFrom(this string file) {
      string line;
      using (var reader = File.OpenText(file)) {
        while ((line = reader.ReadLine()) != null) {
          yield return line;
        }
      }
    }
  }
}