package controllers

import (
	"net/http"

	"github.com/cdsacademy/cdsgarage/speedshield/internal/dtos"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/exc"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/services"
	"github.com/labstack/echo/v4"
)

// Controller for authentication.
type AuthController struct {
	authService *services.AuthService
}

// Creates a new instance of AuthController.
func NewAuthController(authService *services.AuthService) *AuthController {
	return &AuthController{authService: authService}
}

// Handles user registration.
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

	return c.JSON(http.StatusCreated, tokenResponse)
}

// Handles user login.
func (h *AuthController) Login(c echo.Context) error {
	loginRequest := &dtos.LoginRequestDto{}
	if err := c.Bind(&loginRequest); err != nil {
		return err
	}
	if err := c.Validate(loginRequest); err != nil {
		return err
	}

	tokenResponse, err := h.authService.Login(*loginRequest)
	if err != nil {
		return err
	}

	return c.JSON(http.StatusOK, tokenResponse)
}

// Handles token refresh.
func (h *AuthController) Refresh(c echo.Context) error {
	refreshRequest := &dtos.RefreshRequestDto{}
	if err := c.Bind(&refreshRequest); err != nil {
		return err
	}
	if err := c.Validate(refreshRequest); err != nil {
		return err
	}

	tokenResponse, err := h.authService.Refresh(*refreshRequest)
	if err != nil {
		return err
	}

	return c.JSON(http.StatusOK, tokenResponse)
}

// Handles user logout.
func (h *AuthController) Logout(c echo.Context) error {
	userId, ok := c.Get("userId").(int)
	if !ok {
		return exc.InvalidCredentialsError()
	}

	logoutRequest := &dtos.LogoutRequestDto{}
	if err := c.Bind(logoutRequest); err != nil {
		return err
	}
	if err := c.Validate(logoutRequest); err != nil {
		return err
	}

	err := h.authService.Logout(int(userId), *logoutRequest)
	if err != nil {
		return err
	}

	return c.NoContent(http.StatusOK)
}
