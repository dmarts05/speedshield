package dtos

// Data transfer object for a token response.
type TokenResponseDto struct {
	Token        string `json:"token" example:"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MzE5NzA2MDQsInN1YiI6MX0.BUnMhHws6UdDCio_nyuUYQYkqWy_NwEmt6BZbA3ODk4"`
	RefreshToken string `json:"refresh_token" example:"00000000-0000-0000-0000-000000000000"`
}
