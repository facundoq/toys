using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using KinectUI.db;
using Microsoft.Kinect;
using Kinect.Toolbox;
using KinectUI.gestures;
using KinectUI.mouse;
using KinectUI.view;
using SampleLibrary;
using UnManaged;
using KinectUI.gestures.trainable;
using KinectUI.view.windows;
using KinectUI.observer;
using KinectUI.utility;

namespace KinectUI{
  /// <summary>
  /// Interaction logic for MainWindow.xaml
  /// </summary>
  public partial class MainWindow : Window, IKinectSkeletonFrameProvider{
    private KinectManager manager;
    private Skeleton[] skeletons;
    private TrayIconView trayIconView;
    public DB db;
    public static MainWindow instance;

    public MainWindow(){
      instance = this;
      initialized = false;
      manager = new KinectManager(this);
      InitializeComponent();
      trayIconView = new TrayIconView(manager);
      options.initialize(manager);
      status.initialize(manager);
      this.Closed += shutdown;
      db = new LibraryDB();
    }

    private void shutdown(object sender, EventArgs e){
      Clean();
    }

    private void MainWindowLoaded(object sender, RoutedEventArgs e){
      try{
        manager.onLoad();
      }
      catch (Exception ex){
        MessageBox.Show(ex.Message);
      }
    }

    public void shutdown(Hotkey hotkey){
      this.shutdown();
    }

    public void shutdown(){
      this.Clean();
      this.Close();
    }

    public void toggleAll(Hotkey hotkey){
      this.gestureSystem.pauseOrResume();
      this.mouseSystem.pauseOrResume();
    }

    public void toggleMouse(Hotkey hotkey){
      this.mouseSystem.pauseOrResume();
    }

    public void toggleGestures(Hotkey hotkey){
      this.gestureSystem.pauseOrResume();
    }

    public void Initialize(){
      manager.kinectSensor.SkeletonFrameReady += processFrame;
      if (!initialized){
        initialized = true;
        initializeSystems();
        registerHotkeys();

        //testing gesture training
        //NeuralNetworkDetector detector = this.gestureSystem.getGestureDetectors().OfType<NeuralNetworkDetector>().First();
        //TrainableGesture g =new TrainableGesture(new Gesture( "test"));
        //detector.addGesture(g);
        //gestures.updateGestures();
        //Action action = new Action(() => new TrainableGestureWindow(g, this).ShowDialog());
        //new DelayedAction(5000, action).execute();
      }
    }

    private void registerHotkeys(){
      KeyModifier modifier = KeyModifier.Ctrl | KeyModifier.Alt;
      new Hotkey(Key.F9, modifier, shutdown);
      new Hotkey(Key.F10, modifier, toggleMouse);
      new Hotkey(Key.F11, modifier, toggleGestures);
      new Hotkey(Key.F12, modifier, toggleAll);
    }

    private void processFrame(object sender, SkeletonFrameReadyEventArgs e){
      using (SkeletonFrame frame = e.OpenSkeletonFrame()){
        if (frame == null){
          return;
        }

        frame.GetSkeletons(ref skeletons);
        updateSystems(skeletons, frame);
      }
    }

    public void Clean(){
      //actionSystem.clean();
      db.shutdown();
    }

    public bool initialized { get; set; }
  }
}