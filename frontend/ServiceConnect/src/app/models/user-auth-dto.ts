export interface UserAuthDto {
    id: number;
    username: string;
    email: string;
    fullName: string;
    profileImage?: string;
    location?: string;
    userType: string;
    yearsOfExperience?: number;
    bio?: string;
    languages?: string;
    preferredContactMethod?: string;
    notificationPreferences?: string;
  }
