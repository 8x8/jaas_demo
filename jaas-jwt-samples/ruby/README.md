# JaaS JWT

This following demonstrates how to generate a JaaS JWT using Ruby.

### Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)

[JaaS JWT Document](https://developer.8x8.com/jaas/docs/api-keys-jwt)

## Dependencies

This code uses [openssl](https://github.com/ruby/openssl)

```
gem install openssl
```

And [ruby-jwt](https://github.com/jwt/ruby-jwt)

```
gem install jwt
```

## Generate JWT

In order to generate a custom JWT you must modify the variables from below in jaas-jwt.rb. The variables will modify the values for the claims.

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
TIME_NOW=Time.now.to_i # Sets to current local time
EXP_TIME_DELAY=7200 # No real need to modify this
NBF_TIME_DELAY=10 # No real need to modify this
##############################
```
