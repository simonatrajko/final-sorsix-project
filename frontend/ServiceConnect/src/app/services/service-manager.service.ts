import { Injectable } from '@angular/core';
import { Service } from '../models/Service';
@Injectable({
  providedIn: 'root'
})
export class ServiceManagerService {


  services:Service[]
  constructor() { 
    this.services = JSON.parse(localStorage.getItem("services") || '[]') as Service[];
  }


  addService(service:Service){
    this.services.push(service)
    localStorage.setItem("services",JSON.stringify(this.services))
  }
}
