import { Component, Input } from '@angular/core';
import { Booking } from '../../models/Booking';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking-service';

@Component({
  selector: 'app-booked-service',
  imports: [CommonModule],
  templateUrl: './booked-service.component.html',
  styleUrl: './booked-service.component.css'
})
export class BookedServiceComponent {
  @Input({required:true})booking!:Booking

  constructor(private bookingService:BookingService){}

  cancelBooking(){
    this.bookingService.cancelBooking(this.booking.id).subscribe(res=>console.log(res))
    this.booking.status="CANCELLED"
  }


  private cleanTimeString(time: string): string {
  return time.split('.')[0];
}

  ngOnInit(){
    this.booking.slot.startTime=this.cleanTimeString(this.booking.slot.startTime)
    this.booking.slot.endTime=this.cleanTimeString(this.booking.slot.endTime)
  }
}
