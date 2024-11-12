package controllers

import (
	"github.com/cdsacademy/cdsgarage/speedshield/internal/dtos"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/services"
	"github.com/labstack/echo/v4"
)

type AuthController struct {
	authService *services.AuthService
}

func NewAuthController(authService *services.AuthService) *AuthController {
	return &AuthController{authService: authService}
}

func (h *AuthController) Register(c echo.Context) error {
	registerRequest := &dtos.RegisterRequestDto{}
	if err := c.Bind(&registerRequest); err != nil {
		return err
	}
	if err := c.Validate(registerRequest); err != nil {
		return err
	}

	tokenResponse, err := h.authService.Register(*registerRequest)
	if err != nil {
		return err
	}

	return c.JSON(201, tokenResponse)
}

func (h *AuthController) Login(c echo.Context) error {
	return nil
}

func (h *AuthController) Refresh(c echo.Context) error {
	return nil
}

func (h *AuthController) Logout(c echo.Context) error {
	return nil
}
