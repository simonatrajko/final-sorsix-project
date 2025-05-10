import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { BookingRequestDto } from '../../models/bookingRequestDto';
import { BookingService } from '../../services/booking-service';

@Component({
  selector: 'app-bookable-schedule-slot',
  templateUrl: './bookable-schedule-slot.component.html',
  styleUrls: ['./bookable-schedule-slot.component.css'],
  imports:[ReactiveFormsModule]
})
export class BookableScheduleSlotComponent implements OnInit {
  @Input({required:true}) slot!: ScheduleSlot;
  @Input({required:true}) serviceId!:number;
  bookingForm!: FormGroup;
  @Output() delete:EventEmitter<number> = new EventEmitter()
  constructor(private fb: FormBuilder,private bookingService:BookingService) {}

  ngOnInit(): void {
    this.bookingForm = this.fb.group({
      isRecurring: [false]
    });
  }

  handleBooking(): void {
    const isRecurringF = this.bookingForm.value.isRecurring;
    const bookingRequestDto = {
      isRecurring: isRecurringF,
      slotId: this.slot.id
    };
    this.bookingService.bookService(bookingRequestDto,this.serviceId).subscribe(res=>{
      this.delete.emit(this.slot.id)
    })
  }
}
