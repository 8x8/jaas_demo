require "openssl"
require "jwt"

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

PAYLOAD = {
  "aud": "jitsi",
  "exp": TIME_NOW + EXP_TIME_DELAY,
  "nbf": TIME_NOW - NBF_TIME_DELAY,
  "iss": "chat",
  "room": ROOM_NAME,
  "sub": APP_ID,

  "context": {
    "features": {
      "livestreaming": LIVESTREAMING_ENABLED,
      "outbound-call": OUTBOUND_CALL_ENABLED,
      "transcription": TRANSCRIPTION_ENABLED,
      "recording": RECORDING_ENABLED
    },
    "user": {
      "moderator": USER_IS_MODERATOR,
      "name": USER_NAME,
      "id": USER_ID,
      "avatar": USER_AVATAR_URL,
      "email": USER_EMAIL
    }
  }
}

private_key = OpenSSL::PKey::RSA.new File.read("/path/to/your_private_key.pk")
jwt = JWT.encode PAYLOAD, private_key, "RS256", {"typ": "JWT", "kid": API_KEY}
