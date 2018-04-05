using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Timers;
using System.Windows.Threading;

namespace KinectUI.utility{
  public class DelayedAction{
    public TimeSpan delay;
    public Action action;

    public DispatcherTimer timer;

    public DelayedAction(TimeSpan delay, Action action){
      this.delay = delay;
      this.action = action;
    }

    public DelayedAction(int delay, Action action) : this(new TimeSpan(0, 0, 0, 0, delay), action){
    }

    public void execute(){
      timer = new DispatcherTimer();
      timer.Tick += new EventHandler(executeAction);
      timer.Interval = delay;
      timer.Start();
    }

    private void executeAction(object sender, EventArgs e){
      timer.Stop();
      action();
    }
  }
}