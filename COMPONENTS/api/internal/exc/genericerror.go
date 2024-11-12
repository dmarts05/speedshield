package exc

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

func GenericError(message string) *echo.HTTPError {
	return echo.NewHTTPError(
		http.StatusInternalServerError,
		message,
	)
}
