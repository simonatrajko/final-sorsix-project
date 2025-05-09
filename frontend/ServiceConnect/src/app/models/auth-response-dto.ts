import { UserAuthDto } from './user-auth-dto';

export interface AuthResponseDto {
  user: UserAuthDto;
  tokens: {
    accessToken: string;
    refreshToken: string;
  };
}