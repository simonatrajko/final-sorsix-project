import { User } from "./User";

export enum Status {
  AVAILABLE = 'AVAILABLE',
  BOOKED = 'BOOKED'
}

export enum DayOfWeek {
  MONDAY = 'MONDAY',
  TUESDAY = 'TUESDAY',
  WEDNESDAY = 'WEDNESDAY',
  THURSDAY = 'THURSDAY',
  FRIDAY = 'FRIDAY',
  SATURDAY = 'SATURDAY',
  SUNDAY = 'SUNDAY'
}


export interface ScheduleSlot {
  id: number;
  startTime: string;         
  endTime: string;           
  dayOfWeek: DayOfWeek;
  status: Status;
  created_at: string;
  provider: User;
}
