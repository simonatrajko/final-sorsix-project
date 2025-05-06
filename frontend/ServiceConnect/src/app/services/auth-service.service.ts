import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthResponseDto } from '../models/auth-response-dto';
import { UserAuthDto } from '../models/user-auth-dto';
import { HttpHeaders } from '@angular/common/http';



@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<AuthResponseDto> {
    return this.http.post<AuthResponseDto>(`${this.apiUrl}/login`, { email, password });
  }

  register(data: any): Observable<AuthResponseDto> {
    return this.http.post<AuthResponseDto>(`${this.apiUrl}/register`, data).pipe(
      tap(res => this.storeTokens(res.tokens))
    );
  }

  refreshToken(): Observable<{ accessToken: string; refreshToken: string }> {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post<{ accessToken: string; refreshToken: string }>(
      `${this.apiUrl}/refresh`, { refreshToken }
    ).pipe(
      tap(tokens => this.storeTokens(tokens))
    );
  }

  getUserInfo(): Observable<UserAuthDto> {
    const accessToken = localStorage.getItem('accessToken');
    const headers = new HttpHeaders({ Authorization: `Bearer ${accessToken}` });
    return this.http.get<UserAuthDto>(`${this.apiUrl}/user-info`, { headers });
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  private storeTokens(tokens: { accessToken: string; refreshToken: string }) {
    localStorage.setItem('accessToken', tokens.accessToken);
    localStorage.setItem('refreshToken', tokens.refreshToken);
  }
}