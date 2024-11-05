package main

import (
	"context"
	"os"
	"os/signal"

	"github.com/cdsacademy/cdsgarage/speedshield/internal/server"
	log "github.com/sirupsen/logrus"
)

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
