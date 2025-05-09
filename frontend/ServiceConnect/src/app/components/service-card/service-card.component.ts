import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServiceDTO } from '../../models/ServiceDto';

@Component({
  selector: 'app-service-card',
  templateUrl: './service-card.component.html',
  styleUrls: ['./service-card.component.css'],
  imports:[CommonModule]
})
export class ServiceCardComponent {
  @Input({ required: true }) service!: ServiceDTO;

  
}
