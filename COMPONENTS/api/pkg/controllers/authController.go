package controllers

import (
	"net/http"

	"github.com/dmarts05/speedshield/pkg/dtos"
	"github.com/dmarts05/speedshield/pkg/exc"
	"github.com/dmarts05/speedshield/pkg/services"
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
//
//	@Summary		Registers a new user.
//	@Description	Registers a new user given the required information and returns authentication tokens. Doesn't require authentication.
//	@Tags			auth
//	@Accept			json
//	@Produce		json
//	@Param			registerRequest	body		dtos.RegisterRequestDto	true	"Register request"
//	@Success		201				{object}	dtos.TokenResponseDto	"JWT token and refresh token"
//	@Failure		400				{object}	echo.HTTPError			"Invalid request"
//	@Failure		409				{object}	echo.HTTPError			"User already exists"
//	@Failure		500				{object}	echo.HTTPError			"Internal server error"
//	@Router			/auth/register [post]
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
//
//	@Summary		Logs in a user.
//	@Description	Logs in a user given the required information and returns authentication tokens. Doesn't require authentication.
//	@Tags			auth
//	@Accept			json
//	@Produce		json
//	@Param			loginRequest	body		dtos.LoginRequestDto	true	"Login request"
//	@Success		200				{object}	dtos.TokenResponseDto	"JWT token and refresh token"
//	@Failure		400				{object}	echo.HTTPError			"Invalid request"
//	@Failure		401				{object}	echo.HTTPError			"Invalid credentials"
//	@Failure		404				{object}	echo.HTTPError			"User not found"
//	@Failure		500				{object}	echo.HTTPError			"Internal server error"
//	@Router			/auth/login [post]
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
//
//	@Summary		Refreshes a user token.
//	@Description	Refreshes a user token given the required information and returns a new authentication token. Doesn't require authentication.
//	@Tags			auth
//	@Accept			json
//	@Produce		json
//	@Param			refreshRequest	body		dtos.RefreshRequestDto	true	"Refresh request"
//	@Success		200				{object}	dtos.TokenResponseDto	"JWT token
//	@Failure		400				{object}	echo.HTTPError			"Invalid request"
//	@Failure		401				{object}	echo.HTTPError			"Invalid credentials"
//	@Failure		500				{object}	echo.HTTPError			"Internal server error"
//	@Router			/auth/refresh [post]
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
//
//	@Summary		Logs out a user.
//	@Description	Logs out a user given the required information. Removing the refresh token from the database.
//	@Tags			auth
//	@Accept			json
//	@Produce		json
//	@Param			logoutRequest	body	dtos.LogoutRequestDto	true	"Logout request"
//	@Success		200				"OK"
//	@Failure		400				{object}	echo.HTTPError	"Invalid request"
//	@Failure		401				{object}	echo.HTTPError	"Invalid credentials"
//	@Failure		500				{object}	echo.HTTPError	"Internal server error"
//	@Router			/auth/logout [post]
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
