using System.Collections;
using System.Collections.Generic;
using mlt.experiments;

namespace mlt.classification{
  public abstract class DatasetGenerator<DatasetGeneratorOptions> :
    IEnumerable<GeneratedDataset<DatasetGeneratorOptions>>
    where DatasetGeneratorOptions : Report{
    public readonly List<DatasetGeneratorOptions> options;

    public DatasetGenerator(List<DatasetGeneratorOptions> options){
      this.options = options;
    }

    // IEnumerable Member
    IEnumerator IEnumerable.GetEnumerator(){
      return GetEnumerator();
    }

    public IEnumerator<GeneratedDataset<DatasetGeneratorOptions>> GetEnumerator(){
      foreach (DatasetGeneratorOptions o in options){
        yield return new GeneratedDataset<DatasetGeneratorOptions>(generateDataset(o), o);
      }
    }

    public abstract Dataset generateDataset(DatasetGeneratorOptions options);
  }
}