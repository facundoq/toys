using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KinectUI.gestures{
  public interface IGesture{
    string getInfo { get; }
    string id { get; }
  }

  [Serializable]
  public abstract class BaseGesture : IGesture{
    public string id { get; set; }

    protected BaseGesture(string id){
      this.id = id;
    }

    public abstract string getInfo { get; }
  }
}