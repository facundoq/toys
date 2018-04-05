using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace KinectUI.mouse{
  [Flags]
  public enum MouseEvent{
    LeftDown = 0x00000002,
    LeftUp = 0x00000004,
    LeftClick = 0x00000004 | 0x00000002,
    MiddleDown = 0x00000020,
    MiddleUp = 0x00000040,
    Move = 0x00000001,
    Absolute = 0x00008000,
    RightDown = 0x00000008,
    RightUp = 0x00000010,
    RightClick = 0x00000010 | 0x00000008,
  }
}