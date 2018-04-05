using System;
using System.Collections.Generic;
using KinectUI.gestures;
using KinectUI.gestures.trainable;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SampleLibrary;

namespace KinectUI.db{
  internal class LibraryDB : DB{
    private Library library;
    public GestureSystem system;

    public LibraryDB(){
      library = new Library();
      system = new GestureSystem();
      //TrainableGestureDetector detector=system.getGestureDetectors().OfType<NeuralNetworkDetector>().ToList()[0];
      //detector.addGestures(library.gestures.Select(g => new TrainableGesture(g)).ToList());
      //detector.train();
      var detector = system.getGestureDetectors().OfType<RBFDetector>().ToList()[0];
      if (library.gestures.Count != 0){
        detector.addGestures(library.gestures.Select(g => new TrainableGesture(g)).ToList());
        detector.train();
      }
    }

    public void shutdown(){
      library.clear();
      library.addAll(this.getTrainableGestures());
      library.save();
    }

    private List<Gesture> getTrainableGestures(){
      return system.getGestures().OfType<TrainableGesture>().Select(g => g.gesture).ToList();
    }

    public GestureSystem getGestureSystem(){
      return system;
    }
  }
}