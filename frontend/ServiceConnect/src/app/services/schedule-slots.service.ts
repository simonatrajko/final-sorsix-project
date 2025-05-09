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

  getFreeSlotsByUser(username:string){
    const freeScheduleSlots=JSON.parse(localStorage.getItem(`${username}slots`) || "[]") as ScheduleSlot[]
    return freeScheduleSlots.filter(s=>s.status=="free")
  }
}
