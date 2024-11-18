package services

import (
	"context"
	"time"

	"github.com/cdsacademy/cdsgarage/speedshield/internal/db"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/dtos"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/exc"
	"github.com/cdsacademy/cdsgarage/speedshield/internal/utils"
	"github.com/google/uuid"
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
	tokenResponse, httpErr := s.generateTokens(ctx, qtx, int(user.ID))
	if httpErr != nil {
		return dtos.TokenResponseDto{}, httpErr
	}

	// Commit transaction
	err = tx.Commit(ctx)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}

	return tokenResponse, nil
}

func (s *AuthService) Login(loginRequest dtos.LoginRequestDto) (dtos.TokenResponseDto, *echo.HTTPError) {
	// Create context with timeout
	ctx, cancel := utils.TimeoutContext()
	defer cancel()

	// Check if user exists
	exists, err := s.queries.DoesUserAlreadyExist(
		ctx,
		db.DoesUserAlreadyExistParams{
			Username: "",
			Email:    loginRequest.Email,
		},
	)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}
	if !exists {
		return dtos.TokenResponseDto{}, exc.UserNotFoundError()
	}

	// Get user by email
	user, err := s.queries.FindUserByEmail(ctx, loginRequest.Email)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}

	// Check password
	if !utils.CheckPassword(user.PasswordHash, loginRequest.Password) {
		return dtos.TokenResponseDto{}, exc.InvalidCredentialsError()
	}

	// Generate token and refresh token
	return s.generateTokens(ctx, s.queries, int(user.ID))
}

// Handles token refresh.
func (s *AuthService) Refresh(refreshTokenRequest dtos.RefreshRequestDto) (dtos.TokenResponseDto, *echo.HTTPError) {
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

	// Check if token is valid. It is important to acknowledge that the token is valid even if the expiration date is in the past
	userId, err := utils.VerifyTokenWithoutClaimValidation(refreshTokenRequest.Token)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.InvalidCredentialsError()
	}

	// Check if refresh token is valid
	refreshTokenUUID, err := uuid.Parse(refreshTokenRequest.RefreshToken)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.InvalidCredentialsError()
	}
	exists, err := qtx.DoesRefreshTokenExist(ctx, refreshTokenUUID)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}
	if !exists {
		return dtos.TokenResponseDto{}, exc.InvalidCredentialsError()
	}

	// Generate token and refresh token
	tokenResponse, httpErr := s.generateTokens(ctx, qtx, userId)
	if httpErr != nil {
		return dtos.TokenResponseDto{}, httpErr
	}

	// Remove old refresh token
	err = qtx.DeleteRefreshTokenById(ctx, refreshTokenUUID)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}

	// Commit transaction
	err = tx.Commit(ctx)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}

	return tokenResponse, nil
}

// Helper to create tokens and insert refresh token, supporting transactions.
func (s *AuthService) generateTokens(ctx context.Context, q *db.Queries, userID int) (dtos.TokenResponseDto, *echo.HTTPError) {
	// Generate token
	token, err := utils.GenerateToken(userID)
	if err != nil {
		return dtos.TokenResponseDto{}, exc.GenericError("Error generating token")
	}

	// Insert refresh token
	refreshToken, err := q.InsertRefreshToken(ctx, db.InsertRefreshTokenParams{
		UserID:     int32(userID),
		ExpiryDate: time.Now().Add(utils.RefreshTokenExpirationTime),
	})
	if err != nil {
		return dtos.TokenResponseDto{}, exc.DbGenericError()
	}

	// Return tokens
	return dtos.TokenResponseDto{
		Token:        token,
		RefreshToken: refreshToken.ID.String(),
	}, nil
}
