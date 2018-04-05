using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace utilities.time{
  public class TimeInfo{
    public long timestamp;
    public long elapsedTime;

    public TimeInfo(long elapsedTime, long timestamp){
      this.elapsedTime = elapsedTime;
      this.timestamp = timestamp;
    }

    public TimeInfo(){
      this.elapsedTime = 0;
      this.timestamp = DateTime.Now.Ticks/10000;
    }

    public TimeInfo travelTo(long timestamp){
      return new TimeInfo(timestamp - this.timestamp, timestamp);
    }

    public TimeInfo travelToNow(){
      return this.travelTo(DateTime.Now.Ticks/10000);
    }
  }
}