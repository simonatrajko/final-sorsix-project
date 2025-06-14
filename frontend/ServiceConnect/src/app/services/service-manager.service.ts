import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ServiceDTO } from '../models/ServiceDto';
import { CreateServiceRequest } from '../models/CreateServiceRequest';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ServiceManagerService {


  private  apiUrl="http://localhost:8080/api/services"
  
  constructor(private http:HttpClient) { }

  deleteService(id:number):Observable<void>{
      return this.http.delete<any>(`${this.apiUrl}/${id}`)
    }

  getAllServices() {
    return this.http.get<any>(`${this.apiUrl}`);
  }
  
   createService(request: CreateServiceRequest): Observable<ServiceDTO> {
    return this.http.post<ServiceDTO>(this.apiUrl, request);
  }

 updatePrice(id: number, price: number): Observable<ServiceDTO> {
  return this.http.patch<ServiceDTO>(`${this.apiUrl}/${id}/price`, { price });
}

  getServicesByCategoryName(categoryName: string): Observable<ServiceDTO[]> {
  return this.http.get<ServiceDTO[]>(`${this.apiUrl}/category/name/${categoryName}`);
  }
  
  getServicesByCategoryId(id: string): Observable<ServiceDTO[]> {
  return this.http.get<ServiceDTO[]>(`${this.apiUrl}/category/${id}`);
  }



  getMyServicesProvider(): Observable<ServiceDTO[]> {
  return this.http.get<ServiceDTO[]>(`${this.apiUrl}/myServicesProvider`);
}
 getMyServicesProviderId(id: number): Observable<ServiceDTO[]> {
  return this.http.get<ServiceDTO[]>(`${this.apiUrl}/providerServices/${id}`)
}


}
