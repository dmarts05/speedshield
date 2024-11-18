package dtos

// Data transfer object for the logout request.
type LogoutRequestDto struct {
	RefreshToken string `json:"refresh_token" validate:"required"`
}
