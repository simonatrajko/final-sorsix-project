import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreateSlotRequest } from '../../models/CreateSlotRequest';
import { DayOfWeek } from '../../models/ScheduleSlot';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';

@Component({
  selector: 'app-schedule-slot-form',
  templateUrl: './schedule-slot-form.component.html',
  imports:[ReactiveFormsModule,CommonModule]
})
export class ScheduleSlotFormComponent  {
 form: FormGroup;
  days = Object.values(DayOfWeek);
  @Output() makeSelfInvisible = new EventEmitter<boolean>()
  constructor(private fb: FormBuilder,private scheduleSlotService:ScheduleSlotsService) {
    this.form = this.fb.group({
      startTime: ['', Validators.required],
      endTime: ['', Validators.required ],
      dayOfWeek: ['', Validators.required],
    });
  }

  submit() {
    if (this.form.valid) {
      const start = this.form.value.startTime;
      const end = this.form.value.endTime;
      console.log(start)
      console.log(end)
      const request: CreateSlotRequest = {
        startTime: start,
        endTime: end,
        dayOfWeek: this.form.value.dayOfWeek,
      };
      this.scheduleSlotService.bookSchedule(request).subscribe(response=>console.log(response))
      this.makeSelfInvisible.emit(false)
    }
  }
}
