using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KinectUI{
  public class KinectStateEvent : EventArgs{
    public KinectManager.State state;

    public KinectStateEvent(KinectManager.State state){
      this.state = state;
    }
  }
}