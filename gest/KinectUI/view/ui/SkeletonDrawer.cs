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
using Microsoft.Kinect;
using Kinect.Toolbox;
using KinectUI.mouse;
using KinectUI.observer;
using SampleLibrary;
using utilities;

namespace KinectUI.view.ui{
  public class SkeletonDrawerOptions{
    public Color skeletonColor = Colors.Orange;
    public List<JointType> highlightedJoints;

    public SkeletonDrawerOptions() : this(Colors.Orange, new List<JointType>()){
    }

    public SkeletonDrawerOptions(Color skeletonColor, List<JointType> highlightedJoints){
      this.skeletonColor = skeletonColor;
      this.highlightedJoints = highlightedJoints;
    }

    public Color highlightedColor{
      get { return Colors.Green; }
    }
  }

  public class SkeletonDrawer{
    public SkeletonDrawerOptions o;
    public Canvas canvas { get; private set; }
    public KinectSensor sensor { get; set; }
    public List<Ellipse> trace;

    public SkeletonDrawer(SkeletonDrawerOptions o, Canvas canvas){
      this.o = o;
      this.canvas = canvas;
      trace = new List<Ellipse>();
    }

    public SkeletonDrawer(Canvas canvas) : this(new SkeletonDrawerOptions(), canvas){
    }

    private void GetCoordinates(JointType jointType, IEnumerable<Joint> joints, out float x, out float y){
      var joint = joints.First(j => j.JointType == jointType);

      ColorImageFormat format = sensor.ColorStream.Format;

      ColorImagePoint p = sensor.MapSkeletonPointToColor(joint.Position, format);
      Position r = KinectUtility.getResolution(format);

      x = (float) (p.X/r.x*canvas.ActualWidth);
      y = (float) (p.Y/r.y*canvas.ActualHeight);
    }

    private void Plot(SkeletonDrawerOptions options, JointType centerID, IEnumerable<Joint> joints){
      float centerX;
      float centerY;

      GetCoordinates(centerID, joints, out centerX, out centerY);
      var highlight = options.highlightedJoints.Contains(centerID);
      double diameter = highlight ? 16 : 8;
      var color = highlight ? options.highlightedColor : options.skeletonColor;

      var ellipse = this.ellipse(diameter, color, centerX, centerY);

      canvas.Children.Add(ellipse);
      if (highlight){
        if (trace.Count > 40){
          var e = trace.removeAt(0);
          //canvas.Children.Remove(e);
        }
        var te = this.ellipse(5, Colors.Aqua, centerX, centerY);
        trace.Add(te);
        trace.ForEach(e => canvas.Children.Add(e));
      }
    }

    protected Ellipse ellipse(double diameter, Color color, float centerX, float centerY){
      Ellipse ellipse = new Ellipse{
        Width = diameter,
        Height = diameter,
        HorizontalAlignment = HorizontalAlignment.Left,
        VerticalAlignment = VerticalAlignment.Top,
        StrokeThickness = 4.0,
        Stroke = new SolidColorBrush(color),
        StrokeLineJoin = PenLineJoin.Round
      };

      Canvas.SetLeft(ellipse, centerX - ellipse.Width/2);
      Canvas.SetTop(ellipse, centerY - ellipse.Height/2);
      return ellipse;
    }

    private void Plot(SkeletonDrawerOptions options, JointType centerID, JointType baseID, JointCollection joints){
      float centerX;
      float centerY;

      GetCoordinates(centerID, joints, out centerX, out centerY);

      float baseX;
      float baseY;

      GetCoordinates(baseID, joints, out baseX, out baseY);

      double diameter = Math.Abs(baseY - centerY);

      Ellipse ellipse = new Ellipse{
        Width = diameter,
        Height = diameter,
        HorizontalAlignment = HorizontalAlignment.Left,
        VerticalAlignment = VerticalAlignment.Top,
        StrokeThickness = 4.0,
        Stroke = new SolidColorBrush(options.skeletonColor),
        StrokeLineJoin = PenLineJoin.Round
      };

      Canvas.SetLeft(ellipse, centerX - ellipse.Width/2);
      Canvas.SetTop(ellipse, centerY - ellipse.Height/2);

      canvas.Children.Add(ellipse);
    }

    private void Trace(SkeletonDrawerOptions options, JointType sourceID, JointType destinationID,
                       JointCollection joints){
      float sourceX;
      float sourceY;

      GetCoordinates(sourceID, joints, out sourceX, out sourceY);

      float destinationX;
      float destinationY;

      GetCoordinates(destinationID, joints, out destinationX, out destinationY);

      Line line = new Line{
        X1 = sourceX,
        Y1 = sourceY,
        X2 = destinationX,
        Y2 = destinationY,
        HorizontalAlignment = HorizontalAlignment.Left,
        VerticalAlignment = VerticalAlignment.Top,
        StrokeThickness = 4.0,
        Stroke = new SolidColorBrush(options.skeletonColor),
        StrokeLineJoin = PenLineJoin.Round
      };

      canvas.Children.Add(line);
    }

    public void draw(Skeleton skeleton, FrameInfo frame, KinectSensor sensor, SkeletonDrawerOptions options){
      Skeleton[] skeletons = new Skeleton[]{skeleton};
      this.draw(skeletons, frame, sensor, options);
    }

    public void draw(Skeleton skeleton, FrameInfo frame, KinectSensor sensor){
      this.draw(skeleton, frame, sensor, o);
    }

    public void draw(Skeleton[] skeletons, FrameInfo frame, KinectSensor sensor){
      this.sensor = sensor;
      this.draw(skeletons, sensor.SkeletonStream.TrackingMode == SkeletonTrackingMode.Seated, frame.fps, o);
    }

    public void draw(Skeleton[] skeletons, FrameInfo frame, KinectSensor sensor, SkeletonDrawerOptions options){
      this.sensor = sensor;
      this.draw(skeletons, sensor.SkeletonStream.TrackingMode == SkeletonTrackingMode.Seated, frame.fps, options);
    }

    private void draw(Skeleton[] skeletons, bool seated, float fps, SkeletonDrawerOptions options){
      canvas.Children.Clear();
      TextBlock textblock = new TextBlock{
        Text = ("fps:" + fps.ToString()),
        FontSize = 25,
        Foreground = new SolidColorBrush(Colors.White),
      };
      canvas.Children.Add(textblock);
      foreach (Skeleton skeleton in skeletons){
        if (skeleton.TrackingState != SkeletonTrackingState.Tracked){
          continue;
        }

        Plot(options, JointType.HandLeft, skeleton.Joints);
        Trace(options, JointType.HandLeft, JointType.WristLeft, skeleton.Joints);
        Plot(options, JointType.WristLeft, skeleton.Joints);
        Trace(options, JointType.WristLeft, JointType.ElbowLeft, skeleton.Joints);
        Plot(options, JointType.ElbowLeft, skeleton.Joints);
        Trace(options, JointType.ElbowLeft, JointType.ShoulderLeft, skeleton.Joints);
        Plot(options, JointType.ShoulderLeft, skeleton.Joints);
        Trace(options, JointType.ShoulderLeft, JointType.ShoulderCenter, skeleton.Joints);
        Plot(options, JointType.ShoulderCenter, skeleton.Joints);

        Trace(options, JointType.ShoulderCenter, JointType.Head, skeleton.Joints);

        Plot(options, JointType.Head, JointType.ShoulderCenter, skeleton.Joints);

        Trace(options, JointType.ShoulderCenter, JointType.ShoulderRight, skeleton.Joints);
        Plot(options, JointType.ShoulderRight, skeleton.Joints);
        Trace(options, JointType.ShoulderRight, JointType.ElbowRight, skeleton.Joints);
        Plot(options, JointType.ElbowRight, skeleton.Joints);
        Trace(options, JointType.ElbowRight, JointType.WristRight, skeleton.Joints);
        Plot(options, JointType.WristRight, skeleton.Joints);
        Trace(options, JointType.WristRight, JointType.HandRight, skeleton.Joints);
        Plot(options, JointType.HandRight, skeleton.Joints);

        if (!seated){
          Trace(options, JointType.ShoulderCenter, JointType.Spine, skeleton.Joints);
          Plot(options, JointType.Spine, skeleton.Joints);
          Trace(options, JointType.Spine, JointType.HipCenter, skeleton.Joints);
          Plot(options, JointType.HipCenter, skeleton.Joints);

          Trace(options, JointType.HipCenter, JointType.HipLeft, skeleton.Joints);
          Plot(options, JointType.HipLeft, skeleton.Joints);
          Trace(options, JointType.HipLeft, JointType.KneeLeft, skeleton.Joints);
          Plot(options, JointType.KneeLeft, skeleton.Joints);
          Trace(options, JointType.KneeLeft, JointType.AnkleLeft, skeleton.Joints);
          Plot(options, JointType.AnkleLeft, skeleton.Joints);
          Trace(options, JointType.AnkleLeft, JointType.FootLeft, skeleton.Joints);
          Plot(options, JointType.FootLeft, skeleton.Joints);

          Trace(options, JointType.HipCenter, JointType.HipRight, skeleton.Joints);
          Plot(options, JointType.HipRight, skeleton.Joints);
          Trace(options, JointType.HipRight, JointType.KneeRight, skeleton.Joints);
          Plot(options, JointType.KneeRight, skeleton.Joints);
          Trace(options, JointType.KneeRight, JointType.AnkleRight, skeleton.Joints);
          Plot(options, JointType.AnkleRight, skeleton.Joints);
          Trace(options, JointType.AnkleRight, JointType.FootRight, skeleton.Joints);
          Plot(options, JointType.FootRight, skeleton.Joints);
        }
      }
    }
  }
}