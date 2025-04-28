import { Component } from '@angular/core';
import { Provider } from '../../models/Provider';
import { ProviderCardComponent } from '../../components/provider-card/provider-card.component';
@Component({
  selector: 'app-providers',
  imports: [ProviderCardComponent],
  templateUrl: './providers.component.html',
  styleUrl: './providers.component.css'
})
export class ProvidersComponent {
  providers:Provider[];
  constructor() {
    this.providers=JSON.parse(localStorage.getItem("users")!!).filter((u:Provider)=>u.role=="provider")
  }
  
}
