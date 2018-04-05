using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using KinectUI.Properties;
using System.IO;
using System.Windows.Media.Imaging;
using Hardcodet.Wpf.TaskbarNotification;
using KinectUI;

namespace KinectUI.view{
  public class TrayIconView{
    private TaskbarIcon taskbarIcon;
    //private ContextMenu trayMenu;
    private KinectManager manager;

    public TrayIconView(KinectManager manager){
      this.manager = manager;
      // Create a simple tray menu with only one item.
      //trayMenu = new ContextMenu();
      //trayMenu.MenuItems.Add("Exit", OnExit);

      taskbarIcon = new TaskbarIcon();
      taskbarIcon.Icon = new Icon(SystemIcons.Question, 40, 40);
      taskbarIcon.ToolTipText = "Loading...";
      manager.statusChanged += updateIcon;
      //trayIcon.ContextMenu = trayMenu;
    }

    public void updateIcon(Object manager, KinectStateEvent e){
      switch (e.state){
        case KinectManager.State.Disconnected:{
          taskbarIcon.Icon = new Icon(SystemIcons.Error, 40, 40);
          break;
        }
        case KinectManager.State.Unpowered:{
          taskbarIcon.Icon = new Icon(SystemIcons.Warning, 40, 40);
          break;
        }
        case KinectManager.State.NotReady:{
          taskbarIcon.Icon = new Icon(SystemIcons.Exclamation, 40, 40);
          break;
        }
        case KinectManager.State.PoweredNotTracking:{
          taskbarIcon.Icon = new Icon(SystemIcons.Application, 40, 40);
          break;
        }
        case KinectManager.State.PoweredTracking:{
          taskbarIcon.Icon = new Icon(SystemIcons.Asterisk, 40, 40);
          break;
        }
      }
      taskbarIcon.ToolTipText = e.state.ToString();

      //String path = Path.Combine(Environment.CurrentDirectory, "res","tray",manager.state+".png");

      //var uri = new Uri(path);

      //Icon.FromHandle(Image.FromFile(path).trayIcon.Icon = new Icon(SystemIcons.Application, 40, 40);
    }

    /// <summary>
    /// Converts an image into an icon.
    /// </summary>
    /// <param name="img">The image that shall become an icon</param>
    /// <param name="size">The width and height of the icon. Standard
    /// sizes are 16x16, 32x32, 48x48, 64x64.</param>
    /// <param name="keepAspectRatio">Whether the image should be squashed into a
    /// square or whether whitespace should be put around it.</param>
    /// <returns>An icon!!</returns>
    private Icon MakeIcon(Image img, int size, bool keepAspectRatio){
      Bitmap square = new Bitmap(size, size); // create new bitmap
      Graphics g = Graphics.FromImage(square); // allow drawing to it

      int x, y, w, h; // dimensions for new image

      if (!keepAspectRatio || img.Height == img.Width){
        // just fill the square
        x = y = 0; // set x and y to 0
        w = h = size; // set width and height to size
      }
      else{
        // work out the aspect ratio
        float r = (float) img.Width/(float) img.Height;

        // set dimensions accordingly to fit inside size^2 square
        if (r > 1){
          // sw is bigger, so divide h by r
          w = size;
          h = (int) ((float) size/r);
          x = 0;
          y = (size - h)/2; // center the image
        }
        else{
          // h is bigger, so multiply sw by r
          w = (int) ((float) size*r);
          h = size;
          y = 0;
          x = (size - w)/2; // center the image
        }
      }

      // make the image shrink nicely by using HighQualityBicubic mode
      g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.HighQualityBicubic;
      g.DrawImage(img, x, y, w, h); // draw image with specified dimensions
      g.Flush(); // make sure all drawing operations complete before we get the icon

      // following line would work directly on any image, but then
      // it wouldn't look as nice.
      return Icon.FromHandle(square.GetHicon());
    }

    public void showBalloon(string title, string message){
      taskbarIcon.ShowBalloonTip(title, message, BalloonIcon.Info);
    }
  }
}