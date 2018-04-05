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
  /// Interaction logic for Console.xaml
  /// </summary>
  public class ManagerAwareControl : UserControl{
    protected KinectManager kinectManager;

    public void initialize(KinectManager manager){
      this.kinectManager = manager;
      this.initializeControl();
      kinectManager.statusChanged += new EventHandler<KinectStateEvent>(managerStatusChanged);
    }

    protected virtual void initializeControl(){
      throw new NotImplementedException();
    }

    protected virtual void managerStatusChanged(object sender, KinectStateEvent e){
      throw new NotImplementedException();
    }

    public ManagerAwareControl(){
    }
  }
}