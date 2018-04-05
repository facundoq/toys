using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KinectUI.posture{
  public enum PostureState{
    On,
    Off,
    Unknown
  }

  public class PostureDetectedEvent : EventArgs{
    public Posture posture { get; set; }
    public PostureState type { get; set; }

    public PostureDetectedEvent(Posture posture, PostureState type){
      this.posture = posture;
      this.type = type;
    }
  }
}