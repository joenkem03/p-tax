package org.bizzdeskgroup.models;

/***
 * CREATE TABLE `posmetas` (
 *   `Id` int(11) NOT NULL AUTO_INCREMENT,
 *   `PosMetaId` int(11)
 *   `PosImei` text,
 *   `SerialNo` text,
 *   `IsAssigned` tinyint(1),
 *   `ActivationCode` text,
 *   `MdaId` int(11),
 *   `MdaOfficeId` int(11),
 *   `LoggedInUser` int(11),
 *   `LastLoggedInUser` int(11),
 *   `IsActive` boolean,
 *      *   `CreatedBy` int,
 *      *   `CreatedDate` timestamp,
 *      *   `UpdatedBy` int,
 *      *   `UpdatedDate` timestamp,
 *   PRIMARY KEY (`Id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
 * SELECT * FROM eigr_taraba.assessment;
 */
public class Pos extends AuditedBaseModel{
    public int posMetaId;
    public String posImei;
    public String serialNo;
    public boolean isAssigned;
    public String activationCode;
    public int mdaId;
    public int mdaOfficeId;
    public int loggedInUser;
    public int lastLoggedInUser;
    public String posDeviceSerial;


    public int getPosMetaId() {
        return posMetaId;
    }

    public void setPosMetaId(int posMetaId) {
        this.posMetaId = posMetaId;
    }

    public String getPosImei() {
        return posImei;
    }

    public void setPosImei(String posImei) {
        this.posImei = posImei;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public int getMdaOfficeId() {
        return mdaOfficeId;
    }

    public void setMdaOfficeId(int mdaOfficeId) {
        this.mdaOfficeId = mdaOfficeId;
    }

    public int getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(int loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public int getLastLoggedInUser() {
        return lastLoggedInUser;
    }

    public void setLastLoggedInUser(int lastLoggedInUser) {
        this.lastLoggedInUser = lastLoggedInUser;
    }

    public String getPosDeviceSerial() {
        return posDeviceSerial;
    }

    public void setPosDeviceSerial(String posDeviceSerial) {
        this.posDeviceSerial = posDeviceSerial;
    }
}
