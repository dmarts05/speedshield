package server

import (
	"net/http"

	"github.com/labstack/echo/v4"
)

// Loads all server routes.
func (s *Server) loadRoutes() {
	g := s.router.Group("/api/v1")

	g.GET("/health", func(c echo.Context) error {
		return c.JSON(http.StatusOK, map[string]string{"message": "OK"})
	})
}
