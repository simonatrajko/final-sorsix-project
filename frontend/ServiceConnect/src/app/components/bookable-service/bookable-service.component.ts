import { Component, Input, OnInit } from '@angular/core';
import { Service } from '../../models/Service';
import { ActivatedRoute } from '@angular/router';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { CommonModule } from '@angular/common';
import { BookableScheduleSlotComponent } from '../bookable-schedule-slot/bookable-schedule-slot.component';

@Component({
  standalone:true,
  selector: 'app-bookable-service',
  imports: [CommonModule,BookableScheduleSlotComponent],
  templateUrl: './bookable-service.component.html',
  styleUrl: './bookable-service.component.css'
})
export class BookableServiceComponent implements OnInit {
  @Input({required:true}) service?:Service;
  seeker:string|null
  provider:string|undefined
  scheduleSlots?:ScheduleSlot[]
  constructor(private route:ActivatedRoute,private scheduleSlotService:ScheduleSlotsService){
    this.seeker=this.route.snapshot.paramMap.get("username")
  }
  ngOnInit(): void {
    this.provider=this.service?.providerUserName
    this.scheduleSlots=this.scheduleSlotService.getFreeSlotsByUser(this.provider!)
    
  }

  
}
