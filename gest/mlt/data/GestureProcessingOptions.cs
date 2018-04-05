using System;
using System.Collections.Generic;
using mlt.classification;
using mlt.experiments;
using utilities;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mlt.data {
  public class GestureProcessingOptions:Report,ID{
    public bool angles;
    public int n;
    public bool normalize;
    public int smoothingWindowSize;
    public bool rotate;
    public readonly string datasetId;

    public GestureProcessingOptions(bool angles, int n, bool normalize, int smoothingWindowSize, bool rotate, string datasetId) {
      this.angles = angles;
      this.n = n;
      this.normalize = normalize;
      this.smoothingWindowSize = smoothingWindowSize;
      this.rotate = rotate;
      this.datasetId = datasetId;
    }

    public string report(){
      return this.fields().toString();
    }

    public string csv(){
      return ",".join(this.fieldsValues());
    }

    public string header() {
      return ",".join(this.fieldsNames());
    }

    public string id(){
      return datasetId;
    }
  }
}
