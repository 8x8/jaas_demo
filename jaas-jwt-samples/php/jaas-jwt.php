#!/usr/bin/php
<?php

require __DIR__ . '/vendor/autoload.php';

/**
 * PHP code below is used to generate a JaaS JWT.
 * You can copy the code below in your implementation.
 */
use Jose\Component\Core\AlgorithmManager;
use Jose\Component\Core\JWK;
use Jose\Component\Signature\Algorithm\RS256;
use Jose\Component\Signature\JWSBuilder;
use Jose\Component\KeyManagement\JWKFactory;
use Jose\Component\Signature\Serializer\CompactSerializer;

/**
 * Change the variables below.
 */
$API_KEY="my api key";
$APP_ID="my AppID"; // Your AppID (previously tenant)
$USER_EMAIL="myemail@email.com";
$USER_NAME="my user name";
$USER_IS_MODERATOR=true;
$USER_AVATAR_URL="";
$USER_ID="b2c94a50-e53b-4afc-8bef-3132f3ec27dc";
$LIVESTREAMING_IS_ENABLED=false;
$RECORDING_IS_ENABLED=true;
$OUTBOUND_IS_ENABLED=false;
$TRANSCRIPTION_IS_ENABLED=false;
$EXP_DELAY_SEC=7200;
$NBF_DELAY_SEC=10;
///

/**
 * We read the JSON Web Key (https://tools.ietf.org/html/rfc7517) 
 * from the private key we generated at https://jaas.8x8.vc/#/apikeys .
 * 
 * @var \Jose\Component\Core\JWK jwk
 */
$jwk = JWKFactory::createFromKeyFile("./rsa-private.key");

/**
 * Setup the algoritm used to sign the token.
 * @var \Jose\Component\Core\AlgorithmManager $algorithm
 */
$algorithm = new AlgorithmManager([
    new RS256()
]);

/**
 * The builder will create and sign the token.
 * @var \Jose\Component\Signature\JWSBuilder $jwsBuilder
 */
$jwsBuilder = new JWSBuilder($algorithm);

/**
 * Must setup JaaS payload!
 * Change the claims below or using the variables from above!
 */
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

/**
 * Create a JSON Web Signature (https://tools.ietf.org/html/rfc7515)
 * using the payload created above and the api key specified for the kid claim.
 * 'alg' (RS256) and 'typ' claims are also needed.
 */
$jws = $jwsBuilder
        ->create()
        ->withPayload($payload)
        ->addSignature($jwk, [
            'alg' => 'RS256',
            'kid' => $API_KEY,
            'typ' => 'JWT'
        ])
        ->build();

/**
 * We use the serializer to base64 encode into the final token.
 * @var \Jose\Component\Signature\Serializer\CompactSerializer $serializer
 */
$serializer = new CompactSerializer();
$token = $serializer->serialize($jws, 0);

/**
 * Write the token to standard output.
 */
echo $token;
