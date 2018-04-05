using mlt.experiments;

namespace mlt.classification{
  public struct GeneratedDataset<DatasetGeneratorOptions> where DatasetGeneratorOptions : Report{
    public Dataset dataset;
    public DatasetGeneratorOptions options;

    public GeneratedDataset(Dataset dataset, DatasetGeneratorOptions options){
      this.dataset = dataset;
      this.options = options;
    }
  }
}