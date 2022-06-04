package org.bizzdeskgroup.models;

public class PosMeta extends BaseModel{

    /***
     * CREATE TABLE `posmetas` (
     *   `Id` int(11) NOT NULL AUTO_INCREMENT,
     *   `Os` text,
     *   `OsVer` text,
     *   `Man` text,
     *   `Model` text,
     *   `SimSlot` text,
     *   `PrinterSize` text,
     *   `Printer` text,
     *   `Battery` text,
     *   `Charger` text,
     *   `CreatedBy` int,
     *   `CreatedDate` timestamp,
     *   PRIMARY KEY (`Id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
     * SELECT * FROM eigr_taraba.assessment;
     */
    public String os;
    public String osVer;
    public String man;
    public String model;
    public String simSlot;
    public String printerSize;
    public String printer;
    public String battery;
    public String charger;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVer() {
        return osVer;
    }

    public void setOsVer(String osVer) {
        this.osVer = osVer;
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSimSlot() {
        return simSlot;
    }

    public void setSimSlot(String simSlot) {
        this.simSlot = simSlot;
    }

    public String getPrinterSize() {
        return printerSize;
    }

    public void setPrinterSize(String printerSize) {
        this.printerSize = printerSize;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getCharger() {
        return charger;
    }

    public void setCharger(String charger) {
        this.charger = charger;
    }
}
