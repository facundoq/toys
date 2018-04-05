using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Kinect;
using Kinect.Toolbox;
using KinectUI.mouse;
using KinectUI.observer;
using System.Windows.Controls;
using KinectUI.utility;
using SampleLibrary;
using utilities;

namespace KinectUI.view.ui{
  /// <summary>   
  /// Interaction logic for SkeletonViewer.xaml
  /// </summary>
  /// 
  public enum SkeletonRecorderState{
    Initializing,
    Drawing,
    StartingToRecord,
    Recording,
    Playing
  }

  public partial class SkeletonRecorder : UserControl{
    private const int StartRecordingDelay = 3000;
    private IKinectSkeletonFrameProvider _provider;

    public IKinectSkeletonFrameProvider provider{
      get { return _provider; }
      set{
        _provider = value;
        provider.newFrame += new EventHandler<SkeletonFrameEvent>(onFrame);
      }
    }

    private SkeletonDrawer skeletonDrawer;
    private SamplePlayer samplePlayer;

    public SkeletonRecorderState state{
      get { return _state; }
      set{
        _state = value;
        configRecordingButtons();
      }
    }

    private SkeletonRecorderState _state;
    private DateTime startedRecordingTimestamp;

    public event EventHandler startedRecording;

    public class StoppedRecordingEvent : EventArgs{
      public List<SkeletonFrameInfo> list { get; set; }

      public StoppedRecordingEvent(List<SkeletonFrameInfo> list){
        this.list = list;
      }
    }

    public event EventHandler<StoppedRecordingEvent> stoppedRecording;
    public List<SkeletonFrameInfo> frames { get; set; }

    public SkeletonRecorder(){
      InitializeComponent();
      state = SkeletonRecorderState.Initializing;
      var options = new SkeletonDrawerOptions(Colors.Orange, JointType.HandLeft.inList());
      skeletonDrawer = new SkeletonDrawer(options, canvas);
    }

    public void onFrame(object sender, SkeletonFrameEvent e){
      switch (state){
        case SkeletonRecorderState.Initializing:{
          state = SkeletonRecorderState.Drawing;
          break;
        }
        case SkeletonRecorderState.Drawing:{
          skeletonDrawer.draw(e.skeletons, e.frame, e.sensor);
          break;
        }
        case SkeletonRecorderState.Recording:{
          skeletonDrawer.draw(e.skeletons, e.frame, e.sensor, new SkeletonDrawerOptions(
                                                                Colors.Red, JointType.HandLeft.inList()));
          SkeletonFrameInfo defaultSkeletonFrameInfo = e.getDefaultSkeletonFrameInfo();
          if (defaultSkeletonFrameInfo.skeleton != null){
            this.frames.Add(defaultSkeletonFrameInfo.copySkeleton());
          }
          break;
        }
        case SkeletonRecorderState.StartingToRecord:{
          skeletonDrawer.draw(e.skeletons, e.frame, e.sensor, new SkeletonDrawerOptions(
                                                                Colors.Yellow, JointType.HandLeft.inList()));
          this.drawTimeRemainingToStartRecording();
          break;
        }
        case SkeletonRecorderState.Playing:{
          break;
        }
      }
    }

    private void drawTimeRemainingToStartRecording(){
      long timeElapsedSinceStartButtonWasPressed = ((DateTime.Now.Ticks - startedRecordingTimestamp.Ticks)/10000);
      long timeRemaining = StartRecordingDelay - timeElapsedSinceStartButtonWasPressed;
      string text = (timeRemaining < 300) ? "Now" : "" + (timeRemaining/1000 + 1);
      int fontSize = (timeRemaining < 300) ? 100 : 75;
      TextBlock timeRemainingText = new TextBlock{
        Text = text,
        FontSize = fontSize,
        Foreground = new SolidColorBrush(Colors.White),
        VerticalAlignment = VerticalAlignment.Center,
        HorizontalAlignment = HorizontalAlignment.Right,
      };
      //Canvas.SetLeft(timeRemainingText, canvas.Width / 2);
      //Canvas.SetTop(timeRemainingText, canvas.Height / 2);
      canvas.Children.Add(timeRemainingText);
    }

    public void startRecording(){
      state = SkeletonRecorderState.Recording;
      this.frames = new List<SkeletonFrameInfo>();
      if (startedRecording != null){
        this.startedRecording(this, EventArgs.Empty);
      }
    }

    private void configRecordingButtons(){
      startRecordingButton.IsEnabled = state == SkeletonRecorderState.Drawing;
      if (state == SkeletonRecorderState.Drawing){
        startRecordingButton.Focus();
      }

      stopRecordingButton.IsEnabled = state == SkeletonRecorderState.Recording;
      if (state == SkeletonRecorderState.Recording){
        stopRecordingButton.Focus();
      }
    }

    public List<SkeletonFrameInfo> stopRecording(){
      state = SkeletonRecorderState.Drawing;
      if (stoppedRecording != null){
        this.stoppedRecording(this, new StoppedRecordingEvent(this.frames));
      }
      return this.frames;
    }

    private void startRecording(object sender, RoutedEventArgs e){
      state = SkeletonRecorderState.StartingToRecord;
      startedRecordingTimestamp = DateTime.Now;
      Action action = new Action(() => startRecording());
      new DelayedAction(StartRecordingDelay, action).execute();
    }

    private void stopRecording(object sender, RoutedEventArgs e){
      this.stopRecording();
    }

    public void play(Sample sample){
      state = SkeletonRecorderState.Playing;
      samplePlayer = new SamplePlayer(sample, skeletonDrawer, skeletonDrawer.sensor);
      samplePlayer.finishedPlaying += finishPlaying;
      samplePlayer.play();
    }

    public void finishPlaying(object sender, EventArgs e){
      state = SkeletonRecorderState.Drawing;
    }
  }
}