package server

import (
	"net/http"

	"github.com/dmarts05/speedshield/pkg/controllers"
	"github.com/dmarts05/speedshield/pkg/middlewares"
	"github.com/dmarts05/speedshield/pkg/services"
	"github.com/labstack/echo/v4"
	echoSwagger "github.com/swaggo/echo-swagger"
)

// Loads all server routes.
func (s *Server) loadRoutes() {
	s.router.GET("/swagger/*", echoSwagger.WrapHandler)

	g := s.router.Group("/api/v1")

	g.GET("/health", func(c echo.Context) error {
		return c.JSON(http.StatusOK, map[string]string{"message": "OK"})
	})

	// Auth
	authService := services.NewAuthService(s.pool)
	authController := controllers.NewAuthController(authService)
	authGroup := g.Group("/auth")
	authGroup.POST("/register", authController.Register)
	authGroup.POST("/login", authController.Login)
	authGroup.POST("/refresh", authController.Refresh)

	// -- Token Required ---
	authGroup.Use(middlewares.JwtMiddleware)
	authGroup.POST("/logout", authController.Logout)
}
