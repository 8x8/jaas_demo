# JaaS JWT

This following demonstrates how to generate a JaaS JWT using .NET(C#).

### Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)

## Dependencies

Add [IdentityModel extensions for .Net](https://www.nuget.org/packages/System.IdentityModel.Tokens.Jwt/) to your project using the following command in your project directory using PowerShell:

```
dotnet add package System.IdentityModel.Tokens.Jwt --version 6.8.0
```

## Generate JWT

    Using the JaaSJwtBuilder class you can generate a new JWT.

    /** Create new JaaSJwtBuilder and setup the claims. */
    var token = JaaSJwtBuilder.Builder()
                    .WithDefaults()
                    .WithApiKey("my api key")
                    .WithUserName("my user name")
                    .WithUserEmail("my user email")
                    .WithUserAvatar("https://avatarurl.com/avatar/url")
                    .WithTenantName("my tenant name")
                    .SignWith(rsaPrivateKey);

    /** Write JaaS JWT to standard output. */
    Console.Write(token);