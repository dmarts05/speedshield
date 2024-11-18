package middlewares

import (
	"strings"

	"github.com/cdsacademy/cdsgarage/speedshield/internal/exc"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/utils"
	"github.com/labstack/echo/v4"
)

// Middleware for JWT token verification.
// Extracts the token from the "Authorization" header and verifies it.
// If the token is valid, it sets the user id in the context.
func JwtMiddleware(next echo.HandlerFunc) echo.HandlerFunc {
	return func(c echo.Context) error {
		// Get tokenString string from "Bearer" field
		authorizationHeader := c.Request().Header.Get("Authorization")
		tokenString := strings.Split(authorizationHeader, "Bearer ")[1]
		if tokenString == "" {
			return exc.InvalidCredentialsError()
		}

		// Check token
		userId, err := utils.VerifyToken(tokenString)
		if err != nil {
			return exc.InvalidCredentialsError()
		}

		// Set user id in the context
		c.Set("userId", userId)
		return next(c)
	}
}
