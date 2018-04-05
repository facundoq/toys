using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.posture;

namespace KinectUI.actions{
  public class PostureActionTranslator{
    private Action offAction;
    private Action unknownAction;
    private Action onAction;

    public PostureActionTranslator(Action offAction, Action onAction)
      : this(offAction, offAction, onAction){
    }

    public PostureActionTranslator(Action unknownAction, Action offAction, Action onAction){
      this.offAction = offAction;
      this.onAction = onAction;
      this.unknownAction = unknownAction;
    }

    public void translate(PostureDetectedEvent e){
      switch (e.type){
        case PostureState.Unknown:{
          unknownAction.execute();
          break;
        }
        case PostureState.On:{
          onAction.execute();
          break;
        }
        case PostureState.Off:{
          offAction.execute();
          break;
        }
      }
    }
  }
}