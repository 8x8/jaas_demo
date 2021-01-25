#!/usr/bin/env bash

#
# The following code generated a JaaS JWT.
#

##############################
# Change the variables bellow
API_KEY="my api key" # See https://jaas.8x8.vc/#/apikeys for more info.
TENANT_NAME="my tenant name" # Sets the tenant name / tenant unique identifier
USER_NAME="my user name" # Sets the user name
USER_EMAIL="my user email" # Sets the user email
USER_AVATAR_URL="my avatar url" # Sets the users avatar url
USER_ID="my user id" # Set a unique id for the user, corresponding to your backend
USER_IS_MODERATOR=false # Sets the user as moderator
RECORDING_ENABLED=false # Sets recording feature enabled
LIVESTREAMING_ENABLED=false # Sets live steraming feature enabled
OUTBOUND_CALL_ENABLED=false # Sets outbound call feature enabled
TRANSCRIPTION_ENABLED=false # Sets transcription feature enabled
ROOM_NAME="my room name or *" # Sets the room name
timeNow=`date +%s` # Sets to current local time
expTimeDelay=7200 # No real need to modify this
nbfTimeDelay=10 # No real need to modify this
##############################

header='{
    "typ": "JWT",
    "alg": "RS256",
    "kid": "'$API_KEY'"
}'

userClaims='{
    "id": "'$USER_ID'",
    "name": "'$USER_NAME'",
    "avatar": "'$USER_AVATAR_URL'",
    "email": "'$USER_EMAIL'",
    "moderator": "'$USER_IS_MODERATOR'"
}'

featureClaims='{
    "livestreaming": "'$LIVESTREAMING_ENABLED'",
    "recording": "'$RECORDING_ENABLED'",
    "transcription": "'$TRANSCRIPTION_ENABLED'",
    "outbound-call": "'$OUTBOUND_CALL_ENABLED'"
}'

payload='{
    "aud": "jitsi",
    "iss": "chat",
    "context": {
        "user": '$userClaims',
        "features": '$featureClaims'
    },
    "room": "'$ROOM_NAME'",
    "sub": "'$TENANT_NAME'",
    "exp": '$(($timeNow+$expTimeDelay))',
    "nbf": '$(($timeNow-$nbfTimeDelay))'
}'

#################
# $1 String to base64 encode
#################
encodeBase64() {
    echo -n $1 | base64 | sed s/\+/-/ | sed -E s/=+$//
}

#################
# $1 path of the rsa private key in pem format
# $2 jwt payload including header
#################
signWithKey() {
    echo -n "$2" | openssl dgst -sha256 -binary -sign "$1"  | openssl enc -base64 | tr -d '\n=' | tr -- '+/' '-_'
}

################
generateJaaSJwt() {
    encodedHeader=$(echo `encodeBase64 "$header"`)
    encodedPayload=$(echo `encodeBase64 "$payload"`)
    encodedData=$encodedHeader"."$encodedPayload
    signature=$(echo `signWithKey "rsa-private.pem" $encodedData`)
    echo $encodedData"."$signature
}

generateJaaSJwt