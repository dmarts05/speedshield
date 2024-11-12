package utils

import (
	"context"
	"time"
)

// Returns a context with a timeout of 10 seconds
func TimeoutContext() (context.Context, context.CancelFunc) {
	return context.WithTimeout(context.Background(), 10*time.Second)
}
