package dtos

// Data transfer object for a register request.
type RegisterRequestDto struct {
	Email    string `json:"email" validate:"required,email" example:"johndoe@example.com"`
	Username string `json:"username" validate:"required,min=3,max=32" example:"johndoe" minLength:"3" maxLength:"32"`
	Password string `json:"password" validate:"required,min=8,max=32" example:"password123" minLength:"8" maxLength:"32"`
}
