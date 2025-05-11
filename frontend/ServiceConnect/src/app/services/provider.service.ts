import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ProviderDTO } from "../models/ProviderDTO";
@Injectable({providedIn:"root"})

export class ProviderService{
    constructor(private http:HttpClient){}

    private apiUrl="http://localhost:8080/api/providers"

    searchProviders(name:string){
        return this.http.get<ProviderDTO[]>(`${this.apiUrl}/search`,{params:{name:name}})
    }
    getProviderById(id:number){
        return this.http.get<ProviderDTO>(`${this.apiUrl}/${id}`)
    }
}