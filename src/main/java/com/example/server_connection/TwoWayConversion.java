package com.example.server_connection;
/**
 * This abstract class represents a two way conversion between two given object types, where each conversion has the possibility of failure
 *
 * @author  Rob
 * @author  Sam
 */
import functional.Maybe;
import functional.Monad;
import functional.MonadicOperation;

public abstract class TwoWayConversion<LEFT_OBJECT, RIGHT_OBJECT> {

    /**
     * Convert object of type given by left generic type parameter to object of type given by right generic type parameter
     *
     * @param  leftObject the object to be transformed
     * @return maybe the transformed object
     */
    public abstract Maybe<RIGHT_OBJECT> maybeConvertLeftToRight(LEFT_OBJECT leftObject);

    /**
     * Convert object of type given by right generic type parameter to object of type given by left generic type parameter
     *
     * @param  rightObject the object to be transformed
     * @return maybe the transformed object
     */
    public abstract Maybe<LEFT_OBJECT> convertRightToLeft(RIGHT_OBJECT rightObject);

    /**
     * Create a transitive conversion between three types
     *
     * @param  otherConversionToTheRightOfThisOne the conversion to add to this connection transitively
     * @return the transitive conversion
     */
    public <OTHER_RIGHT_OBJECT> TwoWayConversion<LEFT_OBJECT, OTHER_RIGHT_OBJECT> conversionByConnectingTransitivelyTo(final TwoWayConversion<RIGHT_OBJECT, OTHER_RIGHT_OBJECT> otherConversionToTheRightOfThisOne) {
        return new TwoWayConversion<LEFT_OBJECT, OTHER_RIGHT_OBJECT>() {
            @Override
            public Maybe<LEFT_OBJECT> convertRightToLeft(OTHER_RIGHT_OBJECT rightestObject) {
                return otherConversionToTheRightOfThisOne.convertRightToLeft(rightestObject).applyGivenOperationOntoThisObjectMondically(new MonadicOperation<Monad<LEFT_OBJECT>, RIGHT_OBJECT, LEFT_OBJECT>() {
                    @Override
                    public Maybe<LEFT_OBJECT> performMonadicOperation(RIGHT_OBJECT middleObject) {
                        return TwoWayConversion.this.convertRightToLeft(middleObject);
                    }
                });
            }
            @Override
            public Maybe<OTHER_RIGHT_OBJECT> maybeConvertLeftToRight(LEFT_OBJECT leftestObject) {
                return TwoWayConversion.this.maybeConvertLeftToRight(leftestObject).applyGivenOperationOntoThisObjectMondically(new MonadicOperation<Monad<OTHER_RIGHT_OBJECT>, RIGHT_OBJECT, OTHER_RIGHT_OBJECT>() {
                    @Override
                    public Maybe<OTHER_RIGHT_OBJECT> performMonadicOperation(RIGHT_OBJECT middleObject) {
                        return otherConversionToTheRightOfThisOne.maybeConvertLeftToRight(middleObject);
                    }
                });
            }
        };
    }
}
