using System;
using System.Collections.Generic;
using System.Globalization;
using mlt.classification;
using mlt.data;
using mlt.experiments;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.comparison.textmining {
  public class KeywordsFileDatasetGenerator : DatasetGenerator<KeywordProcessingOptions> {
      public readonly string filepath;

      public KeywordsFileDatasetGenerator(List<KeywordProcessingOptions> options, String filepath)
        : base(options) {
        this.filepath = filepath;
      }

      public override Dataset generateDataset(KeywordProcessingOptions options) {
        var data = readFile(filepath);
        return new Dataset(data);
      }


      public List<Pattern> readFile(String filepath) {
        return (from line in filepath.ReadFrom()
                where line.Length > 0 && !line.StartsWith("#")
                select parsePattern(line)).ToList();
      }

      private Pattern parsePattern(string line) {
        var parts = line.Split(';'.inArray());
        var y = Int32.Parse(parts[12]);
        var x = parts.Where((v, i) => i > 3 && i < 12).Select(v => Double.Parse(v, CultureInfo.InvariantCulture)).ToArray();
        return new Pattern(x, y);
      }

  }

  public class KeywordProcessingOptions : Report,ID{
    public string report() {
      return this.fields().toString();
    }

    public string header() {
      return ",".join(this.fieldsNames());
    }

    public string csv(){
      return ",".join(this.fieldsValues());
    }

    public string id(){
      throw new Exception("fail");
    }
  }
}
