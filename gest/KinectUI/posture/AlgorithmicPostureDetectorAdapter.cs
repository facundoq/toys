using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Kinect.Toolbox;
using Microsoft.Kinect;

namespace KinectUI.posture{
  public class AlgorithmicPostureDetectorAdapter : Posture{
    protected AlgorithmicPostureDetector a;

    public AlgorithmicPostureDetectorAdapter(string id) : base(id){
      this.a = new AlgorithmicPostureDetector();
      this.a.PostureDetected += detect;
    }

    public void detect(string id){
      base.detect(new PostureDetectedEvent(this, PostureState.On));
    }

    public override void update(Skeleton skeleton, long elapsedTime){
      a.TrackPostures(skeleton);
    }
  }
}