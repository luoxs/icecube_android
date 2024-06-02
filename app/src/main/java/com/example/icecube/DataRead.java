package com.example.icecube;

public class DataRead {
    private byte start;
    private byte power;
    private byte tempSetting;
    private byte tempReal;



    private byte froseSetinng; //冷冻湘设定温度
    private byte froseReal;
    private byte turbo;
    private byte mode;
    private byte battery;
    private byte unit;
    private byte status;
    private byte err;
    private byte volhigh;
    private byte vollow;
    private byte type;
    private byte heatSetting;
    private byte reserver1;
    private byte reserver2;
    private byte reserver3;
    private byte crch;
    private byte crcl;
    private byte end;

    private byte[] data;

    public void setData(byte[] data) {
        this.data = data;
        this.power = data[1];
        this.tempSetting = data[2];
        this.tempReal = data[3];
        this.froseSetinng = data[4];
        this.froseReal = data[5];
        this.turbo = data[6];
        this.mode = data[7];
        this.battery = data[7];
        this.unit = data[9];
        this.status = data[10];
        this.err = data[11];
        this.volhigh = data[12];
        this.vollow = data[13];
        this.type = data[14];
        this.heatSetting = data[15];
        this.reserver1 = data[16];
        this.reserver2 = data[17];
        this.reserver3 = data[18];
        this.crch = data[19];
        this.crcl = data[20];
        this.end = data[21];
    }

    public byte getStart() {
        return start;
    }

    public byte getPower() {
        return power;
    }

    public byte getTempSetting() {
        return tempSetting;
    }

    public byte getTempReal() {
        return tempReal;
    }

    public byte getFroseSetinng() {
        return froseSetinng;
    }

    public byte getFroseReal() {
        return froseReal;
    }

    public byte getTurbo() {
        return turbo;
    }

    public byte getMode() {
        return mode;
    }

    public byte getBattery() {
        return battery;
    }

    public byte getUnit() {
        return unit;
    }

    public byte getStatus() {
        return status;
    }

    public byte getErr() {
        return err;
    }

    public byte getVolhigh() {
        return volhigh;
    }

    public byte getVollow() {
        return vollow;
    }

    public byte getType() {
        return type;
    }

    public byte getHeatSetting() {
        return heatSetting;
    }

    public byte getReserver1() {
        return reserver1;
    }

    public byte getReserver2() {
        return reserver2;
    }

    public byte getReserver3() {
        return reserver3;
    }

    public byte getCrch() {
        return crch;
    }

    public byte getCrcl() {
        return crcl;
    }

    public byte getEnd() {
        return end;
    }

    public byte[] getData() {
        return data;
    }
}
