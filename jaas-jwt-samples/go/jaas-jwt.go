package main

import (
	"crypto/rand"
	"crypto/rsa"
	"crypto/x509"
	"encoding/json"
	"encoding/pem"
	"io/ioutil"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/pascaldekloe/jwt"
)

// JWT helper constants
const (
	LiveStreamingKey = "livestreaming"
	RecordingKey     = "recording"
	UserIDKey        = "id"
	IsModeratorKey   = "moderator"
	RoomKey          = "room"
	ExpTimeDelaySec  = 7200
	NbfTimeDelaySec  = 10
)

// JaaSJwtBuilder helps with the generation of the JWT token for JaaS.
// Below you will find an example how to use it.
type JaaSJwtBuilder struct {
	userClaims    map[string]interface{}
	featureClaims map[string]interface{}
	payload       map[string]interface{}
	headers       map[string]interface{}
}

// NewJaaSJwtBuilder creates a new builder that can be used to generate a JWT.
// This will help generate a JaaS JWT using the following functions.
func NewJaaSJwtBuilder(params ...func(*JaaSJwtBuilder) error) (*JaaSJwtBuilder, error) {

	jaaSJwtBuilder := JaaSJwtBuilder{
		featureClaims: map[string]interface{}{},
		userClaims:    map[string]interface{}{},
		payload: map[string]interface{}{
			"aud": "jitsi",
			"iss": "chat",
		},
		headers: map[string]interface{}{
			"typ": "JWT",
		},
	}

	for _, param := range params {
		err := param(&jaaSJwtBuilder)
		if err != nil {
			return nil, err
		}
	}

	return &jaaSJwtBuilder, nil
}

// SignWith should be used after all the needed claims have been set.
// Returns a signed token string or "" on error.
func (jjb *JaaSJwtBuilder) SignWith(privateKey *rsa.PrivateKey) (string, error) {

	jjb.payload["context"] = map[string]interface{}{
		"user":     jjb.userClaims,
		"features": jjb.featureClaims,
	}

	payloadClaims := jwt.Claims{
		Set: jjb.payload,
	}

	extraHeaders, err := json.Marshal(jjb.headers)

	if err != nil {
		return "", err
	}

	signedToken, err := payloadClaims.RSASign(jwt.RS256, privateKey, extraHeaders)

	if err != nil {
		return "", err
	}

	return string(signedToken), err
}

// WithDefaults sets the default valued claims for a JaaSJwtBuilder.
func WithDefaults() func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {

		rid := uuid.New().String()
		applyParams := func(builder *JaaSJwtBuilder, params ...func(*JaaSJwtBuilder) error) error {
			for _, param := range params {
				err := param(builder)

				if err != nil {
					return err
				}
			}
			return nil
		}

		return applyParams(
			jjb,
			WithExpTime((time.Now().Unix() + ExpTimeDelaySec)),
			WithNbf(time.Now().Unix()-NbfTimeDelaySec),
			WithLiveStreamingEnabled(true),
			WithRecordingEnabled(true),
			WithOutboundEnabled(true),
			WithTranscriptionEnabled(true),
			WithModerator(true),
			WithRoomName("*"),
			WithUserID(rid),
		)
	}
}

// WithAPIKey sets the API Key for a JaaSJwtBuilder.
// You can get an API Key from https://jaas.8x8.vc/#/apikeys.
func WithAPIKey(apiKey string) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.headers["kid"] = apiKey
		return nil
	}
}

// WithUserAvatar sets the avatar url for a JaaSJwtBuilder.
func WithUserAvatar(avatarURL string) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.userClaims["avatar"] = avatarURL
		return nil
	}
}

// WithModerator sets the user as moderator if isModerator is true, false otherwise.
func WithModerator(isModerator bool) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.userClaims["moderator"] = strconv.FormatBool(isModerator)
		return nil
	}
}

// WithOutboundEnabled enables outbound call service if isEnabled is true, false otherwise.
func WithOutboundEnabled(isEnabled bool) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.featureClaims["outbound-call"] = strconv.FormatBool(isEnabled)
		return nil
	}
}

// WithTranscriptionEnabled enables transcription service if isEnabled is true, false otherwise.
func WithTranscriptionEnabled(isEnabled bool) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.featureClaims["transcription"] = strconv.FormatBool(isEnabled)
		return nil
	}
}

// WithUserName sets the value for the name claim, represents the user name.
func WithUserName(userName string) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.userClaims["name"] = userName
		return nil
	}
}

// WithUserEmail sets the value for the email claim, the email address of the user.
func WithUserEmail(userEmail string) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.userClaims["email"] = userEmail
		return nil
	}
}

// WithLiveStreamingEnabled enables livestreaming service if isEnabled is true, false otherwise.
func WithLiveStreamingEnabled(isEnabled bool) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.featureClaims["livestreaming"] = strconv.FormatBool(isEnabled)
		return nil
	}
}

// WithRecordingEnabled enables recording service if isEnabled is true, false otherwise.
func WithRecordingEnabled(isEnabled bool) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.featureClaims["recording"] = strconv.FormatBool(isEnabled)
		return nil
	}
}

// WithExpTime sets the value for the exp claim, represents the jwt expiration (unix) time.
// You shouldn't have to change this too much.
func WithExpTime(expTime int64) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.payload["exp"] = expTime
		return nil
	}
}

// WithNbf sets the value for the nbf claim, unix time. You shouldn't have to change this too much.
func WithNbf(nbf int64) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.payload["nbf"] = nbf
		return nil
	}
}

// WithRoomName sets the value for the room claim.
func WithRoomName(roomName string) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.payload["room"] = roomName
		return nil
	}
}

// WithTenantName sets the value for the sub claim, representing the unique tenant identifier.
func WithTenantName(tenantName string) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.payload["sub"] = tenantName
		return nil
	}
}

// WithUserID sets the value for the id claim, identifying the user.
func WithUserID(userID string) func(*JaaSJwtBuilder) error {
	return func(jjb *JaaSJwtBuilder) error {
		jjb.userClaims["id"] = userID
		return nil
	}
}

// Generates a new RSA 4096 bit key.
func generateRsaKey() (*rsa.PrivateKey, error) {
	return rsa.GenerateKey(rand.Reader, 4096)
}

// Reads a private key from a file.
func readRsaPrivateKey() (*rsa.PrivateKey, error) {
	priv, err := ioutil.ReadFile("./rsa-private.pem")

	if err != nil {
		return nil, nil
	}

	privPem, _ := pem.Decode(priv)
	var privPemBytes []byte
	privPemBytes = privPem.Bytes
	var parsedKey interface{}
	parsedKey, err = x509.ParsePKCS8PrivateKey(privPemBytes) // Check errors
	var privateKey *rsa.PrivateKey
	privateKey, _ = parsedKey.(*rsa.PrivateKey)
	return privateKey, nil
}

func main() {

	// Create a new JaaS JWT. Set the desired values below.
	builder, err := NewJaaSJwtBuilder(
		WithDefaults(),
		WithAPIKey("my api key"),
		WithUserName("someone"),
		WithUserEmail("myemail@email.com"),
		WithModerator(true),
		WithTenantName("my tenant name"),
		WithUserAvatar("https://asda.com/avatar"),
	)

	if err == nil {
		// You can read the generated private key from a file. See https://jaas.8x8.vc/#/apikeys.
		pk, _ := readRsaPrivateKey()

		// Signs the JWT.
		signedToken, _ := builder.SignWith(pk)

		// Writes the signed jwt to standard output.
		println(signedToken)
	} else {
		println(err)
	}
}
