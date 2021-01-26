# JaaS JWT

This following demonstrates how to generate a JaaS JWT using Python.

### Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)

## Dependencies

**System requirements : Python 3.x and above**

To install dependencies run the following command in current directory.

```
pip install -r requirements.txt
```

## Generate JWT

Using JaaSJwtBuilder you can generate a new JWT like in the following example :

```
    jaasJwt = JaaSJwtBuilder() # Create new JaaSJwtBuilder and setup the claims.

    token = jaasJwt.withDefaults() \
        .withApiKey("my api key") \
            .withUserName("my user name") \
                .withUserEmail("my email address") \
                    .withModerator(True) \
                        .withTenantName("my tenant name") \
                            .withUserAvatar("https://asda.com/avatar") \
                                .signWith("my private key") # Pass the private here

    print(token) // Writes the signed JaaS JWT to standard output
```
