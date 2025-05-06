import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { CommonModule } from '@angular/common';
import { Status } from '../../models/ScheduleSlot';
@Component({
  selector: 'app-schedule-slot-card',
  imports: [CommonModule],
  templateUrl: './schedule-slot-card.component.html',
  styleUrl: './schedule-slot-card.component.css'
})
export class ScheduleSlotCardComponent {
  @Input() slot!:ScheduleSlot
  
  @Output() deleted:EventEmitter<void> = new EventEmitter();
  @Output() aproved:EventEmitter<void> = new EventEmitter();
  @Output() disaproved:EventEmitter<void> = new EventEmitter();
  onDelete(){
    this.deleted.emit()
  }


  disaproveSchedule(){
    this.disaproved.emit()
  }

  aproveSchedule(){
    this.aproved.emit()
  }
}
