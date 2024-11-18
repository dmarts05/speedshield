package server

import (
	"errors"
	"fmt"
	"os"
	"strconv"
)

// Includes the necessary configuration to run the server.
type config struct {
	dbURI     string
	port      int
	jwtSecret []byte
}

// Loads the configuration from the environment variables.
// Returns the loaded configuration or an error if any of the required environment variables is missing.
func loadConfig() (config, error) {
	dbHost, ok := os.LookupEnv("DB_HOST")
	if !ok {
		return config{}, errors.New("DB_HOST environment variable is not set")
	}
	dbPort, ok := os.LookupEnv("DB_PORT")
	if !ok {
		return config{}, errors.New("DB_PORT environment variable is not set")
	}
	dbUser, ok := os.LookupEnv("DB_USER")
	if !ok {
		return config{}, errors.New("DB_USER environment variable is not set")
	}
	dbPassword, ok := os.LookupEnv("DB_PASSWORD")
	if !ok {
		return config{}, errors.New("DB_PASSWORD environment variable is not set")
	}
	dbURI := fmt.Sprintf("postgres://%s:%s@%s:%s", dbUser, dbPassword, dbHost, dbPort)

	portStr, ok := os.LookupEnv("API_PORT")
	if !ok {
		return config{}, errors.New("API_PORT environment variable is not set")
	}
	port, err := strconv.Atoi(portStr)
	if err != nil {
		return config{}, err
	}

	jwtSecret, ok := os.LookupEnv("JWT_SECRET")
	if !ok {
		return config{}, errors.New("JWT_SECRET environment variable is not set")
	}

	return config{
		dbURI:     dbURI,
		port:      port,
		jwtSecret: []byte(jwtSecret),
	}, nil
}
