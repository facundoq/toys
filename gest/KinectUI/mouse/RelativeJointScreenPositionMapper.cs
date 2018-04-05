using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Kinect.Toolbox;
using Microsoft.Kinect;
using utilities;

namespace KinectUI.mouse{
  public class RelativeJointScreenPositionMapper : JointScreenPositionMapper{
    //Vector2? lastKnownPosition;
    //float previousDepth;

    // Filters
    private Vector2 savedFilteredJointPosition;
    private Vector2 savedTrend;
    private Vector2 savedBasePosition;
    private int frameCount;

    // Filter p
    public float TrendSmoothingFactor;
    public float JitterRadius;
    public float DataSmoothingFactor;
    public float PredictionFactor;
    public float GlobalSmooth;

    public RelativeJointScreenPositionMapper(){
      TrendSmoothingFactor = 0.25f;
      JitterRadius = 0.05f;
      DataSmoothingFactor = 0.5f;
      PredictionFactor = 0.5f;

      GlobalSmooth = 0.9f;
    }

    public override Position toScreen(Joint joint, KinectSensor sensor){
      Vector2 filteredJointPosition;
      Vector2 differenceVector;
      Vector2 currentTrend;
      float distance;

      Vector2 baseJointPosition = Tools.Convert(sensor, joint.Position);
      Vector2 prevFilteredJointPosition = savedFilteredJointPosition;
      Vector2 previousTrend = savedTrend;
      Vector2 previousBaseJointPosition = savedBasePosition;

      // Checking frames count
      switch (frameCount){
        case 0:
          filteredJointPosition = baseJointPosition;
          currentTrend = Vector2.Zero;
          break;
        case 1:
          filteredJointPosition = (baseJointPosition + previousBaseJointPosition)*0.5f;
          differenceVector = filteredJointPosition - prevFilteredJointPosition;
          currentTrend = differenceVector*TrendSmoothingFactor + previousTrend*(1.0f - TrendSmoothingFactor);
          break;
        default:
          // Jitter filter
          differenceVector = baseJointPosition - prevFilteredJointPosition;
          distance = Math.Abs(differenceVector.Length);

          if (distance <= JitterRadius){
            filteredJointPosition = baseJointPosition*(distance/JitterRadius) +
                                    prevFilteredJointPosition*(1.0f - (distance/JitterRadius));
          }
          else{
            filteredJointPosition = baseJointPosition;
          }

          // Double exponential smoothing filter
          filteredJointPosition = filteredJointPosition*(1.0f - DataSmoothingFactor) +
                                  (prevFilteredJointPosition + previousTrend)*DataSmoothingFactor;

          differenceVector = filteredJointPosition - prevFilteredJointPosition;
          currentTrend = differenceVector*TrendSmoothingFactor + previousTrend*(1.0f - TrendSmoothingFactor);
          break;
      }

      // Compute potential new position
      Vector2 potentialNewPosition = filteredJointPosition + currentTrend*PredictionFactor;

      // Cache current value
      savedBasePosition = baseJointPosition;
      savedFilteredJointPosition = filteredJointPosition;
      savedTrend = currentTrend;
      frameCount++;

      return new Position(potentialNewPosition.X, potentialNewPosition.Y);
    }
  }
}