package dtos

// Data transfer object for a login request.
type LoginRequestDto struct {
	Email    string `json:"email" validate:"required,email" example:"johndoe@example.com"`
	Password string `json:"password" validate:"required" example:"password123"`
}
