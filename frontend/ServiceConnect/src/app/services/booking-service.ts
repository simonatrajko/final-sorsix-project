import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";


@Injectable( {providedIn:"root"} )
export class BookingService{
    private apiUrl = "http://localhost:8080/api/bookings"
    constructor(private http:HttpClient){}

    bookService(req:Object,serviceId:number){
        return this.http.post(`${this.apiUrl}/${serviceId}`,req)
    }
}