import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServiceDTO } from '../../models/ServiceDto';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { BookableScheduleSlotComponent } from '../bookable-schedule-slot/bookable-schedule-slot.component';

@Component({
  standalone:true,
  selector: 'app-bookable-service',
  imports: [CommonModule,BookableScheduleSlotComponent],
  templateUrl: './bookable-service.component.html',
  styleUrl: './bookable-service.component.css'
})
export class BookableServiceComponent implements OnInit {
  @Input({required:true}) service!:ServiceDTO;
  days=['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
  slots:ScheduleSlot[]=[]
  constructor(private scheduleSlotService:ScheduleSlotsService){
    
  }
  ngOnInit(): void {
    console.log(this.service)
  }

  onDayChange(e:Event){
    let select=e.target as HTMLSelectElement
    let day = select.value
    this.scheduleSlotService.getScheduleSlotsByDay(day,this.service.id).subscribe(res=>{
      this.slots=res.content
      console.log(this.slots)
    })
  }

  handleDelete(id:number){
    this.slots=this.slots.filter(s=>s.id!=id)
  }
  
}
