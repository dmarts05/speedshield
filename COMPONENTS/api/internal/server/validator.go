package server

import (
	"net/http"

	"github.com/go-playground/validator/v10"
	"github.com/labstack/echo/v4"
)

// Custom validator for echo.
type customValidator struct {
	validator *validator.Validate
}

// Validates input using go-playground/validator.
// https://echo.labstack.com/docs/request#validate-data.
func (cv *customValidator) Validate(i interface{}) error {
	if err := cv.validator.Struct(i); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}
	return nil
}
