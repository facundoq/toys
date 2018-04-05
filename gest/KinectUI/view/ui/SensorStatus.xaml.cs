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

namespace KinectUI.view.ui{
  /// <summary>
  /// Interaction logic for SensorStatus.xaml
  /// </summary>
  public partial class SensorStatus : UserControl{
    public SensorStatus(){
      InitializeComponent();
    }

    public KinectManager kinectManager;

    public void initialize(KinectManager manager){
      this.kinectManager = manager;
      this.initializeControl();
      kinectManager.statusChanged += new EventHandler<KinectStateEvent>(manager_statusChanged);
    }

    private void initializeControl(){
      status.Content = kinectManager.state.ToString();
    }

    private void manager_statusChanged(object sender, KinectStateEvent e){
      initializeControl();
    }
  }
}