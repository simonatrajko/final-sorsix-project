import { Injectable } from '@angular/core';
import { Service } from '../models/Service';
import { HttpClient } from '@angular/common/http';
import { ServiceDTO } from '../models/ServiceDto';
@Injectable({
  providedIn: 'root'
})
export class ServiceManagerService {


  private  apiUrl="http://localhost:8080/api/services"
  constructor(private http:HttpClient) { 

  }

  getAllServices() {
    return this.http.get<any>(`${this.apiUrl}`);
  }
  
  addService(service:Service){
   
  }
}
