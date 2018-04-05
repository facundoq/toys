using System;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Media;
using System.Windows.Media.Media3D;
using HelixToolkit.Wpf;
using KinectUI.utility;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Kinect;

namespace KinectUI.view.kinect{
  public class Joint3D : ModelVisual3D{
    public JointType jointType;
    public SphereVisual3D Sphere { get; private set; }

    public Joint3D(JointType jointType){
      this.jointType = jointType;
      Sphere = new SphereVisual3D(){Radius = 0.02, ThetaDiv = 60, PhiDiv = 30, Center = new Point3D(0, 0, 0.5)};
      Sphere.Material = MaterialHelper.CreateMaterial(Colors.Blue);
      Children.Add(Sphere);
    }

    public void update(Vector3D p){
      //Sphere.Center = p.ToPoint3D();
      //var tg = new Transform3DGroup();
      //tg.Children.Add(new TranslateTransform3D(p));
      //Sphere.Transform = tg;
      Sphere.Transform = new TranslateTransform3D(new Vector3D(p.X, p.Y, p.Z));
      //Sphere.Transform = new TranslateTransform3D(p);
    }
  }
}