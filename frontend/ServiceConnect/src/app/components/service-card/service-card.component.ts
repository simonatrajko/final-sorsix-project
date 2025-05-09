import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServiceDTO } from '../../models/ServiceDto';
import { ServiceManagerService } from '../../services/service-manager.service';

@Component({
  selector: 'app-service-card',
  templateUrl: './service-card.component.html',
  styleUrls: ['./service-card.component.css'],
  imports:[CommonModule]
})
export class ServiceCardComponent {
  @Input({ required: true }) service!: ServiceDTO;
  @Output() deleted:EventEmitter<number> = new EventEmitter
  constructor(private serviceManager:ServiceManagerService){

  }
  deleteSelf(){
    this.serviceManager.deleteService(this.service.id).subscribe(res=>{
    this.deleted.emit(this.service.id)
    })
  }
}
