import { Component } from '@angular/core';
import { ServiceManagerService } from '../../services/service-manager.service';
import { BookableServiceComponent } from '../../components/bookable-service/bookable-service.component';
import { ServiceDTO } from '../../models/ServiceDto';

@Component({
  selector: 'app-services',
  imports: [BookableServiceComponent],
  templateUrl: './services.component.html',
  styleUrl: './services.component.css'
})
export class ServicesComponent {
  services:ServiceDTO[]=[]

  constructor(private serviceManager:ServiceManagerService){
    
  }

  ngOnInit() {
    this.serviceManager.getAllServices().subscribe(services => {
      console.log(services)
      this.services = services.content;
    });
  }
  
  
}
