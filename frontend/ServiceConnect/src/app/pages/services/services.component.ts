import { Component } from '@angular/core';
import { ServiceManagerService } from '../../services/service-manager.service';
import { Service } from '../../models/Service';
import { BookableServiceComponent } from '../../components/bookable-service/bookable-service.component';

@Component({
  selector: 'app-services',
  imports: [BookableServiceComponent],
  templateUrl: './services.component.html',
  styleUrl: './services.component.css'
})
export class ServicesComponent {
  allServices:Service[]

  constructor(private serviceManager:ServiceManagerService){
    this.allServices=this.serviceManager.services
  }
}
