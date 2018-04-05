using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Media.Media3D;
using Microsoft.Kinect;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using utilities;

namespace SampleLibrary{
  public static class KinectExtensions{
    public static Skeleton deepClone(this Skeleton skeleton){
      var ms = new MemoryStream();
      var bf = new BinaryFormatter();
      bf.Serialize(ms, skeleton);
      ms.Position = 0;
      object result = bf.Deserialize(ms);
      ms.Close();
      return result as Skeleton;
    }

    public static void center(this Skeleton skeleton, SkeletonPoint position) {
      skeleton.Joints.ForEach(j => j.Position.minus(position));
    }
    public static void center(this Skeleton skeleton,JointType type){
      skeleton.center(skeleton.Joints[type].Position);
    }

    public static void minus(this SkeletonPoint p, SkeletonPoint q){
      p.X -= q.X;
      p.Y -= q.Y;
      p.Z -= q.Z;
    }

    public static SkeletonPoint toSkeletonPoint(this Position3 p) {
      var s= new SkeletonPoint();
      s.X = p.x;
      s.Y = p.y;
      s.Z = p.z;
      return s;
    }
    public static Position3 toPosition3(this SkeletonPoint vector){
      return new Position3(vector.X, vector.Y, vector.Z);
    }

    public static Vector3D ToVector3D(this SkeletonPoint vector){
      return new Vector3D(vector.X, vector.Y, vector.Z);
    }

    public static string toString(this Joint joint){
      return joint.JointType.ToString() + ": "
             + joint.Position.toPosition3().ToString()
             + "(" + joint.TrackingState.ToString() + ")";
    }


  }
}