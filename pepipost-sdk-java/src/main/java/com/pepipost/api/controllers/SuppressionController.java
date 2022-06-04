/*
 * PepipostLib
 *
 * This file was automatically generated by APIMATIC v2.0 ( https://apimatic.io ).
 */
package com.pepipost.api.controllers;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.pepipost.api.*;
import com.pepipost.api.models.*;
import com.pepipost.api.exceptions.*;
import com.pepipost.api.http.client.HttpClient;
import com.pepipost.api.http.client.HttpContext;
import com.pepipost.api.http.request.HttpRequest;
import com.pepipost.api.http.response.HttpResponse;
import com.pepipost.api.http.response.HttpStringResponse;
import com.pepipost.api.http.client.APICallBack;
import com.pepipost.api.controllers.syncwrapper.APICallBackCatcher;

public class SuppressionController extends BaseController {
    //private static variables for the singleton pattern
    private static final Object syncObject = new Object();
    private static SuppressionController instance = null;

    /**
     * Singleton pattern implementation 
     * @return The singleton instance of the SuppressionController class 
     */
    public static SuppressionController getInstance() {
        if (null == instance) {
            synchronized (syncObject) {
                if (null == instance) {
                    instance = new SuppressionController();
                }
            }
        }
        return instance;
    }

    /**
     * This API allows you to suppress an email address and block any future email delivery attempts on this email address.
     * @param    body    Required parameter: add email or domain to suppression list
     * @return    Returns the Object response from the API call 
     */
    public Object adddomainoremailtosuppressionlist(
                final AddemailordomaintoSuppressionlist body
    ) throws Throwable {

        HttpRequest _request = _buildAdddomainoremailtosuppressionlistRequest(body);
        HttpResponse _response = getClientInstance().executeAsString(_request);
        HttpContext _context = new HttpContext(_request, _response);

        return _handleAdddomainoremailtosuppressionlistResponse(_context);
    }

    /**
     * This API allows you to suppress an email address and block any future email delivery attempts on this email address.
     * @param    body    Required parameter: add email or domain to suppression list
     */
    public void adddomainoremailtosuppressionlistAsync(
                final AddemailordomaintoSuppressionlist body,
                final APICallBack<Object> callBack
    ) {
        Runnable _responseTask = new Runnable() {
            public void run() {

                HttpRequest _request;
                try {
                    _request = _buildAdddomainoremailtosuppressionlistRequest(body);
                } catch (Exception e) {
                    callBack.onFailure(null, e);
                    return;
                }

                // Invoke request and get response
                getClientInstance().executeAsStringAsync(_request, new APICallBack<HttpResponse>() {
                    public void onSuccess(HttpContext _context, HttpResponse _response) {
                        try {
                            Object returnValue = _handleAdddomainoremailtosuppressionlistResponse(_context);
                            callBack.onSuccess(_context, returnValue);
                        } catch (Exception e) {
                            callBack.onFailure(_context, e);
                        }
                    }

                    public void onFailure(HttpContext _context, Throwable _exception) {
                        // Let the caller know of the failure
                        callBack.onFailure(_context, _exception);
                    }
                });
            }
        };

        // Execute async using thread pool
        APIHelper.getScheduler().execute(_responseTask);
    }

    /**
     * Builds the HttpRequest object for adddomainoremailtosuppressionlist
     */
    private HttpRequest _buildAdddomainoremailtosuppressionlistRequest(
                final AddemailordomaintoSuppressionlist body) throws IOException, APIException {
        //the base uri for api requests
        String _baseUri = Configuration.baseUri;

        //prepare query string for API call
        StringBuilder _queryBuilder = new StringBuilder(_baseUri + "/suppression");
        //validate and preprocess url
        String _queryUrl = APIHelper.cleanUrl(_queryBuilder);

        //load all headers for the outgoing API request
        Map<String, String> _headers = new HashMap<String, String>();
        _headers.put("api_key", Configuration.apiKey);
        _headers.put("user-agent", BaseController.userAgent);
        _headers.put("content-type", "application/json");


        //prepare and invoke the API call request to fetch the response
        HttpRequest _request = getClientInstance().postBody(_queryUrl, _headers, APIHelper.serialize(body));

        // Invoke the callback before request if its not null
        if (getHttpCallBack() != null) {
            getHttpCallBack().OnBeforeRequest(_request);
        }

        return _request;
    }

    /**
     * Processes the response for adddomainoremailtosuppressionlist
     * @return An object of type Object
     */
    private Object _handleAdddomainoremailtosuppressionlistResponse(HttpContext _context)
            throws APIException, IOException {
        HttpResponse _response = _context.getResponse();

        //invoke the callback after response if its not null
        if (getHttpCallBack() != null) {
            getHttpCallBack().OnAfterResponse(_context);
        }

        //Error handling using HTTP status codes
        int _responseCode = _response.getStatusCode();

        if (_responseCode == 400) {
            throw new APIException("API Response", _context);
        }
        if (_responseCode == 401) {
            throw new APIException("API Response", _context);
        }
        if (_responseCode == 403) {
            throw new APIException("API Response", _context);
        }
        if (_responseCode == 405) {
            throw new APIException("Invalid input", _context);
        }
        //handle errors defined at the API level
        validateResponse(_response, _context);

        //extract result from the http response
        String _responseBody = ((HttpStringResponse)_response).getBody();
        Object _result = _responseBody;

        return _result;
    }

    /**
     * Use this API to remove an email address or a recipient domain from the suppression list. You can remove multiple email addresses and recipient domains together by passing them as values & separating them using commas as shown below.
     * @param    body    Required parameter: remove email or domain to suppression list
     * @return    Returns the Object response from the API call 
     */
    public Object removedomainoremailtosuppressionlist(
                final RemoveemailordomaintoSuppressionlist body
    ) throws Throwable {

        HttpRequest _request = _buildRemovedomainoremailtosuppressionlistRequest(body);
        HttpResponse _response = getClientInstance().executeAsString(_request);
        HttpContext _context = new HttpContext(_request, _response);

        return _handleRemovedomainoremailtosuppressionlistResponse(_context);
    }

    /**
     * Use this API to remove an email address or a recipient domain from the suppression list. You can remove multiple email addresses and recipient domains together by passing them as values & separating them using commas as shown below.
     * @param    body    Required parameter: remove email or domain to suppression list
     */
    public void removedomainoremailtosuppressionlistAsync(
                final RemoveemailordomaintoSuppressionlist body,
                final APICallBack<Object> callBack
    ) {
        Runnable _responseTask = new Runnable() {
            public void run() {

                HttpRequest _request;
                try {
                    _request = _buildRemovedomainoremailtosuppressionlistRequest(body);
                } catch (Exception e) {
                    callBack.onFailure(null, e);
                    return;
                }

                // Invoke request and get response
                getClientInstance().executeAsStringAsync(_request, new APICallBack<HttpResponse>() {
                    public void onSuccess(HttpContext _context, HttpResponse _response) {
                        try {
                            Object returnValue = _handleRemovedomainoremailtosuppressionlistResponse(_context);
                            callBack.onSuccess(_context, returnValue);
                        } catch (Exception e) {
                            callBack.onFailure(_context, e);
                        }
                    }

                    public void onFailure(HttpContext _context, Throwable _exception) {
                        // Let the caller know of the failure
                        callBack.onFailure(_context, _exception);
                    }
                });
            }
        };

        // Execute async using thread pool
        APIHelper.getScheduler().execute(_responseTask);
    }

    /**
     * Builds the HttpRequest object for removedomainoremailtosuppressionlist
     */
    private HttpRequest _buildRemovedomainoremailtosuppressionlistRequest(
                final RemoveemailordomaintoSuppressionlist body) throws IOException, APIException {
        //the base uri for api requests
        String _baseUri = Configuration.baseUri;

        //prepare query string for API call
        StringBuilder _queryBuilder = new StringBuilder(_baseUri + "/suppression");
        //validate and preprocess url
        String _queryUrl = APIHelper.cleanUrl(_queryBuilder);

        //load all headers for the outgoing API request
        Map<String, String> _headers = new HashMap<String, String>();
        _headers.put("api_key", Configuration.apiKey);
        _headers.put("user-agent", BaseController.userAgent);
        _headers.put("content-type", "application/json");


        //prepare and invoke the API call request to fetch the response
        HttpRequest _request = getClientInstance().deleteBody(_queryUrl, _headers, APIHelper.serialize(body));

        // Invoke the callback before request if its not null
        if (getHttpCallBack() != null) {
            getHttpCallBack().OnBeforeRequest(_request);
        }

        return _request;
    }

    /**
     * Processes the response for removedomainoremailtosuppressionlist
     * @return An object of type Object
     */
    private Object _handleRemovedomainoremailtosuppressionlistResponse(HttpContext _context)
            throws APIException, IOException {
        HttpResponse _response = _context.getResponse();

        //invoke the callback after response if its not null
        if (getHttpCallBack() != null) {
            getHttpCallBack().OnAfterResponse(_context);
        }

        //Error handling using HTTP status codes
        int _responseCode = _response.getStatusCode();

        if (_responseCode == 400) {
            throw new APIException("API Response", _context);
        }
        if (_responseCode == 401) {
            throw new APIException("API Response", _context);
        }
        if (_responseCode == 403) {
            throw new APIException("API Response", _context);
        }
        if (_responseCode == 405) {
            throw new APIException("Invalid input", _context);
        }
        //handle errors defined at the API level
        validateResponse(_response, _context);

        //extract result from the http response
        String _responseBody = ((HttpStringResponse)_response).getBody();
        Object _result = _responseBody;

        return _result;
    }

}
