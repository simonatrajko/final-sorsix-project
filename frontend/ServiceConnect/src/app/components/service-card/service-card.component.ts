import { Component, Input } from '@angular/core';
import { Service } from '../../models/Service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-service-card',
  templateUrl: './service-card.component.html',
  styleUrls: ['./service-card.component.css'],
  imports:[CommonModule]
})
export class ServiceCardComponent {
  @Input({ required: true }) service!: Service;
}
