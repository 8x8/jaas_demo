# JaaS JWT

This following demonstrates how to generate a JaaS JWT using PHP.

### Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)

## Dependencies

**System requirements : PHP 7.3+ and above**

To install dependencies go into your project directory and install composer using the guide from [Download Composer](https://getcomposer.org/download/)

After Composer is installed in your project directory run the following command:

```
./composer.phar update
```

## Generate JWT

Change the the claim values in the payload as shown below or update the variables below line 20 in jaas-jwt.php

```
$payload = json_encode([
    'iss' => 'chat',
    'aud' => 'jitsi',
    'exp' => time() + $EXP_DELAY_SEC,
    'nbf' => time() - $NBF_DELAY_SEC,
    'room'=> '*',
    'sub' => $APP_ID,
    'context' => [
        'user' => [
            'moderator' => $USER_IS_MODERATOR ? "true" : "false",
            'email' => $USER_EMAIL,
            'name' => $USER_NAME,
            'avatar' => $USER_AVATAR_URL,
            'id' => $USER_ID
        ],
        'features' => [
            'recording' => $RECORDING_IS_ENABLED ? "true" : "false",
            'livestreaming' => $LIVESTREAMING_IS_ENABLED ? "true" : "false",
            'transcription' => $TRANSCRIPTION_IS_ENABLED ? "true" : "false",
            'outbound-call' => $OUTBOUND_IS_ENABLED ? "true" : "false"
        ]
    ]
]);
```

To generate the JWT change the variables as indicated in jaas-jwt.php and run it:

```
./jaas-jwt.php
```

## Using Firebase

jaas-jwt-firebase.php uses the firebase/php-jwt library. To generate a JaaS JWT just call the create_jaas_token using the require parameters :

```
/**
 * Change the variables below.
 */
$API_KEY="my api key";
$APP_ID="my app id"; // Your AppID (previously tenant)
$USER_EMAIL="myemail@email.com";
$USER_NAME="my user name";
$USER_IS_MODERATOR=true;
$USER_AVATAR_URL="";
$USER_ID="my unique user id";
$LIVESTREAMING_IS_ENABLED=true;
$RECORDING_IS_ENABLED=true;
$OUTBOUND_IS_ENABLED=false;
$TRANSCRIPTION_IS_ENABLED=false;
$EXP_DELAY_SEC=7200;
$NBF_DELAY_SEC=0;
///

/// ...

$token = create_jaas_token($API_KEY,
                            $APP_ID,
                            $USER_EMAIL,
                            $USER_NAME,
                            $USER_IS_MODERATOR,
                            $USER_AVATAR_URL,
                            $USER_ID,
                            $LIVESTREAMING_IS_ENABLED,
                            $RECORDING_IS_ENABLED,
                            $OUTBOUND_IS_ENABLED,
                            $TRANSCRIPTION_IS_ENABLED,
                            $EXP_DELAY_SEC,
                            $NBF_DELAY_SEC,
                            $private_key);
```
