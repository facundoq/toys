using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Db4objects.Db4o;
using Db4objects.Db4o.Config;
using KinectUI.actions;
using KinectUI.gestures;
using KinectUI.mouse.utility;

namespace KinectUI.db{
  public interface DB{
    void shutdown();
    GestureSystem getGestureSystem();
  }

  public class ODB : DB{
    public GestureSystem gestureSystem;
    private IObjectContainer container;

    public ODB(){
      IEmbeddedConfiguration config = Db4oEmbedded.NewConfiguration();
      config.Common.UpdateDepth = int.MaxValue;

      config.Common.ActivationDepth = int.MaxValue;
      container = Db4oEmbedded.OpenFile(config, @".\db5.yap");
      IList<GestureSystem> gestureSystems = container.Query<GestureSystem>();
      if (gestureSystems.Count == 0){
        gestureSystem = new GestureSystem(700);
      }
      else{
        gestureSystem = gestureSystems[0];
        foreach (var g in gestureSystem.getGestureDetectors()){
          g.detected += new EventHandler<GestureDetectedEvent>(gestureSystem.gestureDetected);
          Console.WriteLine(g.GetType());
          foreach (var d in g.knownGestures()){
            Console.WriteLine(d.GetType());
          }
          Console.WriteLine();
        }
      }
    }

    public void shutdown(){
      container.Store(gestureSystem);
      container.Close();
    }

    public GestureSystem getGestureSystem(){
      return gestureSystem;
    }

    public void store(object o){
      using (container){
        container.Store(o);
      }
    }
  }
}