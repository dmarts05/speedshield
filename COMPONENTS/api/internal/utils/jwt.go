package utils

import (
	"errors"
	"os"
	"time"

	"github.com/golang-jwt/jwt"
)

var jwtSecretKey = []byte(os.Getenv("JWT_SECRET_KEY"))

// Creates a JWT for a user with the given user ID
func GenerateToken(id int) (string, error) {
	// Create token claims with an expiration time
	claims := jwt.MapClaims{
		"id":  id,
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
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		// Validate the signing method
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, errors.New("invalid signing method")
		}
		return jwtSecretKey, nil
	})
	if err != nil {
		return 0, err
	}

	// Check the token claims
	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		id, ok := claims["id"].(float64) // Claims stores numbers as float64
		if !ok {
			return 0, errors.New("invalid id in token")
		}
		return int(id), nil
	}
	return 0, errors.New("invalid token")
}
