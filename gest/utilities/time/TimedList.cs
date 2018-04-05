using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace utilities.time{
  internal class TimedList<T> : IEnumerable<TimedItem<T>>{
    private List<TimedItem<T>> items;

    public TimedList(){
      items = new List<TimedItem<T>>();
    }

    public void Add(T e, long time){
      TimedItem<T> item = new TimedItem<T>();
      this.Add(item);
    }

    public void Add(TimedItem<T> item){
      items.Add(item);
    }

    public IEnumerator<TimedItem<T>> GetEnumerator(){
      return items.GetEnumerator();
    }

    System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator(){
      return items.GetEnumerator();
    }
  }
}