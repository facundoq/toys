using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace KinectUI.posture{
  public abstract class Posture{
    public string id;
    public PostureState state;

    public Posture(string id){
      this.id = id;
      this.state = PostureState.Unknown;
    }

    public abstract void update(Skeleton skeleton, long elapsedTime);

    public void detect(PostureDetectedEvent postureEvent){
      if (this.detected != null){
        this.detected(this, postureEvent);
      }
    }

    public event EventHandler<PostureDetectedEvent> detected;

    protected void changeToState(PostureState state){
      if (!state.Equals(state)){
        this.state = state;
        this.detect(new PostureDetectedEvent(this, this.state));
      }
    }

    protected void isOnOnlyIf(bool condition){
      if (condition){
        changeToState(PostureState.On);
      }
      else{
        changeToState(PostureState.Off);
      }
    }
  }
}