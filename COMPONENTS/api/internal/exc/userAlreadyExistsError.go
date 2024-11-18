package exc

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

// HTTP error for a user already exists.
func UserAlreadyExistsError() *echo.HTTPError {
	return echo.NewHTTPError(http.StatusConflict, "User already exists")
}
