using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using KinectUI.mouse;
using WindowsInput;

namespace KinectUI.actions{
  public class KeyboardAction : Action{
    private IEnumerable<VirtualKeyCode> modifierKeyCodes, keyCodes;

    public KeyboardAction(IEnumerable<VirtualKeyCode> modifierKeyCodes, IEnumerable<VirtualKeyCode> keyCodes){
      this.modifierKeyCodes = modifierKeyCodes;
      this.keyCodes = keyCodes;
    }

    public override void execute(){
      InputSimulator.SimulateModifiedKeyStroke(modifierKeyCodes, keyCodes);
    }
  }
}