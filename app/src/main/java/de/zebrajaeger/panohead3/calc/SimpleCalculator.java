package de.zebrajaeger.panohead3.calc;

import java.util.LinkedList;
import java.util.List;

import de.zebrajaeger.panohead3.shot.ShooterScript;
import de.zebrajaeger.panohead3.shot.ShotPosition;


/**
 * @author lars on 23.10.2016.
 */
public class SimpleCalculator {

  public ShooterScript createScript(CalculatorData data) {
    boolean hasX = data.hasX();
    boolean hasY = data.hasY();
    float[] xValues = hasX
        ? createValuesX(data.getCamFov().getX(), data.getPanoBounds().getX() )
        : null;
    float[] yValues = data.hasY()
        ? createValuesY(data.getCamFov().getY(), data.getPanoBounds().getY())
        : null;

    List<ShotPosition> result = new LinkedList<>();
    if (hasX && hasY) {
      for (float y : yValues) {
        for (float x : xValues) {
          result.add(new ShotPosition(x, y));
        }
      }
    } else if (hasX) {
      for (float x : xValues) {
        result.add(new ShotPosition(x, 0f));
      }
    } else if (hasY) {
      for (float y : yValues) {
        result.add(new ShotPosition(y, 0f));
      }
    }

    return new ShooterScript(data, result);
  }

  private float[] createValuesX(FovO1D camFov, Bounds1D panoRange) {
    return panoRange.getSweepAngle()==360f
        ? createValuesFull(camFov, panoRange)
        : createValuesPartial(camFov, panoRange);
  }

  private float[] createValuesY(FovO1D camFov, Bounds1D panoRange) {
    return panoRange.getSweepAngle()==180f
        ? createValuesFull(camFov, panoRange)
        : createValuesPartial(camFov, panoRange);
  }

  private float[] createValuesFull(FovO1D camFov, Bounds1D panoRange) {
    float[] result;
    // Full circle pano. Set range to 360 to prevent a range > 360 degree
    float imgCount = (float) Math.floor(panoRange.getSweepAngle() / camFov.getNonOverlapingAngle());
    int n = (int) imgCount;
    result = new float[n];

    if (imgCount == 1) {
      // Single Image -> take center of pano
      // Should be not occur on a full circle
      result[0] = panoRange.getCenter();
    } else {
      // Multible images -> start at zero
      float imgSize = panoRange.getSweepAngle() / imgCount;
      float off = imgSize / 2f;
      for (int i = 0; i < n; ++i) {
        result[i] = i;
        result[i] *= imgSize ;
        result[i] += off ;
      }
    }
    return result;
  }

  private float[] createValuesPartial(FovO1D camFov, Bounds1D panoRange) {
    float[] result;

    // Partial pano
    // One overlapping area fewer than full round. We remove it from range for a more easy calculation
    float range = panoRange.getSweepAngle() - camFov.getOverlapingAngle();
    float imgCount = (float) Math.ceil(range / camFov.getNonOverlapingAngle());
    int n = (int) imgCount;
    result = new float[n];

    if (imgCount == 1) {
      // Single Image -> take center of pano
      result[0] = panoRange.getCenter();
    } else {
      // Multible images -> start and end at half a image
      float halfImage = camFov.getSweepAngle() / 2f;
      result[0] = panoRange.getB1() + halfImage;
      result[result.length - 1] = panoRange.getB2() - halfImage;
      float remainingAngle = result[result.length - 1] - result[0];
      float distance = remainingAngle / (float) (n - 1);
      for (int i = 1; i < (n - 1); ++i) {
        result[i] = result[i - 1] + distance;
      }
    }
    return result;
  }

}
