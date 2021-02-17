var jsonwebtoken = require('jsonwebtoken');
var uuid = require('uuid-random');

/**
 * Function generates a JaaS JWT.
 */
const generate = (privateKey, { id, name, email, avatar, appId, kid }) => {
  const now = new Date()
  const jwt = jsonwebtoken.sign({
    aud: 'jitsi',
    context: {
      user: {
        id,
        name,
        avatar,
        email: email,
        moderator: 'true'
      },
      features: {
        livestreaming: 'true',
        recording: 'true',
        transcription: 'true',
        "outbound-call": 'true'
      }
    },
    iss: 'chat',
    room: '*',
    sub: appId,
    exp: Math.round(now.setHours(now.getHours() + 3) / 1000),
    nbf: (Math.round((new Date).getTime() / 1000) - 10)
  }, privateKey, { algorithm: 'RS256', header: { kid } })
  return jwt;
}

/**
 * Generate a new JWT.
 */
const token = generate('my private key', {
    id: uuid(),
    name: "my user name",
    email: "my user email",
    avatar: "my avatar url",
    appId: "my AppID", // Your AppID ( previously tenant )
    kid: "my api key"
});

console.log(token);