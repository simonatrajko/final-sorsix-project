import { Component, Input, OnInit } from '@angular/core';
import { Service } from '../../models/Service';
import { ActivatedRoute } from '@angular/router';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { CommonModule } from '@angular/common';
import { BookableScheduleSlotComponent } from '../bookable-schedule-slot/bookable-schedule-slot.component';
import { ServiceDTO } from '../../models/ServiceDto';

@Component({
  standalone:true,
  selector: 'app-bookable-service',
  imports: [CommonModule,BookableScheduleSlotComponent],
  templateUrl: './bookable-service.component.html',
  styleUrl: './bookable-service.component.css'
})
export class BookableServiceComponent implements OnInit {
  @Input({required:true}) service?:ServiceDTO;
  
  constructor(){
    
  }
  ngOnInit(): void {
    console.log(this.service)
  }

  
}
