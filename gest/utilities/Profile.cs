using System;
using System.Collections.Generic;
using KinectUI.utility;
using System.Linq;
using System.Text;

namespace utilities{
  public class Profile{
    public struct Snapshot{
      public DateTime t;
      public string s;

      public Snapshot(DateTime t, string s){
        this.t = t;
        this.s = s;
      }

      public long difference(Snapshot s){
        return (t.Ticks - s.t.Ticks)/10000;
      }
    }

    private List<Snapshot> snapshots;

    public Profile(){
      this.snapshots = new List<Snapshot>();
    }

    public Profile snapshot(string s){
      snapshots.Add(new Snapshot(DateTime.Now, s));
      return this;
    }

    public override string ToString(){
      if (snapshots.Count == 0){
        return "No snapshots.";
      }
      string total = "Total: " + snapshots.Last().difference(snapshots[0]) + "ms \n";
      String detail = "";
      Snapshot last = snapshots[0];
      int i = 0;
      foreach (var s in snapshots){
        long timeDifference = s.difference(last);
        detail += i + " - " + s.s + ": " + timeDifference + "ms\n";
        last = s;
        i++;
      }
      return total + detail;
    }
  }
}