package exc

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

// HTTP error for an undefined database error.
func DbGenericError() *echo.HTTPError {
	return echo.NewHTTPError(http.StatusInternalServerError, "Database error")
}
