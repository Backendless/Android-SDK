package com.backendless.persistence.offline.visitor.bcknd;

public class UnitConvertor
{
    public final static double METER_TO_KILOMETER = 1000;
    public final static double METER_TO_MILES = 1609.344;
    public final static double METER_TO_YARD = 0.9144;
    public final static double METER_TO_FOOT = 0.3048;

    public static double convertToMeter(double value, Units unit)
    {
        switch( unit )
        {
            case METERS:
                return value;
            case KILOMETERS:
                return value * METER_TO_KILOMETER;
            case YARDS:
                return value * METER_TO_YARD;
            case FEET:
                return value * METER_TO_FOOT;
            case MILES:
                return value * METER_TO_MILES;
            default:
                throw new RuntimeException( "Unknown unit of measure" );
        }
    }

    public static double convertFromMeter( double value, Units unit )
    {
        switch( unit )
        {
            case METERS:
                return value;
            case KILOMETERS:
                return value / METER_TO_KILOMETER;
            case YARDS:
                return value / METER_TO_YARD;
            case FEET:
                return value / METER_TO_FOOT;
            case MILES:
                return value / METER_TO_MILES;
            default:
                throw new RuntimeException( "Unknown unit of measure" );
        }
    }
}
