import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Booking } from "../models/Booking";


@Injectable( {providedIn:"root"} )
export class BookingService{
    private apiUrl = "http://localhost:8080/api/bookings"
    constructor(private http:HttpClient){}

    bookService(req:Object,serviceId:number){
        return this.http.post(`${this.apiUrl}/${serviceId}`,req)
    }
    getBookedServicesForSeeker() {
    return this.http.get<Booking[]>(`${this.apiUrl}/my-services-seeker`);
    }

    cancelBooking(id: number, cancelAllRecurring: boolean = true) {
    return this.http.put<Booking>(`${this.apiUrl}/${id}/cancel`, null, {
    params: { cancelAllRecurring: cancelAllRecurring.toString() }
  });
    }
    getConfirmedBookingsForProvider(page: number = 0, size: number = 10) {
  return this.http.get<any>(`${this.apiUrl}/provider/confirmed`, {
    params: {
      page: page.toString(),
      size: size.toString()
    }
  });
    }

    getPendingBookingsForProvider(page: number = 0, size: number = 10) {
  return this.http.get<any>(`${this.apiUrl}/provider/pending`, {
    params: {
      page: page.toString(),
      size: size.toString()
    }
  });

  
}

completeBooking(id: number) {
  return this.http.put(`${this.apiUrl}/${id}/complete`, null);
}

rejectBooking(id: number) {
  return this.http.put(`${this.apiUrl}/${id}/reject`, null);
}

confirmBooking(id: number) {
  return this.http.put(`${this.apiUrl}/${id}/confirm`, null);
}

}