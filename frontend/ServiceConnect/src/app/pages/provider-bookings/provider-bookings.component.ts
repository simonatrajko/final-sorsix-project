import { Component } from '@angular/core';
import { BookingService } from '../../services/booking-service';
import { Booking } from '../../models/Booking';
import { PendingBookingComponent } from '../../components/pending-booking/pending-booking.component';
import { CommonModule } from '@angular/common';
import { ActiveBookingComponent } from '../../components/active-booking/active-booking.component';

@Component({
  selector: 'app-provider-bookings',
  imports: [PendingBookingComponent,CommonModule,ActiveBookingComponent],
  templateUrl: './provider-bookings.component.html',
  styleUrl: './provider-bookings.component.css'
})
export class ProviderBookingsComponent {
  constructor(private bookingService:BookingService){}
  pendingBookings:Booking[]=[]
  activeBookings:Booking[]=[]
  

  trackByBookingId(){

  }


  onConfirm(id:number){
    this.bookingService.confirmBooking(id).subscribe()
    let movedToActive=this.pendingBookings.find(pb=>pb.id==id)!
    this.activeBookings.push(movedToActive)
    this.pendingBookings=this.pendingBookings.filter(pb=>pb.id!=id)
  }

  onReject(id:number){
    this.bookingService.rejectBooking(id).subscribe()
    this.pendingBookings=this.pendingBookings.filter(pb=>pb.id!=id)
  }

  handleComplete(id:number){
    this.activeBookings=this.activeBookings.filter(ab=>ab.id!=id)
    this.bookingService.completeBooking(id).subscribe()
  }

  ngOnInit(){
    this.bookingService.getPendingBookingsForProvider().subscribe(res=>{
      this.pendingBookings=res.content      
    })
    this.bookingService.getConfirmedBookingsForProvider().subscribe(res=>{
      this.activeBookings=res.content
    })
  }
}
