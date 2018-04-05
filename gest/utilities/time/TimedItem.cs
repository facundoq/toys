namespace utilities.time{
  public struct TimedItem<T>{
    public TimeInfo time;
    public T item;

    public TimedItem(TimeInfo time, T item){
      this.time = time;
      this.item = item;
    }
  }
}