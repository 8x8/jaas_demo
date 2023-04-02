package com._8x8_;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.interfaces.*;
import java.util.UUID;

/**
 * The following sample code helps generate a JaaS JWT.
 */
public class Main {

    /**
     * The file that contains the generated rsa private key. See https://jaas.8x8.vc/#/start-guide Section 1. API Keys
     * The file must be in PKCS #8 format supported by JAVA, also see https://tools.ietf.org/html/rfc5208.
     * Use the following command to convert the generated private key to PKCS #8 :
     * openssl pkcs8 -topk8 -inform PEM -in <input key path> -outform pem -nocrypt -out <output key path>
     * */
    private static final String PRIVATE_KEY_FILE_RSA = "src/test/resources/jaas-key.pk";

    /** Placeholder helper string. */
    public static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";

    /** Placeholder helper string. */
    public static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";

    /** Placeholder empty string. */
    public static final String EMPTY = "";

    /** Helper string for key algorithm. */
    public static final String RSA = "RSA";

    /**
     * To be used with exp value.
     * The time after which the JWT expires.
     * */
    public static final long EXP_TIME_DELAY_SEC = 7200;

    /**
     * To be used with nbf value.
     * The time before which the JWT must not be accepted for processing.
     * */
    public static final long NBF_TIME_DELAY_SEC = 10;

    /**
     * JaaSJwtBuilder class that helps generate JaaS tokens.
     */
    static class JaaSJwtBuilder {
        private JWTCreator.Builder jwtBuilder;
        private Algorithm algorithm;
        private Map<String, Object> userClaims;
        private Map<String, Object> featureClaims;

        private JaaSJwtBuilder() {
            userClaims = new HashMap<>();
            featureClaims = new HashMap<>();
            jwtBuilder = JWT.create();
        }

        /**
         * Creates a new JaaSJwtBuilder.
         * @return Returns a new builder that needs to be setup.
         */
        private static JaaSJwtBuilder builder() {
            return new JaaSJwtBuilder();
        }

        /**
         * Sets the value for the kid header claim. Represents the JaaS api key.
         * You can find the api key here : https://jaas.8x8.vc/#/apikeys
         * @param apiKey
         * @return Returns a new builder with kid claim set.
         */
        public JaaSJwtBuilder withApiKey(String apiKey) {
            jwtBuilder.withKeyId(apiKey);
            return this;
        }

        /**
         * Sets the value for the user avatar url as a string.
         * @param url The publicly available URL that points to the user avatar picture.
         * @return Returns a new builder with avatar claim set.
         */
        public JaaSJwtBuilder withUserAvatar(String url) {
            userClaims.put("avatar", url);
            return this;
        }

        /**
         * Sets the value for the moderator claim. If value is true user is moderator, false otherwise.
         * @param isModerator
         * @return Returns a new builder with moderator claim set.
         */
        public JaaSJwtBuilder withModerator(boolean isModerator) {
            userClaims.put("moderator", String.valueOf(isModerator));
            return this;
        }

        /**
         * Sets the value for the user name to be displayed in the meeting.
         * @param userName
         * @return Returns a new builder with name claim set.
         */
        public JaaSJwtBuilder withUserName(String userName) {
            userClaims.put("name", userName);
            return this;
        }

        /**
         * Sets the value for the user email claim.
         * @param userEmail
         * @return Returns a new builder with email claim set.
         */
        public JaaSJwtBuilder withUserEmail(String userEmail) {
            userClaims.put("email", userEmail);
            return this;
        }

        /**
         * Sets the value for the live streaming feature claim. If value is true recording is enabled, false otherwise.
         * @param isEnabled
         * @return Returns a new builder with livestreaming claim set.
         */
        public JaaSJwtBuilder withLiveStreamingEnabled(boolean isEnabled) {
            featureClaims.put("livestreaming", String.valueOf(isEnabled));
            return this;
        }

        /**
         * Sets the value for the recording feature claim. If value is true recording is enabled, false otherwise.
         * @param isEnabled
         * @return Returns a new builder with recording claim set.
         */
        public JaaSJwtBuilder withRecordingEnabled(boolean isEnabled) {
            featureClaims.put("recording", String.valueOf(isEnabled));
            return this;
        }

        /**
         * Sets the value for the outbound feature claim. If value is true outbound calls are enabled, false otherwise.
         * @param isEnabled
         * @return Returns a new builder with outbound-call claim set.
         */
        public JaaSJwtBuilder withOutboundEnabled(boolean isEnabled) {
            featureClaims.put("outbound-call", String.valueOf(isEnabled));
            return this;
        }

        /**
         * Sets the value for the transcription feature claim. If value is true transcription is enabled, false otherwise.
         * @param isEnabled
         * @return Returns a new builder with transcription claim set.
         */
        public JaaSJwtBuilder withTranscriptionEnabled(boolean isEnabled) {
            featureClaims.put("transcription", String.valueOf(isEnabled));
            return this;
        }

        /**
         * Sets the value for the exp claim representing the time until the token expires.
         * @param expTime Unix timestamp is expected.
         * @return Returns a new builder with exp claim set.
         */
        public JaaSJwtBuilder withExpTime(long expTime) {
            jwtBuilder.withClaim("exp", expTime);
            return this;
        }

        /**
         * Sets the value for the nbf claim.
         * @param nbfTime Unix timestamp is expected.
         * @return Returns a new builder with nbf claim set.
         */
        public JaaSJwtBuilder withNbfTime(long nbfTime) {
            jwtBuilder.withClaim("nbf", nbfTime);
            return this;
        }

        /**
         * Sets the value for the room.
         * @param roomName The meeting room value for which the token is issued;
         *                 this field supports also wildcard ("*") if the token is issued for all rooms.
         * @return Returns a new builder with room claim set.
         */
        public JaaSJwtBuilder withRoomName(String roomName) {
            jwtBuilder.withClaim("room", roomName);
            return this;
        }

        /**
         * Sets the value for the sub claim representing the AppID (previously tenant name/unique identifier).
         * @param appId The AppID that identifies your application.
         * @return Returns a new builder with sub claim set.
         */
        public JaaSJwtBuilder withAppID(String appId) {
            jwtBuilder.withClaim("sub", appId);
            return this;
        }

        /**
         * Sets the value for the id claim.
         * @param userId The user's unique identifier.
         * @return Returns a new builder with kid claim set.
         */
        public JaaSJwtBuilder withUserId(String userId) {
            userClaims.put("id", userId);
            return this;
        }

        /**
         * Fills the default values for required claims.
         * @return Returns a new builder with needed claim set to default values.
         */
        public JaaSJwtBuilder withDefaults() {
            return this.withExpTime(Instant.now().getEpochSecond() + EXP_TIME_DELAY_SEC) // Default value, no change needed.
                    .withNbfTime(Instant.now().getEpochSecond() - NBF_TIME_DELAY_SEC) // Default value, no change needed.
                    .withLiveStreamingEnabled(true)
                    .withRecordingEnabled(true)
                    .withModerator(true)
                    .withRoomName("*")
                    .withUserId(UUID.randomUUID().toString());
        }

        /**
         * Returns a signed JaaS JWT token string.
         * @param privateKey The private key used to sign the JWT.
         * @return A signed JWT.
         */
        public String signWith(RSAPrivateKey privateKey) {
            algorithm = Algorithm.RSA256(null, privateKey);
            Map<String, Object> context = new HashMap<String, Object>() {{
                put("user", userClaims);
                put("features", featureClaims);
            }};

            return jwtBuilder
                    .withClaim("iss", "chat")
                    .withClaim("aud", "jitsi")
                    .withClaim("context", context)
                    .sign(this.algorithm);
        }
    }

    /**
     * Reads a private key from a file in PKCS #8 format.
     * @param filename
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey getPemPrivateKey(String filename) throws Exception {
        String pem = new String(Files.readAllBytes(Paths.get(filename)));
        String privKey = pem.replace(BEGIN_PRIVATE_KEY, EMPTY).replace(END_PRIVATE_KEY, EMPTY);
        byte[] decoded = Base64.getDecoder().decode(privKey);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    public static void main(String[] args) {
        try
        {
            /** Read private key from file. */
            RSAPrivateKey rsaPrivateKey = getPemPrivateKey(PRIVATE_KEY_FILE_RSA);

            /** Create new JaaSJwtBuilder and setup the claims. */
            String token = JaaSJwtBuilder.builder()
                    .withDefaults() // This sets default/most common values
                    .withApiKey("my api key") // Set the api key
                    .withUserName("My name here") // Set the user name
                    .withUserEmail("My email here") // Set the user email
                    .withModerator(true) // Enable user as moderator
                    .withOutboundEnabled(true) // Enable outbound calls
                    .withTranscriptionEnabled(true) // Enable transcription
                    .withAppID("my AppID") // Set the AppID
                    .withUserAvatar("https://avatarurl.com/avatar/url") // Set the user avatar
                    .signWith(rsaPrivateKey); /** Finally the JWT is signed with the private key */

            System.out.println(token);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
