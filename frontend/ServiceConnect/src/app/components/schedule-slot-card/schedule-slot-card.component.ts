import { Component, EventEmitter, Input, input, Output } from '@angular/core';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-schedule-slot-card',
  imports: [CommonModule],
  templateUrl: './schedule-slot-card.component.html',
  styleUrl: './schedule-slot-card.component.css'
})
export class ScheduleSlotCardComponent {
  @Input() slot!:ScheduleSlot
  @Output() deleted:EventEmitter<void> = new EventEmitter();
  onDelete(){
    this.deleted.emit()
  }
}
