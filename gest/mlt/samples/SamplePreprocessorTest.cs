using NUnit.Framework;
using mlt.samples;
using utilities;

namespace mlt{
  [TestFixture]
  internal class SamplePreprocessorTest{
    public PreprocessingParameters p0 = new PreprocessingParameters(20, 3);

    [Test]
    public void constantSample1(){
      var input = Samples.createInput(p0, Line3.horizontal(new Position3() + 1), new Position3() + 1,
                                      new DoubleRange(3));
      Assert.AreEqual(1.inArray(p0.samplingPoints*3), input);
    }

    [Test]
    public void constantSample2WithCenter1(){
      var input = Samples.createInput(p0, Line3.horizontal(new Position3() + 2), new Position3() + 1,
                                      new DoubleRange(3));
      Assert.AreEqual(1.inArray(p0.samplingPoints*3), input);
    }

    [Test]
    public void lineSample(){
      var input = Samples.createInput(p0, Line3.origin(new Position3() + 1), new Position3(),
                                      new DoubleRange(3));

      // test something (increasing sequence?)
    }

    [Test]
    public void constantSample0(){
      var input = Samples.createInput(p0, Line3.horizontal(new Position3()), new Position3(),
                                      new DoubleRange(3));
      Assert.AreEqual(1.inArray(p0.samplingPoints*3), input);
    }
  }
}