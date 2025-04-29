import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { User } from '../../models/User';
import { Provider } from '../../models/Provider';
@Component({
  selector: 'app-provider-card',
  imports: [],
  templateUrl: './provider-card.component.html',
  styleUrl: './provider-card.component.css'
})
export class ProviderCardComponent {
  @Input({ required: true }) provider = new Provider();
  
}
