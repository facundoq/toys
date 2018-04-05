using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Timers;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using ILNumerics;
using ILNumerics.Drawing;
using ILNumerics.Drawing.Plotting;
using mlt.classification;
using mlt.data;
using mlt.experiments;
using utilities;

namespace DataSetVisualization {
  /// <summary>
  /// Interaction logic for MainWindow.xaml
  /// </summary>
  public partial class MainWindow : Window {
    public ILPlotCube plotCube;
    private GeneratedDataset<GestureProcessingOptions> gd;
    public int currentGesture = 0;
    private ILPanel p;

    public MainWindow() {
      InitializeComponent();
      this.Loaded += new RoutedEventHandler(Window_Loaded);
    }

    private void Window_Loaded(object sender, RoutedEventArgs e) {
      gd = read();
      setUpPanel();
      updateGesture(0);
      setUpCiclying();
    }

    private void OnTimedEvent(object source, ElapsedEventArgs e) {
    }
    private void setUpCiclying(){
      var timer =new System.Windows.Threading.DispatcherTimer();
      timer.Tick += OnTimedEvent;
      timer.Interval = new TimeSpan(0, 0, 2);
      timer.Start(); ;
    }

    private void OnTimedEvent(object sender, EventArgs e){
      updateGesture(currentGesture + 1);
    
    }

    public void setUpPanel() {
       p = new ILPanel { };
      p.Driver = RendererTypes.GDI;
      plotCube = new ILPlotCube(null,false);
      var scene = new ILScene();
      scene.Children.Add(plotCube);
      host.Child = p;
      p.Scene = scene;

    }
    public void displayGesture(){
      var gesture = gd.dataset.patterns[currentGesture];
      var x = gesture.x;
       var positionsMatrix=x.toFloatArray().splitIntoGroupsArray(3);
      ILArray<float> positions= positionsMatrix;
      plotCube.Children.Clear();
      plotCube.Add(new ILLinePlot(positions));
      plotCube.Add(new ILLabel("class " + gesture.y));

      p.Refresh();
    }

    public GeneratedDataset<GestureProcessingOptions> read() {
      var po = new GestureProcessingOptions(false, 30, false, 3,false);
      //var path = "D:\\Dropbox\\Tesina de Facundo Quiroga\\gestures\\samples\\article\\visap-half.csv";
      var path = "D:\\Dropbox\\Tesina de Facundo Quiroga\\gestures\\samples\\article\\numbers\\numbers.csv";
      
      var dg = new GestureFileDatasetGenerator(po.inList(), path);

      return dg.ToList()[0];
    }

    private void displayPreviousGesture(object sender, RoutedEventArgs e) {
      updateGesture(currentGesture - 1);
    }

    private void updateGesture(int newCurrentGesture) {
      currentGesture = (newCurrentGesture < 0) ? gd.dataset.patterns.Count : newCurrentGesture;
      currentGesture = (newCurrentGesture > gd.dataset.patterns.Count) ? 0 : newCurrentGesture;
      displayGesture();
      //currentGestureLabel.Content = "" + currentGesture + "/" + gd.dataset.patterns.Count;
    }
    private void displayNextGesture(object sender, RoutedEventArgs e) {
      updateGesture(currentGesture + 1);
    }

    private void displayPreviousGesture(object sender, EventArgs e) {
      updateGesture(currentGesture - 1);
    }
  }
}
