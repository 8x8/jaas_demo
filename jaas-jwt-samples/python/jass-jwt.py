#!/usr/bin/python

import sys, os, time, uuid
from authlib.jose import jwt

class JaaSJwtBuilder:
    """
        The JaaSJwtBuilder class helps with the generation of the JaaS JWT.
    """

    EXP_TIME_DELAY_SEC = 7200
    # Used as a delay for the exp claim value.

    NBF_TIME_DELAY_SEC = 10
    # Used as a delay for the nbf claim value.

    def __init__(self) -> None:
        self.header = { 'alg' : 'RS256' }
        self.userClaims = {}
        self.featureClaims = {}
        self.payloadClaims = {}

    def withDefaults(self):
        """Returns the JaaSJwtBuilder with default valued claims."""
        return self.withExpTime(int(time.time() + JaaSJwtBuilder.EXP_TIME_DELAY_SEC)) \
            .withNbfTime(int(time.time() - JaaSJwtBuilder.NBF_TIME_DELAY_SEC)) \
                .withLiveStreamingEnabled(True) \
                    .withRecordingEnabled(True) \
                        .withModerator(True) \
                            .withRoomName('*') \
                                .withUserId(str(uuid.uuid4()))

    def withApiKey(self, apiKey):
        """
        Returns the JaaSJwtBuilder with the kid claim(apiKey) set.

        :param apiKey A string as the API Key https://jaas.8x8.vc/#/apikeys
        """
        self.header['kid'] = apiKey
        return self

    def withUserAvatar(self, avatarUrl):
        """
        Returns the JaaSJwtBuilder with the avatar claim set.

        :param avatarUrl A string representing the url to get the user avatar.
        """
        self.userClaims['avatar'] = avatarUrl
        return self

    def withModerator(self, isModerator):
        """
        Returns the JaaSJwtBuilder with the moderator claim set.

        :param isModerator A boolean if set to True, user is moderator and False otherwise.
        """
        self.userClaims['moderator'] = 'true' if isModerator == True else 'false'
        return self

    def withUserName(self, userName):
        """
        Returns the JaaSJwtBuilder with the name claim set.

        :param userName A string representing the user's name.
        """
        self.userClaims['name'] = userName
        return self

    def withUserEmail(self, userEmail):
        """
        Returns the JaaSJwtBuilder with the email claim set.

        :param userEmail A string representing the user's email address.
        """
        self.userClaims['email'] = userEmail
        return self

    def withLiveStreamingEnabled(self, isEnabled):
        """
        Returns the JaaSJwtBuilder with the livestreaming claim set.

        :param isEnabled A boolean if set to True, live streaming is enabled and False otherwise.
        """
        self.featureClaims['livestreaming'] = 'true' if isEnabled == True else 'false'
        return self

    def withRecordingEnabled(self, isEnabled):
        """
        Returns the JaaSJwtBuilder with the recording claim set.

        :param isEnabled A boolean if set to True, recording is enabled and False otherwise.
        """
        self.featureClaims['recording'] = 'true' if isEnabled == True else 'false'
        return self

    def withExpTime(self, expTime):
        """
        Returns the JaaSJwtBuilder with exp claim set. Use the defaults, you won't have to change this value too much.

        :param expTime Unix time in seconds since epochs plus a delay. Expiration time of the JWT.
        """
        self.payloadClaims['exp'] = expTime
        return self

    def withNbfTime(self, nbfTime):
        """
        Returns the JaaSJwtBuilder with nbf claim set. Use the defaults, you won't have to change this value too much.

        :param nbfTime Unix time in seconds since epochs.
        """
        self.payloadClaims['nbfTime'] = nbfTime
        return self

    def withRoomName(self, roomName):
        """
        Returns the JaaSJwtBuilder with room claim set.

        :param roomName A string representing the room to join.
        """
        self.payloadClaims['room'] = roomName
        return self

    def withTenantName(self, tenantName):
        """
        Returns the JaaSJwtBuilder with the tenant claim set.

        :param tenantName A string representing the unique tenant identifier.
        """
        self.payloadClaims['sub'] = tenantName
        return self

    def withUserId(self, userId):
        """
        Returns the JaaSJwtBuilder with the id claim set.

        :param A string representing the user, should be unique from your side.
        """
        self.payloadClaims['id'] = userId
        return self

    def signWith(self, key):
        """
        Returns a signed JWT.

        :param key A string representing the private key in PEM format.
        """
        context = { 'user': self.userClaims, 'features': self.featureClaims }
        self.payloadClaims['context'] = context
        self.payloadClaims['iss'] = 'chat'
        self.payloadClaims['aud'] = 'jitsi'
        return jwt.encode(self.header, self.payloadClaims, key)

def main(argv):
    try:
        scriptDir = os.path.dirname(__file__)
        fp = os.path.join(scriptDir, 'rsa-private.pem')

        with open(fp, 'r') as reader:

            jaasJwt = JaaSJwtBuilder()

            token = jaasJwt.withDefaults() \
                .withApiKey("vpaas-magic-cookie-931ab79243e94c7da66e4bded55c2fb6/8c003f") \
                    .withUserName("my user name") \
                        .withUserEmail("myemail@email.com") \
                            .withModerator(True) \
                                .withTenantName("vpaas-magic-cookie-931ab79243e94c7da66e4bded55c2fb6") \
                                    .withUserAvatar("https://asda.com/avatar") \
                                        .signWith(reader.read())

            print(token)

    except Exception as e:
        print(e)

if __name__ == "__main__":
    main(sys.argv[1:])