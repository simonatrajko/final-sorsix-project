import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateSlotRequest } from '../models/CreateSlotRequest';
import { ScheduleSlot } from '../models/ScheduleSlot';

@Injectable({
  providedIn: 'root'
})
export class ScheduleSlotsService {
  private apiUrl="http://localhost:8080/api/schedules"
  constructor(private http:HttpClient) { }

  bookSchedule(schedule:CreateSlotRequest){
    return this.http.post<ScheduleSlot>(`${this.apiUrl}/slots`, schedule);
  }
  

  getAllSlotsByProvider(){
    
  }
}
