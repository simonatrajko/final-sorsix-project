package com.sorsix.serviceconnector.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String
) {
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))
    private val accessTokenValidityMs = 15L * 60 * 1000 // 15 minutes
    private val refreshTokenValidityMs = 30L * 24 * 60 * 60 * 1000 // 30 days

    private fun generateToken(
        username: String,
        role: String,
        type: String,
        expiry: Long
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)

        return Jwts.builder()
            .setSubject(username)               // ✅ now username, not ID
            .claim("role", role)
            .claim("type", type)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateAccessToken(username: String, role: String): String =
        generateToken(username, role, "access", accessTokenValidityMs)

    fun generateRefreshToken(username: String, role: String): String =
        generateToken(username, role, "refresh", refreshTokenValidityMs)

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        return claims["type"] == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        return claims["type"] == "refresh"
    }

    fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token)
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid token.")
        return claims.subject // ✅ username
    }

    fun getRoleFromToken(token: String): String? =
        parseAllClaims(token)?.get("role", String::class.java)

    private fun parseAllClaims(token: String): Claims? {
        val raw = token.removePrefix("Bearer ").trim()
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(raw)
                .body
        } catch (_: Exception) {
            null
        }
    }
}
