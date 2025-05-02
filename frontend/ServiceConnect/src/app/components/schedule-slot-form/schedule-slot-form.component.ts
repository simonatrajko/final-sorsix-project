import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ScheduleSlot, Status } from '../../models/ScheduleSlot';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-schedule-slot-form',
  templateUrl: './schedule-slot-form.component.html',
  imports:[ReactiveFormsModule,CommonModule]
})
export class ScheduleSlotFormComponent implements OnInit {
  @Output() slotCreated = new EventEmitter<ScheduleSlot>();
  slotForm!: FormGroup;
  statuses = Object.values(Status);
  submited=false
  constructor(private fb: FormBuilder) {
    
  }

  ngOnInit(): void {
    this.slotForm = this.fb.group({
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      status: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.slotForm.valid) {
      const formValue = this.slotForm.value;
      const newSlot: ScheduleSlot = {
        ...formValue,
        startTime: new Date(formValue.startTime),
        endTime: new Date(formValue.endTime),
        createdAt: new Date()
      };
      this.submited=true
      this.slotCreated.emit(newSlot);
    }
  }
}
