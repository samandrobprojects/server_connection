package com.example.server_connection;
/**
 * This interface is used by ServerConnection to implement response callbacks
 *
 * @author  Rob
 * @author  Sam
 */

public interface ResponseCallback<RESPONSE_OBJECT> {

    /**
     * The successful response callback
     *
     * @param responseObject the response object
     */
    public void onSuccessWithResponse(RESPONSE_OBJECT responseObject);

    /**
     * The on failure callback
     */
    public void onFailure();
}
