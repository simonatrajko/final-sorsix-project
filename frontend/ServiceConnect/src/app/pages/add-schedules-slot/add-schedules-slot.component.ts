import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ScheduleSlot, Status } from "../../models/ScheduleSlot";
import { ScheduleService } from '../../services/schedule.service';

@Component({
  selector: 'app-add-schedule-slot',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-schedules-slot.component.html',
})
export class AddScheduleSlotComponent implements OnInit {
  slotForm: FormGroup;
  providerUsername: string = '';
  serviceTitle: string = '';
  statusOptions = Object.values(Status);

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private scheduleService: ScheduleService
  ) {
    this.slotForm = this.fb.group({
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      status: ['free', Validators.required],
    });
  }

  ngOnInit(): void {
    this.providerUsername = this.route.snapshot.paramMap.get('username')!;
    this.serviceTitle = this.route.snapshot.paramMap.get('service')!;
  }

  onSubmit() {
    if (this.slotForm.valid) {
      const slot: ScheduleSlot = {
        ...this.slotForm.value,
        providerUsername: this.providerUsername,
        serviceTitle: this.serviceTitle,
        createdAt: new Date()
      };

      this.scheduleService.createSlot(slot);
    }
  }
}
