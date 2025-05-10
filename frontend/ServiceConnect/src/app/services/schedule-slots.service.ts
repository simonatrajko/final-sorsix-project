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
  
  deleteById(id:number){
    return this.http.delete(`${this.apiUrl}/slots/${id}`)
  }

  // ova e ko ce si najaven kako provider 
  getAllSlotsByProvider(){
    return this.http.get<ScheduleSlot[]>(`${this.apiUrl}/provider/slots`)
  }

  // ova ko ce si najaven kako seeker da vidis za toj service koj se slobodni schedule slots
  getScheduleSlotsByDay(day: string, serviceId: number) {
  return this.http.get<any>(`${this.apiUrl}/services/${serviceId}/available-slots/day`, {
    params: { dayOfWeek: day }
  });
}
}
