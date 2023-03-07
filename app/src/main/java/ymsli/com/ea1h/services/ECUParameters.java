package ymsli.com.ea1h.services;

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM129)
 * @date    24/8/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ECUParameters : This is the POJO class for ECUParameters.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

public class ECUParameters {

    //to prevent multiple instance creation from different threads
    private static final Object LOCK = new Object();

    private static ECUParameters ecuParameters;

    /**
     * constructor is private in order
     * to create singleton instance
     * of this class
     */
    private ECUParameters(){
        isConnected = false;
        isIgnited = false;
        isBrakeApplied = false;
        battery = 0f;
        isHazardActivated = false;
    }

    /**
     * public method to get the
     * singleton instance of this class
     * @return Instance of this class
     */
    public static ECUParameters getEcuParametersInstance(){
        if(ecuParameters==null){
            synchronized (LOCK) {
                ecuParameters = new ECUParameters();
            }
        }
        return ecuParameters;
    }

    private boolean isConnected;//determine is bike is connected or not
    private boolean isIgnited,isHazardActivated; //determine of bike is ignited and hazard is active or not
    private boolean isBrakeApplied; //determine if brakes are applied
    private float battery; //returns the current battery voltage

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isIgnited() {
        return isIgnited;
    }

    public void setIgnited(boolean ignited) {
        isIgnited = ignited;
    }

    public boolean isBrakeApplied() {
        return isBrakeApplied;
    }

    public void setBrakeApplied(boolean brakeApplied) {
        isBrakeApplied = brakeApplied;
    }

    public float getBattery() {
        return battery;
    }

    public void setBattery(float battery) {
        this.battery = battery;
    }

    public void setHazardActivated(boolean isHazardActivated){
        this.isHazardActivated = isHazardActivated;
    }

    public boolean getHazardActivated(){
        return this.isHazardActivated;
    }
}

