package server

import (
	"net/http"

	"github.com/cdsacademy/cdsgarage/speedshield/internal/controllers"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/services"
	"github.com/labstack/echo/v4"
)

// Loads all server routes.
func (s *Server) loadRoutes() {
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
}
