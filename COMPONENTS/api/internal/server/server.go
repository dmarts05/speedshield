package server

import (
	"context"
	"strconv"

	"github.com/go-playground/validator/v10"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	pgxUUID "github.com/vgarvardt/pgx-google-uuid/v5"
)

// Holds all the necessary components to run the server.
type Server struct {
	db     *pgxpool.Pool
	router *echo.Echo
	cfg    config
}

// Creates a new server, loading the configuration from the environment variables.
func New() (*Server, error) {
	cfg, err := loadConfig()
	if err != nil {
		return nil, err
	}

	return &Server{
		db:     nil,
		router: echo.New(),
		cfg:    cfg,
	}, nil
}

// Starts the server, setting up the database connection and the routes.
// Also provides graceful shutdown handling.
func (s *Server) Start(ctx context.Context) error {
	// Database
	pgxConfig, err := pgxpool.ParseConfig(s.cfg.dbURI)
	if err != nil {
		return err
	}
	pgxConfig.AfterConnect = func(ctx context.Context, conn *pgx.Conn) error {
		pgxUUID.Register(conn.TypeMap())
		return nil
	}

	db, err := pgxpool.NewWithConfig(ctx, pgxConfig)
	if err != nil {
		return err
	}
	defer db.Close()

	err = db.Ping(ctx)
	if err != nil {
		return err
	}

	s.db = db

	// Router
	s.router.HideBanner = true
	s.router.Validator = &customValidator{validator: validator.New()}
	s.router.Use(middleware.Logger())
	s.router.Use(middleware.Recover())
	s.loadRoutes()

	// Start server in a separate goroutine
	go func() {
		if err := s.router.Start(":" + strconv.Itoa(s.cfg.port)); err != nil {
			s.router.Logger.Info("shutting down the server")
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server with a timeout of 10 seconds.
	<-ctx.Done()
	ctx, cancel := context.WithTimeout(context.Background(), 10)
	defer cancel()
	if err := s.router.Shutdown(ctx); err != nil {
		return err
	}

	return nil
}
