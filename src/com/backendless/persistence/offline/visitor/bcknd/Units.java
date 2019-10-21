package com.backendless.persistence.offline.visitor.bcknd;

public enum Units
{
    KILOMETERS(1000), MILES(1609.344), METERS(1), FEET(0.3048), YARDS(0.9144);

    private double metersInUnit;

    private Units( double metersInUnit )
    {
        this.metersInUnit = metersInUnit;
    }

    public double getMetersInUnit()
    {
        return metersInUnit;
    }
}
