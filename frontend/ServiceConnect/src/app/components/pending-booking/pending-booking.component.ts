import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Booking } from '../../models/Booking';
import { BookingService } from '../../services/booking-service';

@Component({
  selector: 'app-pending-booking',
  templateUrl: './pending-booking.component.html',
})
export class PendingBookingComponent {
  @Input({ required: true }) booking!: Booking;
  @Output() confirmed = new EventEmitter<number>();
  @Output() rejected = new EventEmitter<number>();

  confirm() {
    this.confirmed.emit(this.booking.id);
  }

  ngOnInit(){
    let arr =this.booking.slot.startTime.split(":")
    this.booking.slot.startTime=`${arr[0]}:${arr[1]}`
    let arr2 =this.booking.slot.endTime.split(":")
    this.booking.slot.endTime=`${arr2[0]}:${arr2[1]}`
  }

  reject() {
    this.rejected.emit(this.booking.id);
  }
}
