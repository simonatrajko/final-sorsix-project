import { Component } from '@angular/core';
import { User } from '../../models/User';
import { ProviderCardComponent } from '../../components/provider-card/provider-card.component';
@Component({
  selector: 'app-providers',
  imports: [ProviderCardComponent],
  templateUrl: './providers.component.html',
  styleUrl: './providers.component.css'
})
export class ProvidersComponent {
  providers:User[];
  constructor() {
    this.providers=JSON.parse(localStorage.getItem("users")!!).filter((u:User)=>u.role=="provider")
  }
  
}
