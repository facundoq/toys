namespace mlt.experiments{
  public class ExperimentOptions{
    public int repetitions, threads, maxTime;

    public ExperimentOptions(int repetitions, int threads, int maxTime){
      this.repetitions = repetitions;
      this.threads = threads;
      this.maxTime = maxTime;
    }
  }
}