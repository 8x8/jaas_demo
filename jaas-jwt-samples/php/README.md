# JaaS JWT

Here you can find examples on how to generate a JaaS JWT using `firebase/php-jwt` and `web-token/jwt-framework`. But any other JWT library should do as long as you set the claims included in the two examples and use the RS256 asymmetric encryption.

For more libraries, please check [https://jwt.io/libraries](https://jwt.io/libraries).

## Useful links

[JWT (JSON Web Token)](https://tools.ietf.org/html/rfc7519)

[JSON](https://tools.ietf.org/html/rfc7159)

[JWS (JSON Web Signature)](https://tools.ietf.org/html/rfc7515)


## Dependencies

> **System requirements:** **PHP** `^7.1 || ^8.0` for `jaas-jwt-firebase.php` and **PHP** `^8.1` with the `BCMATH` extension for `jaas-jwt.php`

To install dependencies, go into your project directory and install composer using the guide from [the composer website](https://getcomposer.org/download/).

After Composer is installed in your project directory, run the following command to download the libraries.

```bash
./composer.phar update
```

## Generate JWT

Both script samples are meant to be executed from the CLI, but you can easily adapt them to be served by a webserver.

Change the variables defined at the start of the scripts with your values, edit the path or the contents of the private key and run the code.

```bash
./jaas-jwt-firebase.php
./jaas-jwt.php
```
