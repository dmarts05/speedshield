package exc

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

// HTTP error for when a user is not found.
func UserNotFoundError() *echo.HTTPError {
	return echo.NewHTTPError(http.StatusNotFound, "User not found")
}
