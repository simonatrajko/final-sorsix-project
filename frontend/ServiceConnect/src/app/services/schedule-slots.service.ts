import { Injectable } from '@angular/core';
import { ScheduleSlot } from '../models/ScheduleSlot';

@Injectable({
  providedIn: 'root'
})
export class ScheduleSlotsService {

  constructor() { }

  handleInitialScheduleSlots(slots:ScheduleSlot[],username:string){
      localStorage.setItem(`${username}slots`,JSON.stringify(slots))
  }
}
