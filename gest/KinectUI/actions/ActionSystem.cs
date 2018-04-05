using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.gestures;
using KinectUI.posture;
using utilities;

namespace KinectUI.actions{
  public class ActionSystem{
    private PostureSystem postureSystem;
    private GestureSystem gestureSystem;
    private MultivalueDictionary<string, GestureActionTranslator> gestureActions;
    private MultivalueDictionary<string, PostureActionTranslator> postureActions;

    public ActionSystem(PostureSystem postureSystem, GestureSystem gestureSystem){
      this.gestureSystem = gestureSystem;
      this.postureSystem = postureSystem;
      this.gestureActions = new MultivalueDictionary<string, GestureActionTranslator>();
      this.gestureSystem.detected += gestureDetected;
      this.postureActions = new MultivalueDictionary<string, PostureActionTranslator>();
      //this.postureSystem.detected += new EventHandler<PostureDetectedEvent>(postureDetected);
    }

    public void mapGestureToAction(string gesture, GestureActionTranslator gestureActionTranslator){
      this.gestureActions.Add(gesture, gestureActionTranslator);
    }

    public void mapPostureToAction(string posture, PostureActionTranslator postureActionTranslator){
      this.postureActions.Add(posture, postureActionTranslator);
    }

    protected void gestureDetected(object sender, GestureDetectedEvent e){
      if (!gestureActions.ContainsKey(e.gesture.id)){
        return;
      }
      List<GestureActionTranslator> translators = gestureActions[e.gesture.id];
      foreach (GestureActionTranslator translator in translators){
        translator.translate(e);
      }
    }

    protected void postureDetected(object sender, PostureDetectedEvent e){
      List<PostureActionTranslator> translators = postureActions[e.posture.id];
      foreach (PostureActionTranslator translator in translators){
        translator.translate(e);
      }
    }

    public void clean(){
      //gestureSystem.detected -= this.gestureDetected;
    }
  }
}