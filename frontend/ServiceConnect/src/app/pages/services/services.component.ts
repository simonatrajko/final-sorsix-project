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
  services: ServiceDTO[] = [];         
  allServices: ServiceDTO[] = [];      

  constructor(private serviceManager: ServiceManagerService) {}

  ngOnInit() {
    this.serviceManager.getAllServices().subscribe(services => {
      this.allServices = services.content;
      this.services = [...this.allServices]; 
    });
  }

  filterByCategory(e: Event) {
    const val = (e.target as HTMLSelectElement).value;
    if (val === "") {
      this.services = [...this.allServices];
    } else {
      this.serviceManager.getServicesByCategoryId(val).subscribe(res => {
        this.services = res;
      });
    }
  }
}

