export interface Booking {
  id: number;
  createdAt: string;
  isRecurring: boolean;
  client: Client;
  provider: Provider;
  service: Service;
  slot: Slot;
  status:string;
}

export interface Client {
  id: number;
  username: string;
  hashedPassword: string;
  email: string;
  fullName: string;
  location: string;
  notificationPreferences: any;
  preferredContactMethod: string;
  profileImage: string;
}

export interface Provider {
  id: number;
  username: string;
  hashedPassword: string;
  email: string;
  fullName: string;
  location: string;
  profileImage: string;
  bio: string;
  languages: string;
  yearsOfExperience: number;
}

 interface Service {
  id: number;
  title: string;
  description: string;
  category: Category;
  price: number;
  duration: number;
  createdAt: string;
  provider: Provider;
}

 interface Category {
  id: number;
  name: string;
}

 interface Slot {
  id: number;
  startTime: string;
  endTime: string;
  created_at: string;
  dayOfWeek: string;
  status: string;
  provider: Provider;
}
