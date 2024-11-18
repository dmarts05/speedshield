package exc

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

// HTTP error for invalid credentials.
func InvalidCredentialsError() *echo.HTTPError {
	return echo.NewHTTPError(http.StatusUnauthorized, "Invalid credentials")
}
