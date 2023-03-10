/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api;

import com.pepipost.api.controllers.*;
import com.pepipost.api.http.client.HttpClient;

public class PepipostClient {
    /**
     * Singleton access to MailSend controller
     * @return	Returns the MailSendController instance 
     */
    public MailSendController getMailSend() {
        return MailSendController.getInstance();
    }

    /**
     * Singleton access to Events controller
     * @return	Returns the EventsController instance 
     */
    public EventsController getEvents() {
        return EventsController.getInstance();
    }

    /**
     * Singleton access to Stats controller
     * @return	Returns the StatsController instance 
     */
    public StatsController getStats() {
        return StatsController.getInstance();
    }

    /**
     * Singleton access to Subaccounts controller
     * @return	Returns the SubaccountsController instance 
     */
    public SubaccountsController getSubaccounts() {
        return SubaccountsController.getInstance();
    }

    /**
     * Singleton access to SubaccountsDelete controller
     * @return	Returns the SubaccountsDeleteController instance 
     */
    public SubaccountsDeleteController getSubaccountsDelete() {
        return SubaccountsDeleteController.getInstance();
    }

    /**
     * Singleton access to SubaccountsGetSubAccounts controller
     * @return	Returns the SubaccountsGetSubAccountsController instance 
     */
    public SubaccountsGetSubAccountsController getSubaccountsGetSubAccounts() {
        return SubaccountsGetSubAccountsController.getInstance();
    }

    /**
     * Singleton access to SubaccountsGetcreditddetails controller
     * @return	Returns the SubaccountsGetcreditddetailsController instance 
     */
    public SubaccountsGetcreditddetailsController getSubaccountsGetcreditddetails() {
        return SubaccountsGetcreditddetailsController.getInstance();
    }

    /**
     * Singleton access to Setrecurringcreditddetails controller
     * @return	Returns the SetrecurringcreditddetailsController instance 
     */
    public SetrecurringcreditddetailsController getSetrecurringcreditddetails() {
        return SetrecurringcreditddetailsController.getInstance();
    }

    /**
     * Singleton access to SubaccountsSetsubaccountcredit controller
     * @return	Returns the SubaccountsSetsubaccountcreditController instance 
     */
    public SubaccountsSetsubaccountcreditController getSubaccountsSetsubaccountcredit() {
        return SubaccountsSetsubaccountcreditController.getInstance();
    }

    /**
     * Singleton access to SubaccountsUpdateSubaccount controller
     * @return	Returns the SubaccountsUpdateSubaccountController instance 
     */
    public SubaccountsUpdateSubaccountController getSubaccountsUpdateSubaccount() {
        return SubaccountsUpdateSubaccountController.getInstance();
    }

    /**
     * Singleton access to SubaccountsCreateSubaccount controller
     * @return	Returns the SubaccountsCreateSubaccountController instance 
     */
    public SubaccountsCreateSubaccountController getSubaccountsCreateSubaccount() {
        return SubaccountsCreateSubaccountController.getInstance();
    }

    /**
     * Singleton access to Suppression controller
     * @return	Returns the SuppressionController instance 
     */
    public SuppressionController getSuppression() {
        return SuppressionController.getInstance();
    }

    /**
     * Singleton access to DomainDelete controller
     * @return	Returns the DomainDeleteController instance 
     */
    public DomainDeleteController getDomainDelete() {
        return DomainDeleteController.getInstance();
    }

    /**
     * Singleton access to DomainGetDomains controller
     * @return	Returns the DomainGetDomainsController instance 
     */
    public DomainGetDomainsController getDomainGetDomains() {
        return DomainGetDomainsController.getInstance();
    }

    /**
     * Singleton access to Template controller
     * @return	Returns the TemplateController instance 
     */
    public TemplateController getTemplate() {
        return TemplateController.getInstance();
    }

    /**
     * Singleton access to Domain controller
     * @return	Returns the DomainController instance 
     */
    public DomainController getDomain() {
        return DomainController.getInstance();
    }

    /**
     * Get the shared http client currently being used for API calls
     * @return The http client instance currently being used
     */
    public HttpClient getSharedHttpClient() {
        return BaseController.getClientInstance();
    }
    
    /**
     * Set a shared http client to be used for API calls
     * @param httpClient The http client to use
     */
    public void setSharedHttpClient(HttpClient httpClient) {
        BaseController.setClientInstance(httpClient);
    }

    /**
     * Default constructor 
     */     
    public PepipostClient() {
    }

    /**
     * Client initialization constructor 
     */     
    public PepipostClient(String apiKey) {
        this();
        Configuration.apiKey = apiKey;
    }
}