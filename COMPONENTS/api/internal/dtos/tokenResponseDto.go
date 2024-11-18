package dtos

// Data transfer object for a token response.
type TokenResponseDto struct {
	Token        string `json:"token"`
	RefreshToken string `json:"refresh_token"`
}
