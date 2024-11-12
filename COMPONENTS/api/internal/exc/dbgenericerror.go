package exc

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

func DbGenericError() *echo.HTTPError {
	return echo.NewHTTPError(http.StatusInternalServerError, "Database error")
}
