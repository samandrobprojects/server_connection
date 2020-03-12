package com.example.server_connection;
/**
 * This final class represents the server connection that request and responses will pass through
 *
 * @author  Rob
 * @author  Sam
 */
import functional.Maybe;

final public class ServerConnection {

    /**
     * This public abstract class represents the Server Connection protocol implementation
     *
     * @author  Rob
     * @author  Sam
     */
    public abstract class ServerConnectionProtocol {

        /**
         * This function implements sending a string request to the server with a given callback
         *
         * @param  givenStringRequest the string request
         * @param  givenStringResponseCallback the response callback
         */
        abstract void sendStringRequestWithStringResponseCallback(String givenStringRequest, ResponseCallback<String> givenStringResponseCallback);
    }

    /**
     * Static constructor for Server Connection
     *
     * @param  givenServerConnectionProtocol the given ServerConnectionProtocol
     * @return new instance of ServerConnection
     */
    public static ServerConnection serverConnectionWithProtocol(ServerConnectionProtocol givenServerConnectionProtocol) {
        return new ServerConnection(givenServerConnectionProtocol);
    }

    /**
     * Send request to server with given callback and conversions
     *
     * @param  givenRequest the request object
     * @param  givenResponseCallback the response callback
     * @param  givenRequestConversion the two way conversion between a string and the request object type
     * @param  givenResponseConversion the two way conversion between a string and the response object type
     */
    public <REQUEST_TRANSMISSION_UNIT, RESPONSE_TRANSMISSION_UNIT>
        void sendRequestWithResponseCallbackAndRequestResponseConversions(
                final REQUEST_TRANSMISSION_UNIT givenRequest,
                final ResponseCallback<RESPONSE_TRANSMISSION_UNIT> givenResponseCallback,
                final TwoWayConversion<REQUEST_TRANSMISSION_UNIT, String> givenRequestConversion,
                final TwoWayConversion<RESPONSE_TRANSMISSION_UNIT, String> givenResponseConversion) {
        Maybe<String> maybeStringRequest = givenRequestConversion.maybeConvertLeftToRight(givenRequest);
        if (maybeStringRequest.isNotNothing()) {
            String stringRequest = maybeStringRequest.object();
            ResponseCallback<String> stringResponseCallbackFromTransmissionObjectResponseCallbackUsingGivenResponseConversion =  _stringResponseCallbackFromTransmissionObjectResponseCallbackByUsingGivenResponseConversion(givenResponseCallback, givenResponseConversion);
            this.sendStringRequestWithStringResponseCallback(stringRequest, stringResponseCallbackFromTransmissionObjectResponseCallbackUsingGivenResponseConversion);
        } else {
            givenResponseCallback.onFailure();
        }
    }

    private final ServerConnectionProtocol _serverConnectionProtocol;
    private ServerConnection(ServerConnectionProtocol givenServerConnectionProtocol) {
        _serverConnectionProtocol = givenServerConnectionProtocol;
    }

    private void sendStringRequestWithStringResponseCallback(String givenStringRequest, ResponseCallback<String> givenStringResponseCallback) {
        _serverConnectionProtocol.sendStringRequestWithStringResponseCallback(givenStringRequest, givenStringResponseCallback);
    }

    private <RESPONSE_TRANSMISSION_UNIT> ResponseCallback<String> _stringResponseCallbackFromTransmissionObjectResponseCallbackByUsingGivenResponseConversion(
            final ResponseCallback<RESPONSE_TRANSMISSION_UNIT> givenResponseCallback,
            final TwoWayConversion<RESPONSE_TRANSMISSION_UNIT, String> givenResponseConversion) {
        return new ResponseCallback<String>() {
            @Override
            public void onSuccessWithResponse(String responseString) {
                Maybe<RESPONSE_TRANSMISSION_UNIT> response = givenResponseConversion.convertRightToLeft(responseString);
                if (response.isNotNothing()) {
                    givenResponseCallback.onSuccessWithResponse(response.object());
                } else {
                    givenResponseCallback.onFailure();
                }
            }
            @Override
            public void onFailure() {
                givenResponseCallback.onFailure();
            }
        };
    }
}
