import { Component,  EventEmitter,  Input, Output } from '@angular/core';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { CommonModule } from '@angular/common';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';

@Component({
  selector: 'app-schedule-slot-card',
  imports: [CommonModule],
  templateUrl: './schedule-slot-card.component.html',
  styleUrl: './schedule-slot-card.component.css'
})
export class ScheduleSlotCardComponent {
  constructor(private scheduleSlotService:ScheduleSlotsService){

  }
  @Input() slot!:ScheduleSlot
  @Output() deleted:EventEmitter<number> = new EventEmitter()

  ngOnInit(){
    this.slot.created_at=new Date(this.slot.created_at).toString()
    let arr1=this.slot.startTime.split(":")
    let arr2=this.slot.endTime.split(":")
    this.slot.startTime=`${arr1[0]}:${arr1[1]}`
    this.slot.endTime=`${arr2[0]}:${arr2[1]}`
  }

  handleDelete(){
    this.scheduleSlotService.deleteById(this.slot.id).subscribe(res=>console.log(res))
    this.deleted.emit(this.slot.id)
  }
}
