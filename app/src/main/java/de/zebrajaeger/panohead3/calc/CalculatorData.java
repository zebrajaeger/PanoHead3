package de.zebrajaeger.panohead3.calc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class CalculatorData implements Serializable {
    private FovO2D camFov;
    private Bounds2D panoBounds;
    private Pos2D offset;

    public CalculatorData(Fov2D camFov, Overlap overlap, Bounds2D panoBounds, Pos2D offset) {
        this.camFov = new FovO2D(camFov, overlap);
        this.panoBounds = panoBounds;
        this.offset = offset;
    }


    public FovO2D getCamFov() {
        return camFov;
    }

    public Bounds2D getPanoBounds() {
        return panoBounds;
    }

    public Pos2D getOffset() {
        return offset;
    }

    public float getXOffsetOr(float alternative){
        if(hasXOffset()){
            return getOffset().getX();
        }else{
            return alternative;
        }
    }
    public float getYOffsetOr(float alternative){
        if(hasYOffset()){
            return getOffset().getY();
        }else{
            return alternative;
        }
    }

    public boolean hasX() {
        return camFov != null && camFov.hasX() && panoBounds != null && panoBounds.hasX();
    }

    public boolean hasY() {
        return camFov != null && camFov.hasY() && panoBounds != null && panoBounds.hasY();
    }

    public boolean hasXOffset() {
        return offset != null && offset.hasX();
    }

    public boolean hasYOffset() {
        return offset != null && offset.hasY();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
