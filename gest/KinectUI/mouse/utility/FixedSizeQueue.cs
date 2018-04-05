using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KinectUI.mouse{
  public class FixedSizeQueue<T> : Queue<T>{
    public readonly int size;

    public FixedSizeQueue(int size){
      this.size = size;
    }

    public new void Enqueue(T item){
      if (this.isFull()){
        this.Dequeue();
      }
      base.Enqueue(item);
    }

    public bool isFull(){
      return this.Count == this.size;
    }
  }
}