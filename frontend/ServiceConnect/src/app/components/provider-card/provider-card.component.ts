import { Component } from '@angular/core';
import { ProviderDTO } from '../../models/ProviderDTO';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProviderService } from '../../services/provider.service';

@Component({
  selector: 'app-provider-card',
  imports: [RouterLink],
  templateUrl: './provider-card.component.html',
  styleUrl: './provider-card.component.css'
})
export class ProviderCardComponent {
  id:number
  provider!:ProviderDTO
  constructor(route:ActivatedRoute,private providerService:ProviderService){
    this.id=Number(route.snapshot.paramMap.get("id"))
  }

  ngOnInit(){
    this.providerService.getProviderById(this.id).subscribe(res=>this.provider=res)
  }
}
