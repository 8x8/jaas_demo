# JaaS JWT

This following demonstrates how to generate a JaaS JWT using bash.

### Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)

## Dependencies

[OpenSSL](https://www.openssl.org/)

Before you can generate the JWT, you must copy-paste the PEM private key into a file in the current directory.
The file should be named _rsa-private.pem_

## Generate JWT

In order to generate a custom JWT you must modify the variables from below in jaas-jwt.sh.
The variables will modify the values for the claims.

```
##############################
# Change the variables bellow
API_KEY="my api key" # See https://jaas.8x8.vc/#/apikeys for more info.
APP_ID="my AppID" # Sets the AppID / tenant
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
```
