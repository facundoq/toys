using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Collections.ObjectModel;
using KinectUI.gestures;
using KinectUI.gestures.algorithmic;
using Microsoft.Kinect;
using KinectUI.gestures.trainable;
using KinectUI.view.windows;
using KinectUI.utility;
using SampleLibrary;
using utilities;

namespace KinectUI.view.ui{
  /// <summary>
  /// Interaction logic for GestureOptions.xaml
  /// </summary>
  /// 
  public partial class GestureOptions : UserControl{
    public ObservableCollection<IGesture> gestures2 { get; set; }
    public GestureSystem gestureSystem { get; set; }

    public GestureOptions(){
      gestures2 = new ObservableCollection<IGesture>();
      InitializeComponent();
    }

    public void initialize(GestureSystem gestureSystem){
      //this.kinectManager = manager;
      //this.initializeControl();
      //kinectManager.statusChanged += new EventHandler<KinectStateEvent>(managerStatusChanged);
      this.gestureSystem = gestureSystem;
      this.updateGestures();
    }

    public void updateGestures(){
      gestures2.Clear();
      foreach (IGesture g in gestureSystem.getGestures()){
        gestures2.Add(g);
      }
      ;
    }

    public void addGesture(object sender, RoutedEventArgs e){
      string id = "id-" + e.GetHashCode().ToString().Substring(0, 3);
      this.addGesture(id, JointType.HandLeft);
    }

    public void addGesture(string id, JointType jointType){
      var detector = this.gestureSystem.getGestureDetectors().OfType<TrainableGestureDetector>().First();
      detector.addGesture(new TrainableGesture(new Gesture(id)));
      this.updateGestures();
    }

    public void trainGesture(object sender, RoutedEventArgs e){
      IGesture gesture = (IGesture) this.gesturesList.SelectedItem;
      if (gesture != null){
        new TypeSwitch()
          .Case((TrainableGesture g) =>{
            new TrainableGestureWindow(g, MainWindow.instance).ShowDialog();
            this.updateGestures();
          })
          .Else(() => { })
          .Switch(gesture);
      }
      this.updateGestures();
    }
  }
}