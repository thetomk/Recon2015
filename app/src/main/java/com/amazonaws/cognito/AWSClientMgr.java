package com.amazonaws.cognito;

/**
 * Created by tkotlarek on 2/7/15.
 */

// import the CognitoCredentialsProvider object from the auth package

import android.content.Context;
import android.util.Log;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;


public class AWSClientMgr {
    /**
     * This class is used to get clients to the various AWS services. Before
     * accessing a client the credentials should be checked to ensure validity.
     */

        private static final String LOG_TAG = "AWSClientMgr";
        private AmazonDynamoDBClient ddb = null;
        private Context context;
        public AWSClientMgr(Context context) {
            this.context = context;
        }
        public AmazonDynamoDBClient ddb() {
            validateCredentials();
            return ddb;
        }
        public boolean hasCredentials() {
            return (!(Constants.IDENTITY_POOL_ID.equalsIgnoreCase("us-east-1:da5e2cf2-0e74-4b49-95ec-2b51ac70c00c")));
        }
        public void validateCredentials() {
            if (ddb == null) {
                initClients();
            }
        }
        private void initClients() {
            CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                    context,
                    Constants.IDENTITY_POOL_ID,
                    Regions.US_EAST_1);
            ddb = new AmazonDynamoDBClient(credentials);
            ddb.setRegion(Region.getRegion(Regions.US_WEST_2));
        }
        public boolean wipeCredentialsOnAuthError(AmazonServiceException ex) {
            Log.e(LOG_TAG, "Error, wipeCredentialsOnAuthError called" + ex);
            if (
// STS
// http://docs.amazonwebservices.com/STS/latest/APIReference/CommonErrors.html
                    ex.getErrorCode().equals("IncompleteSignature")
                            || ex.getErrorCode().equals("InternalFailure")
                            || ex.getErrorCode().equals("InvalidClientTokenId")
                            || ex.getErrorCode().equals("OptInRequired")
                            || ex.getErrorCode().equals("RequestExpired")
                            || ex.getErrorCode().equals("ServiceUnavailable")
// DynamoDB
// http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/ErrorHandling.html#APIErrorTypes
                            || ex.getErrorCode().equals("AccessDeniedException")
                            || ex.getErrorCode().equals("IncompleteSignatureException")
                            || ex.getErrorCode().equals(
                            "MissingAuthenticationTokenException")
                            || ex.getErrorCode().equals("ValidationException")
                            || ex.getErrorCode().equals("InternalFailure")
                            || ex.getErrorCode().equals("InternalServerError")) {
                return true;
            }
            return false;
        }
    }


