import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { UserAuthDto } from './user-auth-dto';

export interface AuthResponseDto {
  user: UserAuthDto;
  tokens: {
    accessToken: string;
    refreshToken: string;
  };
}
