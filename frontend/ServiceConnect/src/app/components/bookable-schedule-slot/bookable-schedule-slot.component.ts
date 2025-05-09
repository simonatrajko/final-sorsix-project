import { Component, Input } from '@angular/core';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking.service';

@Component({
  selector: 'app-bookable-schedule-slot',
  imports: [CommonModule],
  templateUrl: './bookable-schedule-slot.component.html',
  styleUrl: './bookable-schedule-slot.component.css'
})
export class BookableScheduleSlotComponent {
  @Input({ required: true }) scheduleSlot!:ScheduleSlot
  @Input({required:true}) seeker!:string|null

  constructor(private bookingService:BookingService){

  }
  handleBooking(){
    // const provider=this.scheduleSlot.providerUsername
    // const id = this.scheduleSlot.id
    // this.bookingService.book(provider,this.seeker,id)
  }
}
