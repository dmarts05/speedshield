# Base stage: installs dependencies and copies the code
FROM golang:1.23-alpine AS base

# Set the working directory
WORKDIR /app

# Download dependencies
COPY go.mod go.sum ./
RUN go mod download

# Copy the source code
COPY . .

# Development stage: adds air for live-reloading and delve for debugging
FROM base AS development

# Install air for live-reloading
RUN go install github.com/air-verse/air@latest

# Install delve for debugging
RUN go install github.com/go-delve/delve/cmd/dlv@latest

# Command to run air in development mode
CMD ["air"]

# Build stage: compiles the application
FROM base AS build

# Build the Go application
RUN go build -ldflags="-s -w" -o=speedshield-api ./cmd

# Production stage: runs the compiled application
FROM golang:1.23-alpine AS production

WORKDIR /app

# Copy the compiled binary from the build stage
COPY --from=build /app/speedshield-api .

# Command to run the binary
CMD ["/app/speedshield-api"]
