package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class AddPosMetaDto {
    @NotEmpty
    @NotBlank
    private String os;
    @NotEmpty
    @NotBlank
    private String osVer;
    @NotEmpty
    @NotBlank
    private String man;
    @NotEmpty
    @NotBlank
    private String model;
    @NotEmpty
    @NotBlank
    @Min(1)
    private String simSlot;
    @NotEmpty
    @NotBlank
    private String printerSize;
    @NotEmpty
    @NotBlank
    private String printer;
    @NotEmpty
    @NotBlank
    private String battery;
    @NotEmpty
    @NotBlank
    private String charger;

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
