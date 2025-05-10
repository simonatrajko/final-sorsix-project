import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Booking } from '../../models/Booking';

@Component({
  selector: 'app-active-booking',
  imports: [],
  templateUrl: './active-booking.component.html',
  styleUrl: './active-booking.component.css'
})
export class ActiveBookingComponent {
  @Input({ required: true }) booking!: Booking;

   @Output() completed = new EventEmitter<number>();

   complete(){
    
    this.completed.emit(this.booking.id)
   }
}
