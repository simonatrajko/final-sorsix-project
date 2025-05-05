export interface UserAuthDto {
  id: number;
  username: string;
  email: string;
  fullName: string;
  profileImage?: string;
  location?: string;
  userType: string; // "PROVIDER" or "SEEKER"
  // add provider/seeker-specific fields as needed
  yearsOfExperience?: number;
  bio?: string;
  languages?: string;
  preferredContactMethod?: string;
  notificationPreferences?: string;
}
