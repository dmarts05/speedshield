package dtos

// Data transfer object for the refresh request.
type RefreshRequestDto struct {
	Token        string `json:"token" validate:"required"`
	RefreshToken string `json:"refresh_token" validate:"required"`
}
