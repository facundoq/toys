using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KinectUI.actions{
  public class ConsoleOutputAction : Action{
    public override void execute(){
      Console.WriteLine(" hello! ");
    }
  }
}