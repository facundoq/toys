using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using Microsoft.Kinect;
using KinectUI.gestures;
using System.Diagnostics;
using KinectUI.view;

namespace KinectUI{
  internal class View{
    private Label position;
    private TextBox console;
    private Label gestureLog;

    public View(Label position, TextBox console, Label gestureLog){
      this.position = position;
      this.console = console;
      this.gestureLog = gestureLog;
    }

    public void update(Skeleton skeleton){
      Joint hand = skeleton.Joints[JointType.HandLeft];
      Joint head = skeleton.Joints[JointType.Head];
      string content = KinectUtility.printPosition(hand) + "\n";
      content += KinectUtility.printPosition(head) + "\n";
      content += KinectUtility.printPosition(skeleton.Joints[JointType.HandRight]);
      position.Content = content;
      Trace.Listeners.Add(new TextBoxTraceListener(console));
    }

    public void updateGestureLog(GestureDetectedEvent e){
      string time = DateTime.Now.ToLongTimeString();
      gestureLog.Content += "\ngesture recognized: " + e.gesture.id + " (" + time + ")";
    }

    public class TextBoxTraceListener : TraceListener{
      private TextBox target;
      private StringSendDelegate invokeWrite;

      public TextBoxTraceListener(TextBox target){
        this.target = target;
        invokeWrite = new StringSendDelegate(SendString);
      }

      public override void Write(string message){
        target.Text += message;
      }

      public override void WriteLine(string message){
        target.Text += message + Environment.NewLine;
      }

      private delegate void StringSendDelegate(string message);

      private void SendString(string message){
        // No need to lock text box as this function will only 
        // ever be executed from the UI thread
        target.Text += message;
      }
    }
  }
}