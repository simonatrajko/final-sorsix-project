import { DayOfWeek } from './ScheduleSlot';

export interface CreateSlotRequest {
  startTime: string;     
  endTime: string;       
  dayOfWeek: DayOfWeek;
  slotId?: number;
}
