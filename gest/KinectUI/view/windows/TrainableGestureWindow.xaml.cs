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
using System.Windows.Shapes;
using KinectUI.gestures.trainable;
using KinectUI.observer;
using KinectUI.utility;
using Microsoft.Kinect;
using System.Collections.ObjectModel;
using KinectUI.view.ui;
using SampleLibrary;
using utilities;

namespace KinectUI.view.windows{
  /// <summary>
  /// Interaction logic for GestureWindow.xaml
  /// </summary>
  public partial class TrainableGestureWindow : Window{
    public ObservableCollection<Sample> samples { get; set; }

    public TrainableGesture gesture { get; set; }
    public IKinectSkeletonFrameProvider provider { get; set; }

    public Boolean recording{
      get { return recorder.state == SkeletonRecorderState.Recording; }
    }

    public event Action startedRecordingEvent;
    public event Action stoppedRecordingEvent;

    public TrainableGestureWindow(TrainableGesture gesture, IKinectSkeletonFrameProvider provider){
      this.gesture = gesture;
      this.provider = provider;
      this.samples = new ObservableCollection<Sample>();
      InitializeComponent();
      updateSamples();
      this.recorder.provider = provider;
      this.recorder.startedRecording += startedRecording;
      this.recorder.stoppedRecording += stoppedRecording;
    }

    public void updateSamples(){
      this.samples.Clear();
      this.samples.AddAll(gesture.samples);
      this.samplesContainer.Header = "Samples: " + gesture.samples.Count;
    }

    public void startedRecording(object sender, EventArgs e){
      if (startedRecordingEvent != null){
        this.startedRecordingEvent();
      }
    }

    public void stoppedRecording(object sender, KinectUI.view.ui.SkeletonRecorder.StoppedRecordingEvent e){
      gesture.addSample(new Sample(new List<SkeletonFrameInfo>(e.list), DateTime.Now));
      updateSamples();
      if (stoppedRecordingEvent != null){
        this.stoppedRecordingEvent();
      }
    }

    private void play(object sender, RoutedEventArgs e){
      recorder.play((Sample) samplesList.SelectedItem);
    }

    private void remove(object sender, RoutedEventArgs e){
      gesture.removeSample((Sample) samplesList.SelectedItem);
      updateSamples();
    }

    private void Id_OnTextChanged(object sender, TextChangedEventArgs e){
      gesture.id = id.Text;
    }
  }
}