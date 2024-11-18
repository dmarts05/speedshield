package utils

import (
	"errors"
	"os"
	"time"

	"github.com/golang-jwt/jwt"
)

var jwtSecretKey = []byte(os.Getenv("JWT_SECRET"))

// Creates a JWT for a user with the given user ID
func GenerateToken(sub int) (string, error) {
	// Create token claims with an expiration time
	claims := jwt.MapClaims{
		"sub": sub,
		"exp": time.Now().Add(JwtExpirationTime).Unix(),
	}

	// Create the token with claims and sign it
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	signedToken, err := token.SignedString(jwtSecretKey)
	if err != nil {
		return "", err
	}
	return signedToken, nil
}

// Verifies a JWT and extracts the user ID if valid
func VerifyToken(tokenString string) (int, error) {
	// Parse the token with claims
	token, err := jwt.Parse(tokenString, keyFunc)
	if err != nil {
		return 0, err
	}

	// Check the token claims
	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		sub, ok := claims["sub"].(float64) // Claims stores numbers as float64
		if !ok {
			return 0, errors.New("invalid sub in token")
		}
		return int(sub), nil
	}

	return 0, errors.New("invalid token")
}

func VerifyTokenWithoutClaimValidation(tokenString string) (int, error) {
	// Create custom parser that skips claims validation
	parser := &jwt.Parser{
		SkipClaimsValidation: true,
	}

	// Parse the token with custom parser
	token, err := parser.Parse(tokenString, keyFunc)
	if err != nil {
		return 0, err
	}

	// Check the token claims
	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		sub, ok := claims["sub"].(float64) // Claims stores numbers as float64
		if !ok {
			return 0, errors.New("invalid sub in token")
		}
		return int(sub), nil
	}

	return 0, errors.New("invalid token")
}

// Helper that validates the signing method of a token. It must be HMAC.
func keyFunc(token *jwt.Token) (interface{}, error) {
	// Validate the signing method
	if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
		return nil, errors.New("invalid signing method")
	}
	return jwtSecretKey, nil
}
