import { Component, Input } from '@angular/core';
import { Service } from '../../models/Service';

@Component({
  selector: 'app-service-card',
  templateUrl: './service-card.component.html',
  styleUrls: ['./service-card.component.css']
})
export class ServiceCardComponent {
  @Input({ required: true }) service!: Service;
}
