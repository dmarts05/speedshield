package exc

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

// HTTP error for an undefined server error with a custom message.
func GenericError(message string) *echo.HTTPError {
	return echo.NewHTTPError(
		http.StatusInternalServerError,
		message,
	)
}
