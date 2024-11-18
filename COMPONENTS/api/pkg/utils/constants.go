package utils

import "time"

var JwtExpirationTime = 15 * time.Minute             // 15 minutes
var RefreshTokenExpirationTime = 30 * 24 * time.Hour // 30 days
