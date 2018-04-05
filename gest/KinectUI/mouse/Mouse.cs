using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;

namespace KinectUI.mouse{
  public class Mouse{
    [DllImport("user32.dll", EntryPoint = "SetCursorPos")]
    [return: MarshalAs(UnmanagedType.Bool)]
    private static extern bool SetCursorPos(int X, int Y);

    [DllImport("user32.dll")]
    [return: MarshalAs(UnmanagedType.Bool)]
    private static extern bool GetCursorPos(out MousePoint lpMousePoint);

    [DllImport("user32.dll")]
    private static extern void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);

    public static void setCursorPosition(int X, int Y){
      SetCursorPos(X, Y);
    }

    public static MousePoint getCursorPosition(){
      MousePoint currentMousePoint;
      var gotPoint = GetCursorPos(out currentMousePoint);
      if (!gotPoint){
        currentMousePoint = new MousePoint(0, 0);
      }
      return currentMousePoint;
    }

    public static void triggerEvent(MouseEvent value){
      MousePoint position = getCursorPosition();

      mouse_event
        ((int) value,
         position.X,
         position.Y,
         0,
         0)
        ;
    }

    [StructLayout(LayoutKind.Sequential)]
    public struct MousePoint{
      public int X;
      public int Y;

      public MousePoint(int x, int y){
        X = x;
        Y = x;
      }
    }
  }
}