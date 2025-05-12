import { Component } from '@angular/core';
import { ProviderDTO } from '../../models/ProviderDTO';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProviderService } from '../../services/provider.service';
import { ServiceDTO } from '../../models/ServiceDto';
import { ServiceManagerService } from '../../services/service-manager.service';
import { BookableServiceComponent } from '../bookable-service/bookable-service.component';

@Component({
  selector: 'app-provider-card',
  imports: [RouterLink,BookableServiceComponent],
  templateUrl: './provider-card.component.html',
  styleUrl: './provider-card.component.css'
})
export class ProviderCardComponent {
  id:number
  provider!:ProviderDTO
  services:ServiceDTO[]=[]
  constructor(route:ActivatedRoute,private providerService:ProviderService,private serviceManager:ServiceManagerService){
    this.id=Number(route.snapshot.paramMap.get("id"))
  }

  ngOnInit(){
    this.providerService.getProviderById(this.id).subscribe(res=>{
      this.provider=res
      this.serviceManager.getMyServicesProviderId(this.provider.id).subscribe(res=>{
        this.services=res
      })
    })
    
  }
}
