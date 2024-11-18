package services

import (
	"time"

	"github.com/cdsacademy/cdsgarage/speedshield/internal/db"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/dtos"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/exc"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/utils"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgtype"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/labstack/echo/v4"
)

// Service for authentication.
type AuthService struct {
	pool    *pgxpool.Pool
	queries *db.Queries
}

// Creates a new instance of AuthService.
func NewAuthService(pool *pgxpool.Pool) *AuthService {
	return &AuthService{
		pool:    pool,
		queries: db.New(pool),
	}
}

// Handles the registration of a new user.
func (s *AuthService) Register(registerRequest dtos.RegisterRequestDto) (dtos.TokenResponseDto, *echo.HTTPError) {
	// Create context with timeout
	ctx, cancel := utils.TimeoutContext()
	defer cancel()

	// Start transaction
	tx, err := s.pool.Begin(ctx)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}
	defer tx.Rollback(ctx) // nolint: errcheck

	qtx := s.queries.WithTx(tx)

	// Check if user already exists
	exists, err := qtx.DoesUserAlreadyExist(ctx, db.DoesUserAlreadyExistParams{
		Username: registerRequest.Username,
		Email:    registerRequest.Email,
	})
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	} else if exists {
		return dtos.TokenResponseDto{}, exc.UserAlreadyExistsError()
	}

	// Hash password
	hashedPassword, err := utils.HashPassword(registerRequest.Password)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.GenericError("Error hashing password")
	}

	// Insert newUser
	newUser := db.InsertUserParams{
		Username:     registerRequest.Username,
		Email:        registerRequest.Email,
		PasswordHash: hashedPassword,
	}
	user, err := qtx.InsertUser(ctx, newUser)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}

	// Generate token and refresh token
	token, err := utils.GenerateToken(int(user.ID))
	if err != nil {
		return dtos.TokenResponseDto{}, exc.GenericError("Error generating token")
	}
	refreshToken, err := qtx.InsertRefreshToken(ctx, db.InsertRefreshTokenParams{
		Token:  uuid.New().String(),
		UserID: user.ID,
		ExpiryDate: pgtype.Timestamp{
			Time:  time.Now().Add(utils.RefreshTokenExpirationTime),
			Valid: true,
		},
	})
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}

	// Commit transaction
	err = tx.Commit(ctx)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}

	// Return token
	return dtos.TokenResponseDto{
		Token:        token,
		RefreshToken: refreshToken.Token,
	}, nil
}
