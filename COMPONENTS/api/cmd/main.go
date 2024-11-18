package main

import (
	"context"
	"os"
	"os/signal"

	_ "github.com/dmarts05/speedshield/docs"
	"github.com/dmarts05/speedshield/pkg/server"
	log "github.com/sirupsen/logrus"
)

// Swagger
//
//	@title						Speedshield API
//	@version					1.0
//	@description				An API for managing Speedshield services: users, speedcameras, alerts...
//	@BasePath					/api/v1
//	@host						localhost:8080
//	@contact.name				Daniel Martínez Sánchez
//	@contact.url				https://github.com/dmarts05
//	@contact.email				dmarts05@estudiantes.unileon.es
//	@schemes					http https
//	@securityDefinitions.apikey	JWT
//	@in							header
//	@name						Authorization
//	@description				JWT token for authentication. Please add it in the format "Bearer {AccessToken}" to authorize your requests.
func main() {
	// Setup loggger
	log.SetFormatter(&log.JSONFormatter{})
	log.SetOutput(os.Stdout)

	// Setup server
	s, err := server.New()
	if err != nil {
		log.Fatal(err)
	}

	// Interrupt signal to gracefully shutdown the server
	ctx, cancel := signal.NotifyContext(context.Background(), os.Interrupt)
	defer cancel()

	// Start server
	err = s.Start(ctx)
	if err != nil {
		log.Fatal(err)
	}
}
