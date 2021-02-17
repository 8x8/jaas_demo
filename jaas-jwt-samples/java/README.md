# JaaS JWT

This following demonstrates how to generate a JaaS JWT using JAVA.

### Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)

## Prerequisite

Before you can generate a JWT using this JAVA code you must convert your existing private key to [PKCS#8](https://tools.ietf.org/html/rfc5208) format.
Run the following command in Terminal :

```
    openssl pkcs8 -topk8 -inform PEM -in <input key path> -outform pem -nocrypt -out <output key path>
```

Replace <input key path> with the file path of the private key to convert and <output key path> the output path of the converted key respectively.

Example :

```
    openssl pkcs8 -topk8 -inform PEM -in ./my_jaas_privatekey.key -outform pem -nocrypt -out ./my_jaas_privatekey.pem
```

## Dependencies

Uses [JAVA Auth0 JWT](https://github.com/auth0/java-jwt)

### Maven

Add the following to your pom.xml file.

```
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.12.0</version>
</dependency>
```

**Note : You probably can import the existing Maven project into your IDE.**

### Gradle

Add the following to your build.gradle file.

```
dependencies {
    // ...
    implementation 'com.auth0:java-jwt:3.12.0'
    // ...
```

## Generate JWT

Using the JaaSJwtBuilder class you can generate a new JWT.

```
    /** Create new JaaSJwtBuilder and setup the claims. */
    /** Using method chaining we create a new JaaSJwtBuilder that will setup all the claims.
    Before signing the JWT is created with all the specified claims. */
    String token = JaaSJwtBuilder.builder() // Creates a new instance of JaaSJwtBuilder
            .withDefaults() // This sets default/most common values
            .withApiKey("My api key here") // Set the api key, see https://jaas.8x8.vc/#/apikeys for more info.
            .withUserName("My name here") // Set the user name
            .withUserEmail("My email here") // Set the user email
            .withModerator(true) // Enable user as moderator
            .withOutboundEnabled(true) // Enable outbound calls
            .withTranscriptionEnabled(true) // Enable transcription
            .withAppID("My AppID here") // Set the AppID
            .withUserAvatar("https://avatarurl.com/avatar/url") // Set the user avatar
            .signWith(rsaPrivateKey); /** Finally the JWT is signed with the private key */

    System.out.println(token); // Write to standard output the signed token.
```