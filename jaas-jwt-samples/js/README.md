# JaaS JWT

This code sample demonstrates how to generate a JaaS JWT using Javascript.

### Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)

## Dependencies

To install dependencies run the following in your project directory.

This code uses [node-jsonwebtoken](https://github.com/auth0/node-jsonwebtoken)

```
npm install jsonwebtoken
```

And [uuid-random](https://github.com/jchook/uuid-random)

```
npm install uuid-random
```

## Generate JWT

Using the generate function you can generate and sign a JWT to use with JaaS.

```
const token = generate('my private key', { // Pass your generated private key
    id: uuid(), // You can generate your own id and replace uuid()
    name: "my user name", // Set the user name
    email: "my user email", // Set the user email
    avatar: "my avatar url", // Set the user avatar
    appId: "my app id", // Your AppID
    kid: "my api key" // Set the api key, see https://jaas.8x8.vc/#/apikeys for more info.
});

console.log(token); // Write JWT to console.
```