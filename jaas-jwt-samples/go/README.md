# JaaS JWT

This following demonstrates how to generate a JaaS JWT using Go.

### Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)

## Dependencies

To install dependencies run the following command in current directory.

```
go get ./
```

Or add the following to your go.mod file

```
require (
	github.com/google/uuid v1.1.4
	github.com/lestrrat-go/jwx v1.0.7
)
```

## Generate JWT

Using JaaSJwtBuilder you can generate a new JWT like in the following example :

```go
// Create a new JaaS JWT. Set the desired values below.
builder, err := NewJaaSJwtBuilder(
	WithDefaults(), // Set default values
	WithAPIKey("my api key"), // Set the api key, see https://jaas.8x8.vc/#/apikeys for more info.
	WithUserName("my user name"), // Set the user name
	WithUserEmail("my email address"), // Set the user email
	WithModerator(true), // Set the moderator
	WithAppID("my app id"), // Set the AppID
	WithUserAvatar("https://exampleurl.com/avatar"), // Set the avatar url
)

// Signs the JWT.
signedToken, _ := builder.SignWith(key)
```
